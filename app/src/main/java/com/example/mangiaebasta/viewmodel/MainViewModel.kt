package com.example.mangiaebasta.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangiaebasta.model.dataClasses.APILocation
import com.example.mangiaebasta.model.dataClasses.Error
import com.example.mangiaebasta.model.dataClasses.MenuDetailsWithImage
import com.example.mangiaebasta.model.dataClasses.MenuWithImage
import com.example.mangiaebasta.model.dataClasses.Order
import com.example.mangiaebasta.model.dataClasses.UserDetail
import com.example.mangiaebasta.model.dataClasses.UserUpdateParams
import com.example.mangiaebasta.model.dataClasses.toAPILocation
import com.example.mangiaebasta.model.repository.MenuRepository
import com.example.mangiaebasta.model.repository.OrderRepository
import com.example.mangiaebasta.model.repository.UserRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException


data class UserState(
    val user: UserDetail? = null,
    val isUserRegistered: Boolean = false,
)

data class LastOrderState(
    val lastOrder: Order? = null,
    val lastOrderMenu: MenuDetailsWithImage? = null,
)

data class LocationState(
    val lastKnownLocation: APILocation? = null,
    val isLocationAllowed: Boolean = false,
    val hasCheckedPermissions: Boolean = false,
)

data class MenusExplorationState(
    val nearbyMenus: List<MenuWithImage> = emptyList(),
    val selectedMenu: MenuDetailsWithImage? = null,
    val reloadMenus: Boolean = false,
)

data class AppState(
    val isLoading: Boolean = true,
    val isFirstLaunch: Boolean = true,
    val error: Error? = null
)

