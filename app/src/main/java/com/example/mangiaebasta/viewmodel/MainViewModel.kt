package com.example.mangiaebasta.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mangiaebasta.model.dataClasses.APILocation
import com.example.mangiaebasta.model.dataClasses.MenuDetailsWithImage
import com.example.mangiaebasta.model.dataClasses.MenuWithImage
import com.example.mangiaebasta.model.dataClasses.Order
import com.example.mangiaebasta.model.dataClasses.UserDetail
import com.example.mangiaebasta.model.dataClasses.UserUpdateParams
import com.example.mangiaebasta.model.repository.MenuRepository
import com.example.mangiaebasta.model.repository.OrderRepository
import com.example.mangiaebasta.model.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


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
) : ViewModel() {

    //Location di Default se non ho user location
    companion object {
        val DEFAULT_LOCATION = APILocation(
            lat = 45.4642,
            lng = 9.19,
        )
    }

    //TAG per Log
    private val TAG = MainViewModel::class.simpleName

    //Mutable State Flow o State Flow
    //_ se privata --> che può essere modificata all'interno della classe o dell'oggetto.
    //se non _ allora è pubblica e stato immutabile

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
        val newUserData = updatedData.copy(sid = _sid.value!!)
        userRepository.putUserData(_sid.value!!, _uid.value!!, newUserData)
        fetchUserDetails()
        return true
    }

    fun getUserRepository(): UserRepository {
        return userRepository
    }


    suspend fun fetchNearbyMenus() {
        viewModelScope.launch {
            //val location = DEFAULT_LOCATION

            val menus = menuRepository.getNearbyMenus(
                sid = _sid.value!!,
                lat = 45.4642,
                lng = 9.19
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
        val menu = menuRepository.getMenuDetail(
            sid = _sid.value!!,
            mid = mid,
            lat = 45.4642,
            lng = 9.19
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

    suspend fun newOrder(deliveryLocation: APILocation, mid: Int?) {
        if(mid == null) {
            Log.d(TAG, "Mid is null")
            return
        }
        Log.d(TAG, "New order: $deliveryLocation")
        val order = orderRepository.newOrder(
            sid = _sid.value!!,
            deliveryLocation = deliveryLocation,
            mid = mid
        )
        _lastOrderState.value = _lastOrderState.value.copy(
            lastOrder = order
        )

    }

    suspend fun fetchLastOrderedMenu() {
        val lastOrder = _lastOrderState.value.lastOrder
        if(lastOrder == null) {
            Log.d(TAG, "Last order is null")
            return
        }
        val menu = menuRepository.getMenuDetail(
            sid = _sid.value!!,
            mid = lastOrder.mid,
            lat = 45.4642,
            lng = 9.19
        )
        val image = menuRepository.getMenuImage(
            sid = _sid.value!!,
            mid = lastOrder.mid,
            imageVersion = menu.imageVersion
        )
        val menuDetailWithImage = MenuDetailsWithImage(menu, image)
        _lastOrderState.value = _lastOrderState.value.copy(
            lastOrderMenu = menuDetailWithImage
        )
    }

    suspend fun fetchOrderDetail(oid: Int?) {
        if(oid==null) {
            Log.d(TAG, "Oid is null")
            return
        }
        val order = orderRepository.getOrderDetail(
            oid = oid,
            sid = _sid.value!!
        )
        _lastOrderState.value = _lastOrderState.value.copy(
            lastOrder = order
        )
        _userState.value = _userState.value.copy(
            user = _userState.value.user?.copy(
                lastOid = oid,
                orderStatus = order.status.toString()
            )
        )
    }
}