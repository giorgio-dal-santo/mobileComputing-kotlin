package com.example.mangiaebasta.view.utils.cards

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.mangiaebasta.model.dataClasses.MenuDetailsWithImage
import com.example.mangiaebasta.view.styles.GlobalCardStyles
import com.example.mangiaebasta.view.styles.GlobalDimensions
import com.example.mangiaebasta.view.styles.GlobalTypography
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

//PREVIEW MENU SENZA BOTTONI E SEMPLICE
@Composable
fun MenuCard(
    menu: MenuDetailsWithImage,
) {
    MenuCardBodySimple(
        title = menu.menuDetails.name,
        description = menu.menuDetails.shortDescription,
        image = menu.image.base64,
    )
}

//BODY CON BOTTONE DETAIL
@OptIn(ExperimentalEncodingApi::class)
@Composable
fun MenuCardBodySimple(
    title: String,
    description: String,
    image: String?,
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
            // Immagine del menu
            if (image != null) {
                val byteArray = Base64.decode(image)
                val bitmap =
                    BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()  // Adatta l'immagine alla larghezza della Card
                        .height(120.dp), // Altezza fissa per mantenere un aspetto rettangolare
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
        }
    }
}