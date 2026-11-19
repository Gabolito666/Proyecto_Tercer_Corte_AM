package com.uts.serviciosestudiantiles.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

// Esquema de formas redondeadas estilo Nubank (20.dp a 24.dp en tarjetas)
val UTSShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

private val UTSLightColorScheme = lightColorScheme(
    primary = VinoTintoOleo,
    onPrimary = BlancoSuperficie,
    primaryContainer = VinoTintoOscuro,
    onPrimaryContainer = BlancoSuperficie,
    secondary = MagentaOleo,
    onSecondary = BlancoSuperficie,
    secondaryContainer = MagentaOleoClaro,
    onSecondaryContainer = MagentaOleo,
    tertiary = VerdeOpalo,
    onTertiary = NegroTexto,
    tertiaryContainer = VerdeOpaloClaro,
    onTertiaryContainer = NegroTexto,
    background = CremaFondo,
    onBackground = NegroTexto,
    surface = BlancoSuperficie,
    onSurface = NegroTexto,
    surfaceVariant = BlancoSuperficie,
    onSurfaceVariant = GrisTextoSecundario,
    outline = GrisPlata,
    outlineVariant = GrisPlata
)

@Composable
fun UTSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = UTSLightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = UTSShapes,
        content = content
    )
}

@Composable
fun ServiciosEstudiantilesTheme(
    content: @Composable () -> Unit
) {
    UTSTheme(content = content)
}
