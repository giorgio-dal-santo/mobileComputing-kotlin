package com.example.mangiaebasta.view.utils.cards

import android.graphics.BitmapFactory
import android.provider.ContactsContract.Profile
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mangiaebasta.model.dataClasses.MenuDetailsWithImage
import com.example.mangiaebasta.model.dataClasses.MenuWithImage
import com.example.mangiaebasta.model.dataClasses.User
import com.example.mangiaebasta.model.dataClasses.UserDetail
import com.example.mangiaebasta.view.styles.GlobalCardStyles
import com.example.mangiaebasta.view.styles.GlobalDimensions
import com.example.mangiaebasta.view.styles.GlobalTypography
import com.example.mangiaebasta.view.styles.buttonTextBlackStyle
import com.example.mangiaebasta.view.styles.buttonTextWhiteStyle
import com.example.mangiaebasta.view.styles.buyButtonModifier
import com.example.mangiaebasta.view.styles.detailButtonModifier
import com.example.mangiaebasta.view.styles.goBackButtonModifier
import com.example.mangiaebasta.view.styles.signUpButtonModifier
import com.example.mangiaebasta.view.utils.button.StyledButton
import com.example.mangiaebasta.viewmodel.MainViewModel
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

//PREVIEW MENU NON DETTAGLIATO
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


//BODY CON BOTTONE DETAIL
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

            Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))

            if (cardFullName != null) {
                Row {
                    Text(text = "Card full name: ", fontWeight = FontWeight.Bold)
                    Text(
                        text = cardFullName,
                        style = GlobalTypography.bodyLarge
                    )
                }
            }

            if (cardNumber != null) {
                Row {
                    Text(text = "Card Number: ", fontWeight = FontWeight.Bold)

                    Text(
                        text = cardNumber,
                        style = GlobalTypography.bodyLarge
                    )
                }
            }

            if (expireMonth != null && expireYear != null) {
                Row {
                    Text(text = "Card Expire: ", fontWeight = FontWeight.Bold)

                    Text(
                        text = "$expireMonth/$expireYear",
                        style = GlobalTypography.bodyLarge
                    )
                }
            }

            if (cvv != null) {
                Row {
                    Text(text = "CVV: ", fontWeight = FontWeight.Bold)

                    Text(
                        text = cvv,
                        style = GlobalTypography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))

            // Bottone edit
            StyledButton(
                text = "Edit Profile",
                modifier = signUpButtonModifier,
                textStyle = buttonTextWhiteStyle,
                onClick = { onPress() },
            )

            Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))
        }
    }
}

