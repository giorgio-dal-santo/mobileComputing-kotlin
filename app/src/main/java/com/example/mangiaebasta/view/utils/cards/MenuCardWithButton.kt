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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.mangiaebasta.model.dataClasses.MenuDetailsWithImage
import com.example.mangiaebasta.model.dataClasses.MenuWithImage
import com.example.mangiaebasta.view.styles.GlobalCardStyles
import com.example.mangiaebasta.view.styles.GlobalDimensions
import com.example.mangiaebasta.view.styles.GlobalShapes
import com.example.mangiaebasta.view.styles.GlobalTypography
import com.example.mangiaebasta.view.styles.buttonTextBlackStyle
import com.example.mangiaebasta.view.styles.buttonTextWhiteStyle
import com.example.mangiaebasta.view.styles.buyButtonModifier
import com.example.mangiaebasta.view.styles.detailButtonModifier
import com.example.mangiaebasta.view.styles.goBackButtonModifier
import com.example.mangiaebasta.view.utils.button.StyledButton
import com.example.mangiaebasta.viewmodel.MainViewModel
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

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

@Composable
fun MenuCardWithButtonDetailed(
    menuDetails: MenuDetailsWithImage,
    onPress: () -> Unit,
    onBack: () -> Unit,
    viewModel: MainViewModel
) {
    MenuCardBodyDetailed(
        title = menuDetails.menuDetails.name,
        description = menuDetails.menuDetails.longDescription,
        price = menuDetails.menuDetails.price,
        deliveryTime = menuDetails.menuDetails.deliveryTime,
        image = menuDetails.image.base64,
        onPress = onPress,
        onBack = onBack,
        viewModel = viewModel
    )
}

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
        colors = GlobalCardStyles.CardColors
    ) {
        Column(
            modifier = Modifier.padding(GlobalCardStyles.CardPadding)
        ) {
            if (image != null) {
                val byteArray = Base64.decode(image)
                val bitmap =
                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(GlobalShapes.small),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(text = "No image")
            }

            Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))

            Text(
                text = title,
                style = GlobalTypography.titleMedium
            )

            Text(
                text = description,
                style = GlobalTypography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))

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

            Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StyledButton(
                    text = "Details",
                    modifier = detailButtonModifier,
                    textStyle = buttonTextBlackStyle,
                    onClick = { onPress() },
                )
            }
        }
    }
}

@OptIn(ExperimentalEncodingApi::class)
@Composable
fun MenuCardBodyDetailed(
    title: String,
    description: String,
    price: Float,
    deliveryTime: Int,
    image: String?,
    onPress: () -> Unit,
    onBack: () -> Unit,
    viewModel: MainViewModel
) {
    val userState by viewModel.userState.collectAsState()

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
            if (image != null) {
                val byteArray = Base64.decode(image)
                val bitmap =
                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(GlobalShapes.small),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(text = "No image")
            }

            Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))

            Text(
                text = title,
                style = GlobalTypography.titleLarge
            )

            Text(
                text = description,
                style = GlobalTypography.bodyLarge,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))

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

            Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))

            if (userState.isUserRegistered) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StyledButton(
                        text = "Buy",
                        modifier = buyButtonModifier,
                        textStyle = buttonTextWhiteStyle,
                        onClick = { onPress() },
                    )
                }
            }

            Spacer(modifier = Modifier.height(GlobalDimensions.DefaultPadding))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StyledButton(
                    text = "Back",
                    modifier = goBackButtonModifier,
                    textStyle = buttonTextBlackStyle,
                    onClick = { onBack() },
                )
            }
        }
    }
}