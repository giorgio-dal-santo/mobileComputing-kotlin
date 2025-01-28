package com.example.mangiaebasta.view.utils.cards

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.mangiaebasta.model.dataClasses.MenuDetailsWithImage
import com.example.mangiaebasta.model.dataClasses.MenuWithImage
import com.example.mangiaebasta.view.styles.GlobalCardStyles
import com.example.mangiaebasta.view.styles.GlobalDimensions
import com.example.mangiaebasta.view.styles.GlobalTypography
import com.example.mangiaebasta.view.styles.buttonTextBlackStyle
import com.example.mangiaebasta.view.styles.buttonTextWhiteStyle
import com.example.mangiaebasta.view.styles.buyButtonModifier
import com.example.mangiaebasta.view.styles.detailButtonModifier
import com.example.mangiaebasta.view.styles.goBackButtonModifier
import com.example.mangiaebasta.view.utils.button.StyledButton
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

//PREVIEW MENU NON DETTAGLIATO
@Composable
fun MenuCardWithButton(
    menu: MenuWithImage,
    onPress: () -> Unit
) {
    MenuCardBody(
        title = menu.menu.name,
        description = menu.menu.shortDescription,
        price = menu.menu.price,
        deliveryTime = menu.menu.deliveryTime,
        image = menu.image?.base64,
        onPress = onPress
    )
}

//PREVIEW MENU DETTAGLIATO
@Composable
fun MenuCardWithButtonDetailed(
    menuDetails: MenuDetailsWithImage,
    onPress: () -> Unit,
    onBack: () -> Unit
) {
    MenuCardBodyDetailed(
        title = menuDetails.menuDetails.name,
        description = menuDetails.menuDetails.longDescription,
        price = menuDetails.menuDetails.price,
        deliveryTime = menuDetails.menuDetails.deliveryTime,
        image = menuDetails.image.base64,
        onPress = onPress,
        onBack = onBack
    )
}

//BODY CON BOTTONE DETAIL
@OptIn(ExperimentalEncodingApi::class)
@Composable
fun MenuCardBody(
    title: String,
    description: String,
    price: Float,
    deliveryTime: Int,
    image: String?,
    onPress: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(GlobalCardStyles.CardPadding)
            .fillMaxWidth(),
        shape = GlobalCardStyles.CardShape,
        elevation = CardDefaults.cardElevation(GlobalCardStyles.CardElevation),
        onClick = onPress
    ) {
        Column(
            modifier = Modifier.padding(GlobalCardStyles.CardPadding)
        ) {
            // Immagine del menu
            if (image != null) {
                val byteArray = Base64.decode(image)
                val bitmap =
                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = title,
                    modifier = Modifier.size(120.dp, 120.dp),
                    contentScale = ContentScale.Crop
                )
            } else {

                Text(text = "No image")

            }


            Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))

            // Titolo del menu
            Text(
                text = title,
                style = GlobalTypography.titleLarge
            )

            // Descrizione breve
            Text(
                text = description,
                style = GlobalTypography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))

            // Prezzo e tempo di consegna
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "€ ${"%.2f".format(price)}",
                    style = GlobalTypography.labelLarge
                )
                Text(
                    text = "$deliveryTime min",
                    style = GlobalTypography.bodyMedium
                )
            }

            // Bottone dei dettagli
            StyledButton(
                text = "Details",
                modifier = detailButtonModifier,
                textStyle = buttonTextWhiteStyle,
                onClick = { onPress() },
            )

            Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))


        }
    }
}


//BODY CON BOTTONE BUY E BACK
@OptIn(ExperimentalEncodingApi::class)
@Composable
fun MenuCardBodyDetailed(
    title: String,
    description: String,
    price: Float,
    deliveryTime: Int,
    image: String?,
    onPress: () -> Unit,
    onBack: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(GlobalCardStyles.CardPadding)
            .fillMaxWidth(),
        shape = GlobalCardStyles.CardShape,
        elevation = CardDefaults.cardElevation(GlobalCardStyles.CardElevation),
        onClick = onPress
    ) {
        Column(
            modifier = Modifier.padding(GlobalCardStyles.CardPadding)
        ) {
            // Immagine del menu
            if (image != null) {
                val byteArray = Base64.decode(image)
                val bitmap =
                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = title,
                    modifier = Modifier.size(120.dp, 120.dp),
                    contentScale = ContentScale.Crop
                )
            } else {

                Text(text = "No image")

            }


            Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))

            // Titolo del menu
            Text(
                text = title,
                style = GlobalTypography.titleLarge
            )

            // Descrizione breve
            Text(
                text = description,
                style = GlobalTypography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))

            // Prezzo e tempo di consegna
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "€ ${"%.2f".format(price)}",
                    style = GlobalTypography.labelLarge
                )
                Text(
                    text = "$deliveryTime min",
                    style = GlobalTypography.bodyMedium
                )
            }


            // Bottone acquista
            StyledButton(
                text = "Buy",
                modifier = buyButtonModifier,
                textStyle = buttonTextWhiteStyle,
                onClick = { onPress() },
            )
            Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))
            //Bottone per back
            StyledButton(
                text = "Back",
                modifier = goBackButtonModifier,
                textStyle = buttonTextBlackStyle,
                onClick = { onBack() },
            )


            Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))


        }
    }
}
