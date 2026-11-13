package com.uts.serviciosestudiantiles.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uts.serviciosestudiantiles.data.LiquidacionMatricula
import com.uts.serviciosestudiantiles.ui.theme.BlancoSuperficie
import com.uts.serviciosestudiantiles.ui.theme.CremaFondo
import com.uts.serviciosestudiantiles.ui.theme.GrisPlata
import com.uts.serviciosestudiantiles.ui.theme.GrisTextoSecundario
import com.uts.serviciosestudiantiles.ui.theme.MagentaOleo
import com.uts.serviciosestudiantiles.ui.theme.NegroTexto
import com.uts.serviciosestudiantiles.ui.theme.VerdeOpalo
import com.uts.serviciosestudiantiles.ui.theme.VerdeOpaloClaro
import com.uts.serviciosestudiantiles.ui.theme.VinoTintoOleo
import com.uts.serviciosestudiantiles.ui.theme.VinoTintoOscuro
import com.uts.serviciosestudiantiles.viewmodel.ServiciosViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HistorialScreen(
    viewModel: ServiciosViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // Garantizar que al abrir la pestaña o cambiar de perfil se actualice leyendo la persistencia local
    androidx.compose.runtime.LaunchedEffect(state.perfilGuardado?.codigo) {
        viewModel.actualizarHistorialPorEstudiante()
    }

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
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Encabezado de sección
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "Módulo 3",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = VinoTintoOleo,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Historial Financiero",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = NegroTexto
                )
            }

            // Tarjeta superior prominente estilo Nubank en VinoTintoOleo con acentos en VerdeOpalo
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = VinoTintoOleo),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Fila superior: Ícono de billetera y badges flotantes alineados a la derecha
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = BlancoSuperficie.copy(alpha = 0.15f),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = VerdeOpalo,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = BlancoSuperficie.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "UTS Pay",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = VerdeOpalo,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = VerdeOpalo
                            ) {
                                Text(
                                    text = "${state.listaLiquidaciones.size} reg.",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = VinoTintoOscuro,
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    // Título reorganizado en una Column con espacio horizontal completo para evitar colisiones
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "Total Acumulado Financiero",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BlancoSuperficie
                        )
                        Text(
                            text = if (state.esAdmin) {
                                "Modo Administrador: Consolidado de todos los estudiantes"
                            } else {
                                state.perfilGuardado?.let {
                                    "Estudiante: ${it.nombre} (Código: ${it.codigo})"
                                } ?: "Sin sesión activa"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = BlancoSuperficie.copy(alpha = 0.8f)
                        )
                    }

                    // Acento en VerdeOpalo para el total acumulado
                    Text(
                        text = currencyFormat.format(state.totalAcumulado),
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 36.sp),
                        fontWeight = FontWeight.ExtraBold,
                        color = VerdeOpalo
                    )

                    Text(
                        text = if (state.esAdmin) "Total institucional consolidado en el sistema" else "Liquidaciones registradas para el estudiante actual",
                        style = MaterialTheme.typography.bodySmall,
                        color = BlancoSuperficie.copy(alpha = 0.75f)
                    )
                }
            }

            // Título de la sección de liquidaciones
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Movimientos Registrados",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NegroTexto
                )
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = GrisTextoSecundario,
                    modifier = Modifier.size(20.dp)
                )
            }

            // LazyColumn con tarjetas individuales estilo Nubank
            if (state.listaLiquidaciones.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
                    border = BorderStroke(1.dp, GrisPlata),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = CremaFondo,
                                modifier = Modifier.size(64.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                                        contentDescription = null,
                                        modifier = Modifier.size(32.dp),
                                        tint = GrisTextoSecundario
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Sin liquidaciones registradas",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = NegroTexto
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = state.perfilGuardado?.let {
                                    "No hay registros financieros para ${it.nombre} (Código: ${it.codigo}). Liquida un curso libre en la pestaña '2. Matrícula' para verlo aquí."
                                } ?: "Guarda tu perfil y liquida cursos libres en la pestaña '2. Matrícula' para visualizar los comprobantes aquí.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = GrisTextoSecundario,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(
                        items = state.listaLiquidaciones,
                        key = { it.id }
                    ) { liquidacion ->
                        LiquidacionItemCard(
                            liquidacion = liquidacion,
                            currencyFormat = currencyFormat
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LiquidacionItemCard(
    liquidacion: LiquidacionMatricula,
    currencyFormat: NumberFormat,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
        border = BorderStroke(1.dp, GrisPlata),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = liquidacion.cursoLibre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NegroTexto,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    color = CremaFondo,
                    border = BorderStroke(1.dp, GrisPlata)
                ) {
                    Text(
                        text = "ID #${liquidacion.id}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = GrisTextoSecundario,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            // Fila con tarifa base y etiqueta de trazabilidad con el Código del Estudiante
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tarifa base: ${currencyFormat.format(liquidacion.costoBase)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTextoSecundario
                )

                // Etiqueta visible con el Código del Estudiante para trazabilidad completa
                Surface(
                    shape = RoundedCornerShape(50),
                    color = CremaFondo,
                    border = BorderStroke(1.dp, VinoTintoOleo.copy(alpha = 0.35f))
                ) {
                    Text(
                        text = "Código: ${liquidacion.codigoEstudiante}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = VinoTintoOleo,
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 3.dp)
                    )
                }
            }

            // Etiquetas tipo píldora (VerdeOpalo para Beca, MagentaOleo para Icetex)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Etiqueta píldora para Beca
                if (liquidacion.tieneBeca) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = VerdeOpaloClaro,
                        border = BorderStroke(1.dp, VerdeOpalo)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = NegroTexto,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Beca: 50% desc.",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = NegroTexto
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = CremaFondo,
                        border = BorderStroke(1.dp, GrisPlata)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = GrisTextoSecundario,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Sin Beca",
                                style = MaterialTheme.typography.labelSmall,
                                color = GrisTextoSecundario
                            )
                        }
                    }
                }

                // Etiqueta píldora para Icetex
                if (liquidacion.tieneIcetex) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MagentaOleo,
                        border = BorderStroke(1.dp, MagentaOleo)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = BlancoSuperficie,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Icetex: +5%",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = BlancoSuperficie
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = CremaFondo,
                        border = BorderStroke(1.dp, GrisPlata)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = GrisTextoSecundario,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Sin Icetex",
                                style = MaterialTheme.typography.labelSmall,
                                color = GrisTextoSecundario
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = GrisPlata, thickness = 1.dp)

            // Costo en tipografía destacada
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Liquidado:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = NegroTexto
                )
                Text(
                    text = currencyFormat.format(liquidacion.totalAPagar),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = VinoTintoOleo
                )
            }
        }
    }
}
