package com.example.mangiaebasta.view.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mangiaebasta.view.utils.FormField
import com.example.mangiaebasta.viewmodel.MainViewModel
import com.example.mangiaebasta.viewmodel.ProfileViewModel

@Composable
fun EditProfileScreen(viewModel: MainViewModel, onBackwardClick: () -> Unit) {

    val viewModelFactory = viewModelFactory {
        initializer {
            ProfileViewModel(
                viewModel.getUserRepository(),
                viewModel.userState.value.user
            )
        }
    }

    val formViewModel: ProfileViewModel = viewModel(
        factory = viewModelFactory
    )

    val userState by viewModel.userState.collectAsState()
    val appState by viewModel.appState.collectAsState()
    val formParams by formViewModel.formParams.collectAsState()
    var submitFailed by remember { mutableStateOf(false) }


    Column {
        Text("Edit Profile Screen")
        //INPUT DELL'UTENTE

        FormField(
            label = "First Name",
            value = formParams.firstName!!,
            onValueChange = { formViewModel.onFirstNameChange(it) },
            errorMessage = "First Name must be between 1 and 15 characters and not empty",
            showError = submitFailed,
            keyBoardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            )
        )

        FormField(
            label = "Last Name",
            value = formParams.lastName!!,
            onValueChange = { formViewModel.onLastNameChange(it) },
            errorMessage = "Last Name must be between 1 and 15 characters and not empty",
            showError = submitFailed,
            keyBoardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words
            )
        )

        FormField(
            label = "Card Full Name",
            value = formParams.cardFullName!!,
            onValueChange = { formViewModel.onCardFullNameChange(it) },
            errorMessage = "Credit Card Name should be at most 31 characters and not empty",
            showError = submitFailed,
            keyBoardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
            )
        )

        FormField(
            label = "Card Number",
            value = formParams.cardNumber!!,
            onValueChange = { formViewModel.onCardNumberChange(it)},
            errorMessage = "Credit Number must be 16 digits",
            showError = submitFailed,
            keyBoardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            )
        )

        FormField(
            label = "Card Expire Year",
            value = formParams.cardExpireYear!!.toString(),
            onValueChange = { formViewModel.onCardExpireYearChange(it)},
            errorMessage = "Card Expire Year must be 4 digits and after 2025 ",
            showError = submitFailed,
            keyBoardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
            )
        )
        FormField(
            label = "Card Expire Month",
            value = formParams.cardExpireMonth!!.toString(),
            onValueChange = { formViewModel.onCardExpireMonthChange(it)},
            errorMessage = "Card Expire Year must be 2 digits ",
            showError = submitFailed,
            keyBoardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
            )
        )

        FormField(
            label = "CVV",
            value = formParams.cardCVV!!,
            onValueChange = { formViewModel.onCardCVVChange(it) },
            errorMessage = "Card CVV should be 3 digits",
            showError = submitFailed,
            keyBoardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )


        Spacer(modifier = Modifier.weight(1f))

        //SUMBIT
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
/*
            LargeButton(
                text = if (newAccount) "Create Account" else "Save",
                onPress = {
                    CoroutineScope(Dispatchers.Main).launch {
                        val ok = formViewModel.submit(viewModel::updateUserData)
                        Log.d("AddEditAccountScreen", "Submit result is $ok")
                        if (ok)
                            onBackClick()
                        else
                            submitFailed = true
                    }
                }
            )
            */

        }






        Button(onClick = {onBackwardClick()}) {
            Text("Back")
        }
    }
}
