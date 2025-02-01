package com.example.mangiaebasta.view.utils.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.mangiaebasta.model.dataClasses.UserDetail
import com.example.mangiaebasta.view.styles.GlobalCardStyles
import com.example.mangiaebasta.view.styles.GlobalDimensions
import com.example.mangiaebasta.view.styles.GlobalTypography
import com.example.mangiaebasta.view.styles.buttonTextWhiteStyle
import com.example.mangiaebasta.view.styles.editProfileButtonModifier
import com.example.mangiaebasta.view.styles.signUpButtonModifier
import com.example.mangiaebasta.view.utils.button.StyledButton

@Composable
fun ProfileCard(
    user: UserDetail,
    onPress: () -> Unit
) {
    ProfileCardBody(
        cardFullName = user.cardFullName,
        cardNumber = user.cardNumber,
        expireMonth = user.cardExpireMonth,
        expireYear = user.cardExpireYear,
        cvv = user.cardCVV,
        onPress = onPress
    )
}

@Composable
fun ProfileCardBody(
    cardFullName: String?,
    cardNumber: String?,
    expireMonth: Int?,
    expireYear: Int?,
    cvv: String?,
    onPress: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(GlobalCardStyles.CardPadding)
            .fillMaxWidth(),
        shape = GlobalCardStyles.CardShape,
        elevation = CardDefaults.cardElevation(GlobalCardStyles.CardElevation),
        colors = GlobalCardStyles.CardColors
    ) {
        Column(
            modifier = Modifier.padding(GlobalCardStyles.CardPadding)
        ) {
            if (cardFullName != null) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = GlobalDimensions.CardPadding),
                ) {
                    Text(text = "Card full name: ", fontWeight = FontWeight.Bold)
                    Text(
                        text = cardFullName,
                        style = GlobalTypography.bodyLarge
                    )
                }
            }

            if (cardNumber != null) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = GlobalDimensions.CardPadding),
                ) {
                    Text(text = "Card number: ", fontWeight = FontWeight.Bold)

                    Text(
                        text = cardNumber,
                        style = GlobalTypography.bodyLarge
                    )
                }
            }

            if (expireMonth != null && expireYear != null) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = GlobalDimensions.CardPadding),
                ) {
                    Text(text = "Expire Date: ", fontWeight = FontWeight.Bold)

                    Text(
                        text = "$expireMonth/$expireYear",
                        style = GlobalTypography.bodyLarge
                    )
                }
            }

            if (cvv != null) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "CVV: ", fontWeight = FontWeight.Bold)

                    Text(
                        text = cvv,
                        style = GlobalTypography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                StyledButton(
                    text = "Edit profile",
                    modifier = editProfileButtonModifier,
                    textStyle = buttonTextWhiteStyle,
                    onClick = { onPress() },
                )
            }
        }
    }
}