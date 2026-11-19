package com.example.proyecto_tercer_corte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.uts.serviciosestudiantiles.ui.MainDrawerScreen
import com.uts.serviciosestudiantiles.ui.theme.UTSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UTSTheme {
                MainDrawerScreen()
            }
        }
    }
}
