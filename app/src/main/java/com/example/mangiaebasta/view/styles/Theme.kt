package com.example.mangiaebasta.view.styles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Modifier

// Definizione dei colori principali
private val AppColors = lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color.White,
    background = Color(0xFFF6F6F6),
    surface = Color.White,
    onSurface = Color.Black
)

// Tipografia
val GlobalTypography = Typography(
    headlineLarge = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Black,
        color = Color(0xFF333333)
    ),
    titleLarge = TextStyle(
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF333333)
    ),
    titleMedium = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF333333)
    ),
    bodyLarge = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal,
        color = Color(0xFF333333)
    ),
    bodyMedium = TextStyle(
        fontSize = 18.sp,
        color = Color(0xFF000000)
    ),
    labelLarge = TextStyle(
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF00A86B)
    )
)

// Shapes personalizzate
val GlobalShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)


// Spaziature e padding
object GlobalDimensions {
    val DefaultPadding = 16.dp
    val CardPadding = 12.dp
    val ButtonPadding = 14.dp
}

// Stile per i bottoni
@Composable
fun DetailAndBackButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6C8BEA), // Colore di sfondo
            contentColor = Color.Black // Colore del testo Nero
        ),
        modifier = Modifier.padding(8.dp)
    ) {
        content()
    }
}

@Composable
fun BuyButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF5722), // Colore di sfondo
            contentColor = Color.White // Colore del testo Bianco
        ),
        modifier = Modifier.padding(8.dp)
    ) {
        content()
    }
}


// Card
object GlobalCardStyles {
    val CardShape = RoundedCornerShape(16.dp)
    val CardPadding = PaddingValues(16.dp)
    val CardElevation = 4.dp
}


// Definizione del tema globale
@Composable
fun MangiaEBastaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColors,
        typography = GlobalTypography,
        shapes = GlobalShapes,
        content = content
    )
}