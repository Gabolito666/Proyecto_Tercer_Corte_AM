package com.uts.serviciosestudiantiles.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uts.serviciosestudiantiles.ui.theme.BlancoSuperficie
import com.uts.serviciosestudiantiles.ui.theme.CremaFondo
import com.uts.serviciosestudiantiles.ui.theme.GrisPlata
import com.uts.serviciosestudiantiles.ui.theme.GrisTextoSecundario
import com.uts.serviciosestudiantiles.ui.theme.NegroTexto
import com.uts.serviciosestudiantiles.ui.theme.VerdeOpalo
import com.uts.serviciosestudiantiles.ui.theme.VerdeOpaloClaro
import com.uts.serviciosestudiantiles.ui.theme.VinoTintoOleo
import com.uts.serviciosestudiantiles.viewmodel.ServiciosViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PerfilScreen(
    viewModel: ServiciosViewModel,
    onNavegarAdmin: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO")).apply {
        maximumFractionDigits = 0
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CremaFondo)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Encabezado institucional estilo Nubank
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Módulo 1",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = VinoTintoOleo,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Mi Perfil Institucional",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = NegroTexto
                )
                Text(
                    text = if (state.esAdmin)
                        "Credenciales oficiales de Administrador General del Sistema UTS."
                    else
                        "Información académica y credenciales del estudiante activo.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTextoSecundario
                )
            }

            // Mensajes de estado / notificaciones
            AnimatedVisibility(
                visible = state.mensajeMensajeEstado != null,
                enter = fadeIn() + slideInVertically()
            ) {
                state.mensajeMensajeEstado?.let { mensaje ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
                        border = BorderStroke(
                            width = 1.5.dp,
                            color = if (state.esError) VinoTintoOleo else if (state.esUsuarioExistente) VerdeOpalo else VinoTintoOleo
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (state.esError) VinoTintoOleo.copy(alpha = 0.12f) else if (state.esUsuarioExistente) VerdeOpaloClaro else CremaFondo,
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (state.esError) Icons.Default.Error else if (state.esUsuarioExistente) Icons.Default.Verified else Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (state.esError) VinoTintoOleo else if (state.esUsuarioExistente) VinoTintoOleo else VerdeOpalo,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (state.esError) "Notificación" else if (state.esAdmin) "Administración UTS" else "Sesión Activa",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (state.esError) VinoTintoOleo else if (state.esUsuarioExistente) VinoTintoOleo else NegroTexto
                                )
                                Text(
                                    text = mensaje,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NegroTexto
                                )
                            }
                        }
                    }
                }
            }

            // --- TARJETA DE PERFIL (CORRECCIÓN VISUAL EXACTA: SIN SCROLL INTERNO, PADDING 16.DP, ELLIPSIS) ---
            if (state.perfilGuardado != null) {
                val perfil = state.perfilGuardado!!

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
                    border = BorderStroke(1.5.dp, if (state.esAdmin) VinoTintoOleo else VerdeOpalo),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (state.esAdmin) VinoTintoOleo else VerdeOpaloClaro,
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = if (state.esAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                                            contentDescription = null,
                                            tint = if (state.esAdmin) VerdeOpalo else VinoTintoOleo,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = perfil.nombre,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = NegroTexto,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = if (state.esAdmin) "Rol: Administrador General" else "Código: ${perfil.codigo}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GrisTextoSecundario,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = if (state.esAdmin) VinoTintoOleo else VerdeOpaloClaro,
                                border = BorderStroke(1.dp, if (state.esAdmin) VinoTintoOleo else VerdeOpalo)
                            ) {
                                Text(
                                    text = if (state.esAdmin) "ADMIN" else "ESTUDIANTE",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (state.esAdmin) BlancoSuperficie else NegroTexto,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        HorizontalDivider(color = GrisPlata)

                        // Información institucional detallada
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = null,
                                    tint = VinoTintoOleo,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Facultad: ${perfil.facultad}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = NegroTexto,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    tint = VinoTintoOleo,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = perfil.correoInstitucional,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = VinoTintoOleo,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Badge,
                                    contentDescription = null,
                                    tint = VerdeOpalo,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Estado: Matriculado y Activo en Plataforma",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NegroTexto
                                )
                            }
                        }

                        // Acceso rápido a Panel Admin si corresponde
                        if (state.esAdmin) {
                            Button(
                                onClick = onNavegarAdmin,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(containerColor = VinoTintoOleo)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = null,
                                    tint = VerdeOpalo
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Abrir Panel de Administración", fontWeight = FontWeight.Bold)
                            }
                        }

                        // Botón de Cerrar Sesión
                        OutlinedButton(
                            onClick = { viewModel.cerrarSesion() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = VinoTintoOleo),
                            border = BorderStroke(1.5.dp, VinoTintoOleo)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cerrar Sesión / Cambiar Cuenta", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Resumen de estado de servicios si es estudiante
                if (!state.esAdmin) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
                        border = BorderStroke(1.dp, GrisPlata),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Receipt,
                                    contentDescription = null,
                                    tint = VinoTintoOleo,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Resumen de Matrícula",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = NegroTexto
                                )
                            }
                            HorizontalDivider(color = GrisPlata)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Liquidaciones realizadas:", color = GrisTextoSecundario)
                                Text(
                                    "${state.listaLiquidaciones.size}",
                                    fontWeight = FontWeight.Bold,
                                    color = NegroTexto
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total acumulado en cursos:", color = GrisTextoSecundario)
                                Text(
                                    currencyFormat.format(state.totalAcumulado),
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VinoTintoOleo
                                )
                            }
                        }
                    }
                }
            } else {
                // Si no hay sesión activa en este composable
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
                    border = BorderStroke(1.dp, GrisPlata)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = CremaFondo,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = VinoTintoOleo,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Text(
                            text = "No hay sesión iniciada",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = NegroTexto
                        )
                        Text(
                            text = "Por favor inicia sesión o regístrate para acceder a los servicios de las UTS.",
                            style = MaterialTheme.typography.bodySmall,
                            color = GrisTextoSecundario
                        )
                        Button(
                            onClick = { viewModel.cerrarSesion() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = VinoTintoOleo)
                        ) {
                            Text("Ir a Pantalla de Autenticación", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
