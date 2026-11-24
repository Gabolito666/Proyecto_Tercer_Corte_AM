package com.uts.serviciosestudiantiles.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal val UTSDarkColors = darkColorScheme(
    primary = Color(0xFFE4E4E4),
    onPrimary = Color(0xFF141414),
    primaryContainer = Color(0xFF242424),
    onPrimaryContainer = Color(0xFFF2F2F2),
    secondary = Color(0xFFB5B5B5),
    onSecondary = Color(0xFF1B1B1B),
    secondaryContainer = Color(0xFF1C1C1C),
    onSecondaryContainer = Color(0xFFF2F2F2),
    tertiary = Color(0xFFD0D0D0),
    onTertiary = Color(0xFF141414),
    tertiaryContainer = Color(0xFF272727),
    onTertiaryContainer = Color(0xFFF2F2F2),
    background = Color(0xFF0B0B0B),
    onBackground = Color(0xFFF2F2F2),
    surface = Color(0xFF171717),
    onSurface = Color(0xFFF2F2F2),
    surfaceVariant = Color(0xFF242424),
    onSurfaceVariant = Color(0xFFAAAAAA),
    surfaceTint = Color.Transparent,
    outline = Color(0xFF737373),
    outlineVariant = Color(0xFF303030),
    error = Color(0xFFDCA69F),
    onError = Color(0xFF301A18),
    errorContainer = Color(0xFF362725),
    onErrorContainer = Color(0xFFF1D5D0),
    surfaceDim = Color(0xFF0B0B0B),
    surfaceBright = Color(0xFF333333),
    surfaceContainerLowest = Color(0xFF080808),
    surfaceContainerLow = Color(0xFF141414),
    surfaceContainer = Color(0xFF1C1C1C),
    surfaceContainerHigh = Color(0xFF252525),
    surfaceContainerHighest = Color(0xFF303030),
    inverseSurface = Color(0xFFE7E7E7),
    inverseOnSurface = Color(0xFF202020),
    inversePrimary = Color(0xFF315540)
)

internal val UTSLightColors = lightColorScheme(
    primary = Color(0xFF315540),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEDF1ED),
    onPrimaryContainer = Color(0xFF315540),
    secondary = Color(0xFF565E58),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF1F3F1),
    onSecondaryContainer = Color(0xFF202421),
    tertiary = Color(0xFF315540),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFEDF1ED),
    onTertiaryContainer = Color(0xFF315540),
    background = Color.White,
    onBackground = Color(0xFF202421),
    surface = Color.White,
    onSurface = Color(0xFF202421),
    surfaceVariant = Color(0xFFF3F5F3),
    onSurfaceVariant = Color(0xFF606862),
    surfaceTint = Color.Transparent,
    outline = Color(0xFF7B847D),
    outlineVariant = Color(0xFFDFE4DF),
    error = Color(0xFF944B43),
    onError = Color.White,
    errorContainer = Color(0xFFF8EDEA),
    onErrorContainer = Color(0xFF73382F),
    surfaceDim = Color(0xFFE3E7E3),
    surfaceBright = Color.White,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFFAFBFA),
    surfaceContainer = Color(0xFFF5F7F5),
    surfaceContainerHigh = Color(0xFFEEF1EE),
    surfaceContainerHighest = Color(0xFFE7EBE7),
    inverseSurface = Color(0xFF292E2A),
    inverseOnSurface = Color(0xFFF1F3F1),
    inversePrimary = Color(0xFFB5C9BC)
)

// El acceso comparte la paleta y las barras del sistema con el resto de la app.
@Composable
fun AuthTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    UTSTheme(darkTheme = darkTheme, content = content)
}
