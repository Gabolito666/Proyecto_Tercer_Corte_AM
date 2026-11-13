package com.example.proyecto_tercer_corte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.uts.serviciosestudiantiles.data.ServiciosRepository
import com.uts.serviciosestudiantiles.ui.MainTabScreen
import com.uts.serviciosestudiantiles.ui.theme.UTSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ServiciosRepository.globalContext = applicationContext
        enableEdgeToEdge()
        setContent {
            UTSTheme {
                MainTabScreen()
            }
        }
    }
}
