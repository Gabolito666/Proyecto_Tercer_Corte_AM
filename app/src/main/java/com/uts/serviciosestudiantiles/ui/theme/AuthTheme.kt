package com.uts.serviciosestudiantiles.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal val UTSDarkColors = darkColorScheme(
    primary = Color(0xFF8CF03F),
    onPrimary = Color(0xFF14251B),
    primaryContainer = Color(0xFF008A45),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF8CF03F),
    onSecondary = Color(0xFF14251B),
    secondaryContainer = Color(0xFF203C2B),
    onSecondaryContainer = Color.White,
    tertiary = Color(0xFF8CF03F),
    onTertiary = Color(0xFF14251B),
    tertiaryContainer = Color(0xFF203C2B),
    onTertiaryContainer = Color.White,
    background = Color(0xFF0D1512),
    onBackground = Color.White,
    surface = Color(0xFF19251F),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF203029),
    onSurfaceVariant = Color(0xFFA8B8AE),
    outline = Color(0xFF617268),
    outlineVariant = Color(0xFF35493D),
    error = Color(0xFFFFB4AB)
)

internal val UTSLightColors = lightColorScheme(
    primary = Color(0xFF008A45),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF8CF03F),
    onPrimaryContainer = Color(0xFF14251B),
    secondary = Color(0xFF008A45),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE2F4E5),
    onSecondaryContainer = Color(0xFF14251B),
    tertiary = Color(0xFF008A45),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE2F4E5),
    onTertiaryContainer = Color(0xFF14251B),
    background = Color(0xFFF6FAF7),
    onBackground = Color(0xFF14251B),
    surface = Color.White,
    onSurface = Color(0xFF14251B),
    surfaceVariant = Color(0xFFEDF4EF),
    onSurfaceVariant = Color(0xFF617268),
    outline = Color(0xFF617268),
    outlineVariant = Color(0xFFCDDCD1)
)

// El acceso comparte la paleta y las barras del sistema con el resto de la app.
@Composable
fun AuthTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    UTSTheme(darkTheme = darkTheme, content = content)
}
