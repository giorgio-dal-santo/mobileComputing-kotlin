package com.example.mangiaebasta.view.utils.accountForm

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mangiaebasta.view.styles.GlobalTypography

@Composable
fun FormField(
    value: String,
    label: String,
    onValueChange: (String) -> Boolean,
    errorMessage: String,
    showError: Boolean,
    keyBoardOptions: KeyboardOptions,
) {

    var isValid by remember { mutableStateOf(onValueChange(value)) }

    Text(
        text = label,
        fontWeight = FontWeight.Bold,
        style = GlobalTypography.titleSmall,
        modifier = Modifier.padding(top = 3.dp)
    )

    BasicTextField(
        value = value,
        onValueChange = {
            isValid = onValueChange(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp) // Altezza leggermente aumentata
            .padding(bottom = 12.dp),
        keyboardOptions = keyBoardOptions,
        textStyle = TextStyle(
            fontSize = 20.sp, // Testo più grande
            fontWeight = FontWeight.Medium, // Testo leggermente più marcato
            textAlign = TextAlign.Start,
            color = Color.Black
        ),
        singleLine = true,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)) // Bordo arrotondato
                    .padding(8.dp), // Padding interno per il testo
                contentAlignment = Alignment.CenterStart
            ) {
                innerTextField()
            }
        }
    )


    if (!isValid && showError) {
        Text(
            text = errorMessage,
            color = Color.Red,
            modifier = Modifier.padding(bottom = 6.dp)
        )
    }

}