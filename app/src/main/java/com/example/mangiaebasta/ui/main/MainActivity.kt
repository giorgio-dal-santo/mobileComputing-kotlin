package com.example.mangiaebasta.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MangiaEBastaTheme {
                Display()
            }
        }
    }
}

@Composable
fun Display() {
    Column {
        Text("Hello, world!")
    }
}
