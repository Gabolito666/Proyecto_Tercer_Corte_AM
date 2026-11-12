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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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

@OptIn(ExperimentalMaterial3Api::class)
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

    val costoBaseActual = viewModel.cursosConCostos[state.cursoSeleccionado] ?: 0.0
    val descuentoEstimado = if (state.tieneBeca) costoBaseActual * 0.5 else 0.0
    val recargoEstimado = if (state.tieneIcetex) costoBaseActual * 0.05 else 0.0
    val totalEstimado = costoBaseActual - descuentoEstimado + recargoEstimado

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = VinoTintoOleo,
        unfocusedBorderColor = GrisPlata,
        focusedLabelColor = VinoTintoOleo,
        unfocusedLabelColor = GrisTextoSecundario,
        cursorColor = VinoTintoOleo,
        focusedLeadingIconColor = VinoTintoOleo,
        unfocusedLeadingIconColor = GrisTextoSecundario,
        focusedTrailingIconColor = VinoTintoOleo,
        unfocusedTrailingIconColor = GrisTextoSecundario,
        focusedContainerColor = BlancoSuperficie,
        unfocusedContainerColor = BlancoSuperficie,
        focusedTextColor = NegroTexto,
        unfocusedTextColor = NegroTexto
    )

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
            verticalArrangement = Arrangement.spacedBy(18.dp)
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
                    text = "Selecciona un curso libre y aplica beneficios institucionales.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTextoSecundario
                )
            }

            // Alerta de estado si el perfil no ha sido registrado
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
                                text = "Perfil pendiente",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MagentaOleo
                            )
                            Text(
                                text = "Completa y guarda tus datos en la pestaña '1. Perfil' para habilitar la liquidación.",
                                style = MaterialTheme.typography.bodySmall,
                                color = GrisTextoSecundario
                            )
                        }
                    }
                }
            }

            // Tarjeta 1: Selector de Cursos Libres (Oferta Ampliada)
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
                                text = "7 cursos libres disponibles con tarifas base UTS",
                                style = MaterialTheme.typography.bodySmall,
                                color = GrisTextoSecundario
                            )
                        }
                    }

                    HorizontalDivider(color = GrisPlata, thickness = 1.dp)

                    ExposedDropdownMenuBox(
                        expanded = state.menuCursoAbierto,
                        onExpandedChange = { viewModel.toggleMenuCurso(it) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = "${state.cursoSeleccionado}  •  ${currencyFormat.format(costoBaseActual)}",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Curso Libre Seleccionado") },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Receipt, contentDescription = null)
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.menuCursoAbierto)
                            },
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = textFieldColors
                        )

                        ExposedDropdownMenu(
                            expanded = state.menuCursoAbierto,
                            onDismissRequest = { viewModel.toggleMenuCurso(false) },
                            modifier = Modifier.background(BlancoSuperficie)
                        ) {
                            viewModel.cursosConCostos.forEach { (curso, costo) ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(end = 12.dp)
                                            ) {
                                                Text(
                                                    text = curso,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    fontWeight = if (curso == state.cursoSeleccionado) FontWeight.Bold else FontWeight.Medium,
                                                    color = NegroTexto
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "Tarifa base: ${currencyFormat.format(costo)}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = VinoTintoOleo
                                                )
                                            }
                                            Surface(
                                                shape = RoundedCornerShape(50),
                                                color = if (curso == state.cursoSeleccionado) VerdeOpaloClaro else CremaFondo,
                                                border = BorderStroke(
                                                    1.dp,
                                                    if (curso == state.cursoSeleccionado) VerdeOpalo else GrisPlata
                                                )
                                            ) {
                                                Text(
                                                    text = currencyFormat.format(costo),
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (curso == state.cursoSeleccionado) VinoTintoOleo else NegroTexto,
                                                    maxLines = 1,
                                                    softWrap = false,
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        viewModel.onCursoSeleccionado(curso)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Tarjeta 2: Beneficios y Descuentos (Beca de Excelencia & Icetex)
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
                                text = "Selecciona las condiciones de tu matrícula",
                                style = MaterialTheme.typography.bodySmall,
                                color = GrisTextoSecundario
                            )
                        }
                    }

                    HorizontalDivider(color = GrisPlata, thickness = 1.dp)

                    // Fila 1: Beca de Excelencia estilizada con VerdeOpalo
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (state.tieneBeca) VerdeOpaloClaro else CremaFondo,
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (state.tieneBeca) VerdeOpalo else GrisPlata
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                role = Role.Checkbox,
                                onClick = { viewModel.onBecaCambiada(!state.tieneBeca) }
                            )
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
                                    checkmarkColor = NegroTexto,
                                    uncheckedColor = GrisTextoSecundario
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
                                    text = "50% de descuento sobre el costo base",
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
                                    text = "-50%",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (state.tieneBeca) NegroTexto else VerdeOpalo,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    // Fila 2: Crédito Icetex estilizado con MagentaOleo
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
                                    text = "5% de recargo administrativo",
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
                                    text = "+5%",
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
                                    checkedThumbColor = BlancoSuperficie,
                                    checkedTrackColor = MagentaOleo,
                                    uncheckedThumbColor = BlancoSuperficie,
                                    uncheckedTrackColor = GrisPlataMate,
                                    uncheckedBorderColor = GrisPlata
                                )
                            )
                        }
                    }
                }
            }

            // Tarjeta 3: Resumen y desglose de liquidación tipo factura Nubank
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
                            text = "Resumen de Liquidación",
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Costo base del curso:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = GrisTextoSecundario
                        )
                        Text(
                            text = currencyFormat.format(costoBaseActual),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = NegroTexto
                        )
                    }

                    if (state.tieneBeca) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Beca de Excelencia (-50%):",
                                style = MaterialTheme.typography.bodyMedium,
                                color = VinoTintoOleo
                            )
                            Text(
                                text = "- ${currencyFormat.format(descuentoEstimado)}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = VinoTintoOleo
                            )
                        }
                    }

                    if (state.tieneIcetex) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Recargo Icetex (+5%):",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MagentaOleo
                            )
                            Text(
                                text = "+ ${currencyFormat.format(recargoEstimado)}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MagentaOleo
                            )
                        }
                    }

                    HorizontalDivider(color = GrisPlata, thickness = 1.dp)

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
                                text = "Valor final liquidado",
                                style = MaterialTheme.typography.labelSmall,
                                color = GrisTextoSecundario
                            )
                        }
                        Text(
                            text = currencyFormat.format(totalEstimado),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = VinoTintoOleo
                        )
                    }
                }
            }

            // Botón "Liquidar Matrícula" tipo píldora
            // VinoTintoOleo cuando esté activo y GrisPlata mate cuando esté deshabilitado (enabled = state.puedeLiquidar)
            Button(
                onClick = { viewModel.liquidarMatricula() },
                enabled = state.puedeLiquidar,
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
                    text = "Liquidar Matrícula",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
