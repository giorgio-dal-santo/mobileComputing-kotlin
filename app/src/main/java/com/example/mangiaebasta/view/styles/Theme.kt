package com.example.mangiaebasta.view.styles

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mangiaebasta.R

val PoppinsBlack = FontFamily(Font(R.font.poppins_black))
val PoppinsBold = FontFamily(Font(R.font.poppins_bold))
val PoppinsRegular = FontFamily(Font(R.font.poppins_regular))

@Composable
fun MangiaEBastaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColors,
        typography = GlobalTypography,
        shapes = GlobalShapes,
        content = content
    )
}

private val AppColors = lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color.White,
    background = Color(0xFFF6F6F6),
    surface = Color.White,
    onSurface = Color.Black
)

val GlobalTypography = Typography(
    headlineLarge = TextStyle(
        fontSize = 32.sp,
        fontFamily = PoppinsBlack,
        color = Color(0xFF333333),
        textAlign = TextAlign.Center
    ),
    titleLarge = TextStyle(
        fontSize = 30.sp,
        fontFamily = PoppinsBold,
        color = Color(0xFF333333),
        textAlign = TextAlign.Left
    ),
    titleMedium = TextStyle(
        fontSize = 24.sp,
        fontFamily = PoppinsBold,
        color = Color(0xFF333333),
        textAlign = TextAlign.Left
    ),
    titleSmall = TextStyle(
        fontSize = 20.sp,
        fontFamily = PoppinsRegular,
        fontWeight = FontWeight.Light,
        color = Color(0xFF333333),
        textAlign = TextAlign.Left
    ),
    bodyLarge = TextStyle(
        fontSize = 18.sp,
        color = Color.Black
    ),
    bodyMedium = TextStyle(
        fontSize = 18.sp,
        color = Color(0xFF888888)
    ),
    labelLarge = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF00A86B)
    ),
    bodySmall = TextStyle(
        fontSize = 16.sp,
        color = Color(0xFF555555)
    )
)

val GlobalShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

object GlobalDimensions {
    val DefaultPadding = 16.dp
    val CardPadding = 12.dp
    val ButtonPadding = 14.dp
}

object GlobalCardStyles {
    val CardShape = RoundedCornerShape(16.dp)
    val CardPadding = PaddingValues(16.dp)
    val CardElevation = 4.dp
    val CardColors = CardColors(
        contentColor = Color.Black,
        containerColor = Color.White,
        disabledContainerColor = Color.White,
        disabledContentColor = Color.Black,
    )
}