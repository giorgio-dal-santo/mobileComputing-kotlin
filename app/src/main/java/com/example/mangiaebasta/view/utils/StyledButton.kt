package com.example.mangiaebasta.view.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun StyledButton(
    text: String,
    modifier: Modifier,
    textStyle: TextStyle,
    onClick: () -> Unit // Qui accetti una funzione lambda
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() } // Esegui la lambda quando cliccato
    ) {
        BasicText(
            text = text,
            style = textStyle
        )
    }
}

