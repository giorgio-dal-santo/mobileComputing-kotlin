package com.example.mangiaebasta.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangiaebasta.model.dataClasses.UserDetail
import com.example.mangiaebasta.model.dataClasses.UserUpdateParams
import com.example.mangiaebasta.model.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class ProfileViewModel(
    private val userRepository: UserRepository,
    initValues: UserDetail? = null,
) : ViewModel() {


    private val TAG = ProfileViewModel::class.simpleName
    private val _sid = MutableStateFlow<String?>(null)
    private val _uid = MutableStateFlow<Int?>(null)

    private val _formParams = MutableStateFlow(
        UserUpdateParams(
            firstName = initValues?.firstName ?: "",
            lastName = initValues?.lastName ?: "",
            cardFullName = initValues?.cardFullName ?: "",
            cardNumber = initValues?.cardNumber ?: "",
            cardExpireMonth = initValues?.cardExpireMonth ?: 1,
            cardExpireYear = initValues?.cardExpireYear ?: 2025,
            cardCVV = initValues?.cardCVV ?: "",
            sid = "",
        )
    )

    val formParams: StateFlow<UserUpdateParams> = _formParams

    init {
        viewModelScope.launch {
            fetchSessionData()
        }
        Log.d(TAG, "INIT ProfileViewModel")
    }

    fun fetchSessionData() {
        viewModelScope.launch {
            val us = userRepository.getUserSession()
            _sid.value = us.sid
            _uid.value = us.uid
        }
    }

    fun onFirstNameChange(value: String): Boolean {
        _formParams.value = _formParams.value.copy(firstName = value)

        return !(value.length > 15 || value.isEmpty())
    }

    fun onLastNameChange(value: String): Boolean {
        _formParams.value = _formParams.value.copy(lastName = value)

        return !(value.length > 15 || value.isEmpty())
    }

    fun onCardFullNameChange(value: String): Boolean {
        _formParams.value = _formParams.value.copy(cardFullName = value)

        return !(value.length > 31 || value.isEmpty())
    }

    fun onCardNumberChange(value: String): Boolean {
        _formParams.value = _formParams.value.copy(cardNumber = value)

        return (value.length == 16 && value.toLongOrNull() != null)
    }

    fun onCardExpireMonthChange(value: Int): Boolean {
        _formParams.value = _formParams.value.copy(cardExpireMonth = value)

        return (value in 1..12)
    }

    fun onCardExpireYearChange(value: Int): Boolean {
        _formParams.value = _formParams.value.copy(cardExpireYear = value)

        return (value >= Calendar.getInstance().get(Calendar.YEAR))
    }

    fun onCardCVVChange(value: String): Boolean {
        _formParams.value = _formParams.value.copy(cardCVV = value)

        return (value.length == 3 && value.toIntOrNull() != null)
    }

    suspend fun submit(submitCb: suspend (UserUpdateParams) -> Boolean): Boolean {
        Log.d(TAG, "Submitting with values: ${_formParams.value}")
        if (_formParams.value.firstName.isEmpty() || _formParams.value.lastName.isEmpty() ||
            _formParams.value.cardFullName.isEmpty() || _formParams.value.cardNumber.isEmpty() ||
            _formParams.value.cardCVV.isEmpty()
        ) {
            Log.d(TAG, "Invalid Form Data")
            return false
        }

        return submitCb(_formParams.value)
    }


}