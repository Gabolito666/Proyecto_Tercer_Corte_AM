package com.uts.serviciosestudiantiles.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

// Acento reservado a las acciones principales en ambos modos.
val FondoAccionPrincipal = Color(0xFF315540)
val TextoAccionPrincipal = Color.White

// Alias de compatibilidad para las pantallas existentes. Los nombres históricos
// ahora resuelven roles del tema compartido, no colores claros fijos.
val VinoTintoOleo: Color
    @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.primary
val VinoTintoOscuro: Color
    @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onPrimary
val VerdeOpalo: Color
    @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.tertiary
val CremaFondo: Color
    @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.background
val BlancoSuperficie: Color
    @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.surface
val NegroTexto: Color
    @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onSurface
val MagentaOleo: Color
    @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.error
val GrisPlata: Color
    @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.outlineVariant
val GrisTextoSecundario: Color
    @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onSurfaceVariant
val GrisPlataMate: Color
    @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.surfaceVariant
val VerdeOpaloClaro: Color
    @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.tertiaryContainer
val MagentaOleoClaro: Color
    @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.errorContainer
