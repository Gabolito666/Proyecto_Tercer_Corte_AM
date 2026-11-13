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
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.uts.serviciosestudiantiles.ui.theme.BlancoSuperficie
import com.uts.serviciosestudiantiles.ui.theme.CremaFondo
import com.uts.serviciosestudiantiles.ui.theme.GrisPlata
import com.uts.serviciosestudiantiles.ui.theme.GrisPlataMate
import com.uts.serviciosestudiantiles.ui.theme.GrisTextoSecundario
import com.uts.serviciosestudiantiles.ui.theme.NegroTexto
import com.uts.serviciosestudiantiles.ui.theme.VerdeOpalo
import com.uts.serviciosestudiantiles.ui.theme.VerdeOpaloClaro
import com.uts.serviciosestudiantiles.ui.theme.VinoTintoOleo
import com.uts.serviciosestudiantiles.viewmodel.ServiciosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    viewModel: ServiciosViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequesterNombre = remember { BringIntoViewRequester() }
    val bringIntoViewRequesterCodigo = remember { BringIntoViewRequester() }

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
                .verticalScroll(rememberScrollState())
                .imePadding()
                .navigationBarsPadding()
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
                    text = "Módulo 1",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = VinoTintoOleo,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Gestión de Perfil",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = NegroTexto
                )
                Text(
                    text = "Configura tus datos para acceder a los servicios de matrícula UTS.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTextoSecundario
                )
            }

            // Indicador visual de estado de persistencia y sesión (mensajeMensajeEstado)
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
                            color = if (state.esUsuarioExistente) VerdeOpalo else VinoTintoOleo
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (state.esUsuarioExistente) VerdeOpaloClaro else CremaFondo,
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (state.esUsuarioExistente) Icons.Default.Verified else Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (state.esUsuarioExistente) VinoTintoOleo else VerdeOpalo,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (state.esUsuarioExistente) "Estudiante Existente Cargado" else "Registro Local Exitoso",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (state.esUsuarioExistente) VinoTintoOleo else NegroTexto
                                )
                                Text(
                                    text = mensaje,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = NegroTexto
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = if (state.esUsuarioExistente) VerdeOpalo else VinoTintoOleo
                            ) {
                                Text(
                                    text = if (state.esUsuarioExistente) "Cargado" else "Nuevo",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (state.esUsuarioExistente) NegroTexto else BlancoSuperficie,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Tarjeta limpia de fondo blanco sobre CremaFondo para el Formulario
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
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = CremaFondo,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = VinoTintoOleo,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Datos del Estudiante",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = NegroTexto
                            )
                            Text(
                                text = "Información requerida para la matrícula",
                                style = MaterialTheme.typography.bodySmall,
                                color = GrisTextoSecundario
                            )
                        }
                    }

                    HorizontalDivider(color = GrisPlata, thickness = 1.dp)

                    // Campo 1: Nombre Completo
                    OutlinedTextField(
                        value = state.campoNombre,
                        onValueChange = { viewModel.onNombreCambiado(it) },
                        label = { Text("Nombre Completo") },
                        placeholder = { Text("Ej. Gabriel Prado") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            keyboardType = KeyboardType.Text
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .bringIntoViewRequester(bringIntoViewRequesterNombre)
                            .onFocusEvent { focusState ->
                                if (focusState.isFocused) {
                                    coroutineScope.launch {
                                        delay(250)
                                        bringIntoViewRequesterNombre.bringIntoView()
                                    }
                                }
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = textFieldColors
                    )

                    // Campo 2: Código Estudiantil
                    OutlinedTextField(
                        value = state.campoCodigo,
                        onValueChange = { viewModel.onCodigoCambiado(it) },
                        label = { Text("Código Estudiantil") },
                        placeholder = { Text("Ej. 1098765432") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Badge, contentDescription = null)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .bringIntoViewRequester(bringIntoViewRequesterCodigo)
                            .onFocusEvent { focusState ->
                                if (focusState.isFocused) {
                                    coroutineScope.launch {
                                        delay(250)
                                        bringIntoViewRequesterCodigo.bringIntoView()
                                    }
                                }
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = textFieldColors
                    )

                    // Selector: Facultad
                    ExposedDropdownMenuBox(
                        expanded = state.menuFacultadAbierto,
                        onExpandedChange = { viewModel.toggleMenuFacultad(it) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = state.facultadSeleccionada,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Facultad") },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.School, contentDescription = null)
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.menuFacultadAbierto)
                            },
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = textFieldColors
                        )

                        ExposedDropdownMenu(
                            expanded = state.menuFacultadAbierto,
                            onDismissRequest = { viewModel.toggleMenuFacultad(false) },
                            modifier = Modifier.background(BlancoSuperficie)
                        ) {
                            viewModel.facultades.forEach { facultad ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = facultad,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = NegroTexto
                                        )
                                    },
                                    onClick = {
                                        viewModel.onFacultadSeleccionada(facultad)
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Botón "Guardar Perfil" tipo píldora (RoundedCornerShape 50%) en VinoTintoOleo con texto blanco
                    val isFormValid = state.campoNombre.isNotBlank() && state.campoCodigo.isNotBlank()
                    Button(
                        onClick = { viewModel.guardarPerfil() },
                        enabled = isFormValid,
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
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Guardar Perfil",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Correo institucional generado: tarjeta destacada con borde VerdeOpalo e ícono indicador
            AnimatedVisibility(
                visible = state.perfilGuardado != null || state.correoSimulado.isNotEmpty(),
                enter = fadeIn() + slideInVertically()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
                    border = BorderStroke(2.dp, VerdeOpalo),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = VerdeOpaloClaro,
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.VerifiedUser,
                                            contentDescription = "Indicador de verificación",
                                            tint = VinoTintoOleo,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Correo Institucional Generado",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = NegroTexto
                                    )
                                    Text(
                                        text = "Cuenta asignada por el sistema UTS",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GrisTextoSecundario
                                    )
                                }
                            }
                        }

                        // Caja destacada de correo institucional
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = CremaFondo,
                            border = BorderStroke(1.dp, GrisPlata),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    tint = VinoTintoOleo,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = state.correoSimulado,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = VinoTintoOleo
                                )
                            }
                        }

                        // Resumen del estudiante registrado
                        state.perfilGuardado?.let { perfil ->
                            HorizontalDivider(color = GrisPlata, thickness = 1.dp)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Estudiante",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = GrisTextoSecundario
                                    )
                                    Text(
                                        text = perfil.nombre,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = NegroTexto
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Código / Facultad",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = GrisTextoSecundario
                                    )
                                    Text(
                                        text = "${perfil.codigo} • ${perfil.facultad}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = NegroTexto
                                    )
                                }
                            }
                        }

                        // Etiqueta tipo píldora de confirmación
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = VerdeOpaloClaro,
                            border = BorderStroke(1.dp, VerdeOpalo.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = NegroTexto,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (state.esUsuarioExistente) "Perfil Restaurado • Matrícula Habilitada" else "Perfil Activo • Matrícula Habilitada",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = NegroTexto
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
