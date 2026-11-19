package com.uts.serviciosestudiantiles.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uts.serviciosestudiantiles.ui.theme.BlancoSuperficie
import com.uts.serviciosestudiantiles.ui.theme.CremaFondo
import com.uts.serviciosestudiantiles.ui.theme.GrisPlata
import com.uts.serviciosestudiantiles.ui.theme.GrisPlataMate
import com.uts.serviciosestudiantiles.ui.theme.GrisTextoSecundario
import com.uts.serviciosestudiantiles.ui.theme.MagentaOleo
import com.uts.serviciosestudiantiles.ui.theme.MagentaOleoClaro
import com.uts.serviciosestudiantiles.ui.theme.NegroTexto
import com.uts.serviciosestudiantiles.ui.theme.VerdeOpalo
import com.uts.serviciosestudiantiles.ui.theme.VerdeOpaloClaro
import com.uts.serviciosestudiantiles.ui.theme.VinoTintoOleo
import com.uts.serviciosestudiantiles.viewmodel.ServiciosViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MatriculaScreen(
    viewModel: ServiciosViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO")).apply {
        maximumFractionDigits = 0
    }

    val becaPorcentajeInt = (state.porcentajeBeca * 100).toInt()
    val icetexPorcentajeInt = (state.porcentajeIcetex * 100).toInt()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CremaFondo)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Encabezado de sección estilo Nubank
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Módulo 2",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = VinoTintoOleo,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Liquidación de Matrícula",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = NegroTexto
                )
                Text(
                    text = "Inscríbete en uno o varios cursos libres simultáneamente y aplica beneficios UTS.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTextoSecundario
                )
            }

            // Alerta si el usuario no ha iniciado sesión o no se ha registrado
            if (!state.puedeLiquidar) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
                    border = BorderStroke(1.dp, MagentaOleo.copy(alpha = 0.4f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MagentaOleoClaro,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = MagentaOleo,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Sesión requerida",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MagentaOleo
                            )
                            Text(
                                text = "Inicia sesión o crea tu perfil en la pestaña '1. Perfil' para liquidar cursos.",
                                style = MaterialTheme.typography.bodySmall,
                                color = GrisTextoSecundario
                            )
                        }
                    }
                }
            }

            // Tarjeta 1: Selección Múltiple de Cursos Libres
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
                border = BorderStroke(1.dp, GrisPlata),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = CremaFondo,
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                        contentDescription = null,
                                        tint = VinoTintoOleo,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Oferta Académica",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = NegroTexto
                                )
                                Text(
                                    text = "Selecciona uno o varios cursos a tomar",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GrisTextoSecundario
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (state.cursosSeleccionados.isNotEmpty()) VerdeOpaloClaro else CremaFondo,
                            border = BorderStroke(1.dp, if (state.cursosSeleccionados.isNotEmpty()) VerdeOpalo else GrisPlata)
                        ) {
                            Text(
                                text = "${state.cursosSeleccionados.size} selec.",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (state.cursosSeleccionados.isNotEmpty()) NegroTexto else GrisTextoSecundario,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = GrisPlata, thickness = 1.dp)

                    // Lista de cursos libres seleccionables
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        state.cursosDisponibles.forEach { (curso, costo) ->
                            val estaSeleccionado = state.cursosSeleccionados.contains(curso)
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (estaSeleccionado) VerdeOpaloClaro.copy(alpha = 0.5f) else CremaFondo,
                                border = BorderStroke(
                                    width = if (estaSeleccionado) 1.5.dp else 1.dp,
                                    color = if (estaSeleccionado) VerdeOpalo else GrisPlata
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.toggleCursoSeleccionado(curso) }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = estaSeleccionado,
                                        onCheckedChange = { viewModel.toggleCursoSeleccionado(curso) },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = VerdeOpalo,
                                            checkmarkColor = NegroTexto
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = curso,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = if (estaSeleccionado) FontWeight.Bold else FontWeight.Medium,
                                            color = NegroTexto
                                        )
                                        Text(
                                            text = "Tarifa base: ${currencyFormat.format(costo)}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (estaSeleccionado) VinoTintoOleo else GrisTextoSecundario,
                                            fontWeight = if (estaSeleccionado) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Tarjeta 2: Beneficios Institucionales (Beca & Icetex)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
                border = BorderStroke(1.dp, GrisPlata),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = CremaFondo,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Percent,
                                    contentDescription = null,
                                    tint = VinoTintoOleo,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Beneficios Aplicables",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = NegroTexto
                            )
                            Text(
                                text = "Opciones de financiación y descuentos UTS",
                                style = MaterialTheme.typography.bodySmall,
                                color = GrisTextoSecundario
                            )
                        }
                    }

                    HorizontalDivider(color = GrisPlata, thickness = 1.dp)

                    // Becas Institucionales desde Supabase
                    if (state.becaHabilitada) {
                        Text(
                            text = "Becas Institucionales Disponibles",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = NegroTexto
                        )

                        if (state.becasDisponibles.isEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (state.tieneBeca) VerdeOpaloClaro else CremaFondo,
                                border = BorderStroke(1.dp, if (state.tieneBeca) VerdeOpalo else GrisPlata),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.onBecaCambiada(!state.tieneBeca) }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = state.tieneBeca,
                                        onCheckedChange = { viewModel.onBecaCambiada(it) },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = VerdeOpalo,
                                            checkmarkColor = NegroTexto
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Beca de Excelencia",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = NegroTexto
                                        )
                                        Text(
                                            text = "$becaPorcentajeInt% de descuento sobre el costo base",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = GrisTextoSecundario
                                        )
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = if (state.tieneBeca) VerdeOpalo else BlancoSuperficie,
                                        border = BorderStroke(1.dp, VerdeOpalo)
                                    ) {
                                        Text(
                                            text = "-$becaPorcentajeInt%",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (state.tieneBeca) NegroTexto else VerdeOpalo,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        } else {
                            state.becasDisponibles.filter { it.activa }.forEach { beca ->
                                val estaSeleccionada = state.tieneBeca && (state.becaSeleccionadaId == beca.id || (state.becaSeleccionadaId == null && state.becasDisponibles.firstOrNull()?.id == beca.id))
                                val pctDesc = (beca.porcentajeDescuento * 100).toInt()

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (estaSeleccionada) VerdeOpaloClaro else BlancoSuperficie,
                                    border = BorderStroke(
                                        width = if (estaSeleccionada) 1.5.dp else 1.dp,
                                        color = if (estaSeleccionada) VerdeOpalo else GrisPlata
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (estaSeleccionada) {
                                                viewModel.seleccionarBeca(null)
                                            } else {
                                                viewModel.seleccionarBeca(beca.id)
                                            }
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = estaSeleccionada,
                                            onCheckedChange = { check ->
                                                viewModel.seleccionarBeca(if (check) beca.id else null)
                                            },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = VerdeOpalo,
                                                checkmarkColor = NegroTexto
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = beca.nombre,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = if (estaSeleccionada) FontWeight.Bold else FontWeight.Medium,
                                                color = NegroTexto
                                            )
                                            Text(
                                                text = beca.requisitos,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = GrisTextoSecundario,
                                                maxLines = 1,
                                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Surface(
                                            shape = RoundedCornerShape(50),
                                            color = if (estaSeleccionada) VerdeOpalo else CremaFondo,
                                            border = BorderStroke(1.dp, VerdeOpalo)
                                        ) {
                                            Text(
                                                text = "-$pctDesc%",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = if (estaSeleccionada) NegroTexto else VinoTintoOleo,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Crédito Icetex
                    if (state.icetexHabilitado) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (state.tieneIcetex) MagentaOleoClaro else CremaFondo,
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (state.tieneIcetex) MagentaOleo else GrisPlata
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    role = Role.Switch,
                                    onClick = { viewModel.onIcetexCambiado(!state.tieneIcetex) }
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Crédito Icetex",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = NegroTexto
                                    )
                                    Text(
                                        text = "$icetexPorcentajeInt% de recargo sobre el valor neto tras descuentos",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GrisTextoSecundario
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = if (state.tieneIcetex) MagentaOleo else BlancoSuperficie,
                                    border = BorderStroke(1.dp, MagentaOleo)
                                ) {
                                    Text(
                                        text = "+$icetexPorcentajeInt%",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (state.tieneIcetex) BlancoSuperficie else MagentaOleo,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Switch(
                                    checked = state.tieneIcetex,
                                    onCheckedChange = { viewModel.onIcetexCambiado(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedTrackColor = MagentaOleo,
                                        checkedThumbColor = BlancoSuperficie
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Tarjeta 3: Resumen y Desglose Matemático Riguroso
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
                border = BorderStroke(1.dp, GrisPlata),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Liquidación Financiera",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = NegroTexto
                        )
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = GrisTextoSecundario
                        )
                    }

                    HorizontalDivider(color = GrisPlata, thickness = 1.dp)

                    // Subtotal base
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Subtotal cursos (${state.cursosSeleccionados.size}):",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrisTextoSecundario
                        )
                        Text(
                            text = currencyFormat.format(state.subtotalBase),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = NegroTexto
                        )
                    }

                    // Descuento Beca
                    if (state.tieneBeca && state.becaHabilitada) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Beca de Excelencia (-$becaPorcentajeInt%):",
                                style = MaterialTheme.typography.bodyMedium,
                                color = VinoTintoOleo
                            )
                            Text(
                                text = "- ${currencyFormat.format(state.descuentoBeca)}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = VinoTintoOleo
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Base neta tras descuento:",
                                style = MaterialTheme.typography.bodySmall,
                                color = GrisTextoSecundario
                            )
                            Text(
                                text = currencyFormat.format(state.baseConDescuento),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = NegroTexto
                            )
                        }
                    }

                    // Recargo Icetex (calculado tras aplicar el descuento)
                    if (state.tieneIcetex && state.icetexHabilitado) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Crédito Icetex (+$icetexPorcentajeInt% sobre base neta):",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MagentaOleo
                            )
                            Text(
                                text = "+ ${currencyFormat.format(state.recargoIcetex)}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MagentaOleo
                            )
                        }
                    }

                    HorizontalDivider(color = GrisPlata, thickness = 1.dp)

                    // Total a pagar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total a Pagar",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = NegroTexto
                            )
                            Text(
                                text = "Valor liquidado para matrícula",
                                style = MaterialTheme.typography.labelSmall,
                                color = GrisTextoSecundario
                            )
                        }
                        Text(
                            text = currencyFormat.format(state.totalCalculado),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = VinoTintoOleo
                        )
                    }
                }
            }

            // Botón Liquidar Matrícula
            Button(
                onClick = { viewModel.liquidarMatricula() },
                enabled = state.puedeLiquidar && state.cursosSeleccionados.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VinoTintoOleo,
                    contentColor = BlancoSuperficie,
                    disabledContainerColor = GrisPlataMate,
                    disabledContentColor = GrisTextoSecundario
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Calculate,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Liquidar Matrícula (${state.cursosSeleccionados.size} cursos)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
