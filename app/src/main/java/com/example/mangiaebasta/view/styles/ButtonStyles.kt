package com.example.mangiaebasta.view.styles

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun baseButtonModifier(
    backgroundColor: Color,
    borderColor: Color = Color.Transparent,
    paddingVertical: Int = 8,
    paddingHorizontal: Int = 10,
    borderRadius: Int = 15
) = Modifier
    .background(color = backgroundColor, shape = RoundedCornerShape(borderRadius.dp))
    .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(borderRadius.dp))
    .padding(vertical = paddingVertical.dp, horizontal = paddingHorizontal.dp)

val goBackButtonModifier = baseButtonModifier(
    backgroundColor = Color.White,
    borderColor = Color.Black,
    paddingHorizontal = 10
).width(100.dp)

val editProfileButtonModifier = baseButtonModifier(
    backgroundColor = Color.Gray,
    paddingHorizontal = 10
).width(150.dp)

val signUpButtonModifier = baseButtonModifier(
    backgroundColor = Color(0xFF2196F3), // Blu
    paddingVertical = 14,
).width(300.dp)

val buyButtonModifier = baseButtonModifier(
    backgroundColor = Color(0xFFFF5722), // Arancione
    paddingVertical = 14,
).width(300.dp)

val detailButtonModifier = baseButtonModifier(
    backgroundColor = Color(0xFFFFC107), // Giallo
    paddingVertical = 10,
    paddingHorizontal = 10
).width(200.dp)

val orderButtonModifier = baseButtonModifier(
    backgroundColor = Color(0xFFFF5733), // Rosso scuro
    paddingVertical = 14
).width(300.dp)

// Stili dei testi
val buttonTextWhiteStyle = TextStyle(
    color = Color.White,
    fontSize = 18.sp,
    fontWeight = FontWeight.Bold
)

val buttonTextBlackStyle = TextStyle(
    color = Color.Black,
    fontSize = 18.sp,
    fontWeight = FontWeight.Bold
)
