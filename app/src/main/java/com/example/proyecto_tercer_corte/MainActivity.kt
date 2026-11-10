package com.example.proyecto_tercer_corte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.proyecto_tercer_corte.ui.theme.Proyecto_Tercer_CorteTheme
import com.uts.serviciosestudiantiles.ui.MainTabScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Proyecto_Tercer_CorteTheme {
                MainTabScreen()
            }
        }
    }
}
