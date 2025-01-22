package com.example.mangiaebasta.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.mangiaebasta.view.styles.MangiaEBastaTheme



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

// ciao sono giorgio
//io sono carlottaaa
//spero sia l'ultimo

@Composable
fun Display() {
    Column {
        Text("Hello, world!")
    }
}
