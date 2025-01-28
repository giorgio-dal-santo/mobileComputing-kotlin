package com.example.mangiaebasta.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import com.example.mangiaebasta.view.styles.buttonTextBlackStyle
import com.example.mangiaebasta.view.styles.goBackButtonModifier
import com.example.mangiaebasta.view.styles.signUpButtonModifier
import com.example.mangiaebasta.view.utils.accountForm.DropDownField
import com.example.mangiaebasta.view.utils.accountForm.FormField
import com.example.mangiaebasta.view.utils.button.StyledButton
import com.example.mangiaebasta.viewmodel.MainViewModel
import com.example.mangiaebasta.viewmodel.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun EditProfileScreen(viewModel: MainViewModel, onBackwardClick: () -> Unit) {

    val userState by viewModel.userState.collectAsState()

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


    val formParams by formViewModel.formParams.collectAsState()
    var submitFailed by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "Edit Profile Screen",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        FormField(
            label = "First Name",
            value = formParams.firstName,
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
            value = formParams.lastName,
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
            value = formParams.cardFullName,
            onValueChange = { formViewModel.onCardFullNameChange(it) },
            errorMessage = "Credit Card Name should be at most 31 characters and not empty",
            showError = submitFailed,
            keyBoardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
            )
        )

        FormField(
            label = "Card Number",
            value = formParams.cardNumber,
            onValueChange = { formViewModel.onCardNumberChange(it) },
            errorMessage = "Credit Number must be 16 digits",
            showError = submitFailed,
            keyBoardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            )
        )

        DropDownField(
            min = 1,
            max = 12,
            value = formParams.cardExpireMonth.toString(),
            onValueChange = { formViewModel.onCardExpireMonthChange(it.toInt()) }
        )

        DropDownField(
            min = Calendar.getInstance().get(Calendar.YEAR),
            max = Calendar.getInstance().get(Calendar.YEAR) + 10,
            value = formParams.cardExpireYear.toString(),
            onValueChange = { formViewModel.onCardExpireYearChange(it.toInt()) }
        )

        FormField(
            label = "CVV",
            value = formParams.cardCVV,
            onValueChange = { formViewModel.onCardCVVChange(it) },
            errorMessage = "Card CVV should be 3 digits",
            showError = submitFailed,
            keyBoardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        Column (
            modifier = Modifier.fillMaxWidth()
        ) {

            StyledButton(
                text = "Back",
                modifier = goBackButtonModifier,
                textStyle = buttonTextBlackStyle,
                onClick = { onBackwardClick() },
            )

            Spacer(modifier = Modifier.weight(1f))

            StyledButton(
                text = if (userState.isUserRegistered) "Save" else "Register",
                modifier = signUpButtonModifier,
                textStyle = buttonTextBlackStyle,
                onClick = {
                    CoroutineScope(Dispatchers.Main).launch {
                        val ok = formViewModel.submit(viewModel::updateUserData)
                        Log.d("EditAccountScreen", "Submit result is $ok")
                        if (ok) {
                            onBackwardClick()
                        } else {
                            submitFailed = true
                        }
                    }
                 },
            )
        }
    }
}
