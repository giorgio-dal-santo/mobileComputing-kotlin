package com.example.mangiaebasta.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
): ViewModel() {

    //Location di Default se non ho user location
    companion object {
        val DEFAULT_LOCATION = APILocation(
            latitude = 45.4642,
            longitude = 9.19,
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
        Log.d("Init", "INIT")
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
            Log.d("MainViewModel", "View Model Is registered ${_userState.value.isUserRegistered}")
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

    suspend fun updateUserData(updatededData : UserUpdateParams) : Boolean {
        userRepository.putUserData(_uid.value!!, updatededData)
        return true
    }

     fun getUserRepository() : UserRepository {
         return userRepository
     }
}