class MainViewModel(
    private val userRepository: UserRepository,
    private val menuRepository: MenuRepository,
    private val orderRepository: OrderRepository,
    private val locationClient: FusedLocationProviderClient
) : ViewModel() {

    companion object {
        val DEFAULT_LOCATION = APILocation(
            lat = 45.4642,
            lng = 9.19,
        )
    }

    private val TAG = MainViewModel::class.simpleName

    private val _sid = MutableStateFlow<String?>(null)
    private val _uid = MutableStateFlow<Int?>(null)

    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState

    private val _lastOrderState = MutableStateFlow(LastOrderState())
    val lastOrderState: StateFlow<LastOrderState> = _lastOrderState

    private val _locationState = MutableStateFlow(LocationState())
    val locationState: StateFlow<LocationState> = _locationState

    private val _menusExplorationState = MutableStateFlow(MenusExplorationState())
    val menusExplorationState: StateFlow<MenusExplorationState> = _menusExplorationState

    private val _appState = MutableStateFlow(AppState())
    val appState: StateFlow<AppState> = _appState

    //Initialization & State Management
    init {
        setLoading(true)
        viewModelScope.launch {
            fetchUserSession()
            fetchUserDetails()
            setLoading(false)
        }
        Log.d(TAG, "INIT MainViewModel")
    }

    fun setLoading(isLoading: Boolean) {
        _appState.value = _appState.value.copy(isLoading = isLoading)
    }

    fun setError(error: Error) {
        if (_appState.value.error != null) return
        _appState.value = _appState.value.copy(error = error)
        Log.d(TAG, "set error: ${_appState.value.error}")
    }

    fun resetError() {
        _appState.value = _appState.value.copy(error = null)
        Log.d(TAG, "reset error: ${_appState.value.error}")
    }

    // Data Fetching and Updating
    suspend fun fetchUserSession() {
        val us = userRepository.getUserSession()
        _sid.value = us.sid
        _uid.value = us.uid
    }

    suspend fun fetchUserDetails() {
        if (!userRepository.isRegistered()) {
            _userState.value = _userState.value.copy(isUserRegistered = false)
            Log.d(TAG, "View Model Is registered ${_userState.value.isUserRegistered}")
            return
        }
        val user = userRepository.getUserData(
            sid = _sid.value!!,
            uid = _uid.value!!
        )
        _userState.value = _userState.value.copy(
            user = user,
            isUserRegistered = true
        )
    }

    suspend fun updateUserData(updatedData: UserUpdateParams): Boolean {
        try {
            Log.d(TAG, "Update user data")

            val newUserData = updatedData.copy(sid = _sid.value!!)
            userRepository.putUserData(_sid.value!!, _uid.value!!, newUserData)
        } catch (e: Error) {
            Log.e(TAG, "Error: ${e.message}")
            setError(e)
        } catch (e: CancellationException) {
            Log.w(TAG, "Error: $e")
        } catch (e: Exception) {
            Log.e(TAG, "Error: $e")
            setError(
                error = Error(
                    title = "Error",
                    message = "Account validation failed",
                )
            )
        }

        val success = _appState.value.error == null

        if (success) {
            fetchUserDetails()
        }

        return success
    }

    fun getUserRepository(): UserRepository {
        return userRepository
    }

    suspend fun fetchNearbyMenus() {
        viewModelScope.launch {

            val location = getCurrentAPILocation()

            val menus = menuRepository.getNearbyMenus(
                sid = _sid.value!!,
                lat = location.lat,
                lng = location.lng,
            )
            val menusWithNoImages = menus.map { menu ->
                MenuWithImage(menu, null)
            }
            _menusExplorationState.value = _menusExplorationState.value.copy(
                nearbyMenus = menusWithNoImages,
                reloadMenus = false
            )

            menus.forEach { menu ->
                val image = menuRepository.getMenuImage(
                    sid = _sid.value!!,
                    mid = menu.mid,
                    imageVersion = menu.imageVersion
                )

                val updatedMenu = MenuWithImage(menu, image)

                _menusExplorationState.value = _menusExplorationState.value.copy(
                    nearbyMenus = _menusExplorationState.value.nearbyMenus.map {
                        if (it.menu.mid == menu.mid) updatedMenu else it
                    }
                )
            }
        }
    }

    suspend fun fetchMenuDetail(mid: Int) {
        val location = getCurrentAPILocation()
        val menu = menuRepository.getMenuDetail(
            sid = _sid.value!!,
            mid = mid,
            lat = location.lat,
            lng = location.lng,
        )
        val image = menuRepository.getMenuImage(
            sid = _sid.value!!,
            mid = mid,
            imageVersion = menu.imageVersion
        )
        val menuDetailWithImage = MenuDetailsWithImage(menu, image)
        _menusExplorationState.value = _menusExplorationState.value.copy(
            selectedMenu = menuDetailWithImage
        )
    }

    suspend fun newOrder(deliveryLocation: APILocation, mid: Int?): Boolean {
        if (mid == null) {
            Log.d(TAG, "Mid is null")
            return false
        }

        try {
            Log.d(TAG, "New order: $mid")

            val order = orderRepository.newOrder(
                sid = _sid.value!!,
                deliveryLocation = deliveryLocation,
                mid = mid
            )
            _lastOrderState.value = _lastOrderState.value.copy(
                lastOrder = order
            )
            _userState.value = _userState.value.copy(
                user = _userState.value.user?.copy(
                    lastOid = order.oid,
                    orderStatus = order.status.toString()
                )
            )
        } catch (e: Error) {
            Log.e(TAG, "Error: ${e.message}")
            setError(e)
        } catch (e: CancellationException) {
            Log.w(TAG, "Error: $e")
        } catch (e: Exception) {
            Log.e(TAG, "Error: $e")
            setError(
                error = Error(
                    title = "Error",
                    message = "Error: ${e.message}",
                )
            )
        }

        val success = _appState.value.error == null

        if (success) {
            fetchLastOrderedMenu()
        }

        return success
    }

    suspend fun fetchLastOrderedMenu() {
        if (_lastOrderState.value.lastOrder?.mid == null) {
            Log.d(TAG, "Mid is null")
            return
        }

        val mid = _lastOrderState.value.lastOrder!!.mid

        val location = getCurrentAPILocation()

        val menu = menuRepository.getMenuDetail(
            sid = _sid.value!!,
            mid = mid,
            lat = location.lat,
            lng = location.lng,
        )
        val image = menuRepository.getMenuImage(
            sid = _sid.value!!,
            mid = mid,
            imageVersion = menu.imageVersion
        )
        val menuDetailWithImage = MenuDetailsWithImage(menu, image)
        _lastOrderState.value = _lastOrderState.value.copy(
            lastOrderMenu = menuDetailWithImage
        )
        Log.d(
            TAG,
            "fetch last ordered menu: ${_lastOrderState.value.lastOrderMenu?.menuDetails?.name}"
        )
    }

    suspend fun fetchLastOrderDetail() {
        if (_userState.value.user?.lastOid == null) {
            Log.d(TAG, "Oid is null")
            return
        }
        val order = orderRepository.getOrderDetail(
            oid = _userState.value.user!!.lastOid!!,
            sid = _sid.value!!
        )
        _lastOrderState.value = _lastOrderState.value.copy(
            lastOrder = order
        )
        _userState.value = _userState.value.copy(
            user = _userState.value.user?.copy(
                lastOid = order.oid,
                orderStatus = order.status.toString()
            )
        )
        Log.d(TAG, "fetch last order detail: ${_lastOrderState.value.lastOrder?.oid}")
        fetchLastOrderedMenu()
    }

    //Location Management
    fun allowLocation(location: Location) {
        setLastKnownLocation(location)
        if (_locationState.value.isLocationAllowed) return
        _locationState.value = _locationState.value.copy(
            isLocationAllowed = true,
        )
        _menusExplorationState.value = _menusExplorationState.value.copy(
            reloadMenus = true
        )
    }

    private fun setLastKnownLocation(location: Location) {
        val loc = location.toAPILocation()
        _locationState.value =
            _locationState.value.copy(lastKnownLocation = loc)
    }

    fun checkLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun subscribeToLocationUpdates(
        locationCallback: LocationCallback
    ) {
        val locationRequest = LocationRequest
            .Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000L)
            .setMinUpdateIntervalMillis(2000L)
            .setMinUpdateDistanceMeters(1.0F)
            .build()

        locationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    fun getCurrentAPILocation(): APILocation {
        val location = _locationState.value.lastKnownLocation
        if (location != null && _locationState.value.isLocationAllowed) {
            return APILocation(
                lat = location.lat,
                lng = location.lng
            )
        }
        return DEFAULT_LOCATION
    }
}