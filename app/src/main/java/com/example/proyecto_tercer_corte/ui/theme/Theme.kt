package com.example.proyecto_tercer_corte.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.uts.serviciosestudiantiles.ui.theme.UTSTheme

@Composable
fun Proyecto_Tercer_CorteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    UTSTheme(
        darkTheme = darkTheme,
        content = content
    )
}
