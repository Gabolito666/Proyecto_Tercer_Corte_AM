package com.example.proyecto_tercer_corte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.uts.serviciosestudiantiles.ui.AjustesDialog
import com.uts.serviciosestudiantiles.ui.MainDrawerScreen
import com.uts.serviciosestudiantiles.ui.theme.UTSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val preferencias = getSharedPreferences("apariencia", MODE_PRIVATE)
        setContent {
            var temaOscuro by rememberSaveable {
                mutableStateOf(preferencias.getBoolean("tema_oscuro", true))
            }
            var mostrarAjustes by rememberSaveable { mutableStateOf(false) }

            UTSTheme(darkTheme = temaOscuro) {
                MainDrawerScreen(onAbrirAjustes = { mostrarAjustes = true })
                if (mostrarAjustes) {
                    AjustesDialog(
                        temaOscuro = temaOscuro,
                        onTemaCambiado = { oscuro ->
                            temaOscuro = oscuro
                            preferencias.edit().putBoolean("tema_oscuro", oscuro).apply()
                        },
                        onCerrar = { mostrarAjustes = false }
                    )
                }
            }
        }
    }
}
