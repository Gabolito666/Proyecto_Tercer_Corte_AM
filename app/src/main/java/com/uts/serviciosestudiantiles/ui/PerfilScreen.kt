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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
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
import androidx.compose.material3.OutlinedButton
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
import com.uts.serviciosestudiantiles.ui.theme.BlancoSuperficie
import com.uts.serviciosestudiantiles.ui.theme.CremaFondo
import com.uts.serviciosestudiantiles.ui.theme.GrisPlata
import com.uts.serviciosestudiantiles.ui.theme.GrisPlataMate
import com.uts.serviciosestudiantiles.ui.theme.GrisTextoSecundario
import com.uts.serviciosestudiantiles.ui.theme.NegroTexto
import com.uts.serviciosestudiantiles.ui.theme.VerdeOpalo
import com.uts.serviciosestudiantiles.ui.theme.VerdeOpaloClaro
import com.uts.serviciosestudiantiles.ui.theme.VinoTintoOleo
import com.uts.serviciosestudiantiles.viewmodel.ModoAuth
import com.uts.serviciosestudiantiles.viewmodel.ServiciosViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    viewModel: ServiciosViewModel,
    onNavegarAdmin: () -> Unit = {},
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
                .verticalScroll(scrollState)
                .imePadding()
                .navigationBarsPadding()
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
                    text = "Módulo 1",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = VinoTintoOleo,
                    letterSpacing = 1.sp
                )
                Text(
                    text = if (state.perfilGuardado != null) "Perfil y Cuenta" else "Acceso y Registro",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = NegroTexto
                )
                Text(
                    text = if (state.perfilGuardado != null)
                        "Gestiona tu sesión activa y revisa tus credenciales institucionales."
                    else
                        "Inicia sesión si ya eres estudiante o regístrate para nuevos servicios.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTextoSecundario
                )
            }

            // Indicador visual de estado / Notificaciones (mensajeMensajeEstado)
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
                                    text = if (state.esError) "Atención" else if (state.esAdmin) "Administración UTS" else if (state.esUsuarioExistente) "Sesión Activa" else "Registro Exitoso",
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

            // --- CASO 1: USUARIO CON SESIÓN ACTIVA ---
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
                                Column {
                                    Text(
                                        text = perfil.nombre,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = NegroTexto
                                    )
                                    Text(
                                        text = if (state.esAdmin) "Rol: Administrador General" else "Código: ${perfil.codigo}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GrisTextoSecundario
                                    )
                                }
                            }

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

                        // Información institucional
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Facultad: ${perfil.facultad}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = NegroTexto,
                                fontWeight = FontWeight.SemiBold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = VinoTintoOleo, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = perfil.correoInstitucional,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = VinoTintoOleo
                                )
                            }
                        }

                        // Botón de panel admin si corresponde
                        if (state.esAdmin) {
                            Button(
                                onClick = onNavegarAdmin,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(containerColor = VinoTintoOleo)
                            ) {
                                Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = VerdeOpalo)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Abrir Panel de Administración", fontWeight = FontWeight.Bold)
                            }
                        }

                        // Botón para cerrar sesión
                        OutlinedButton(
                            onClick = { viewModel.cerrarSesion() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = VinoTintoOleo),
                            border = BorderStroke(1.5.dp, VinoTintoOleo)
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cerrar Sesión / Cambiar Cuenta", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                // --- CASO 2: NO HAY SESIÓN (SELECTOR INICIAR SESIÓN / REGISTRARSE) ---

                // Selector de Modo (Iniciar Sesión vs Registrarse)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = BlancoSuperficie,
                    border = BorderStroke(1.dp, GrisPlata)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Button(
                            onClick = { viewModel.setModoAuth(ModoAuth.LOGIN) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (state.modoAuth == ModoAuth.LOGIN) VinoTintoOleo else BlancoSuperficie,
                                contentColor = if (state.modoAuth == ModoAuth.LOGIN) BlancoSuperficie else GrisTextoSecundario
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = if (state.modoAuth == ModoAuth.LOGIN) 2.dp else 0.dp)
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.Login, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Iniciar Sesión", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                        }

                        Button(
                            onClick = { viewModel.setModoAuth(ModoAuth.REGISTRO) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (state.modoAuth == ModoAuth.REGISTRO) VinoTintoOleo else BlancoSuperficie,
                                contentColor = if (state.modoAuth == ModoAuth.REGISTRO) BlancoSuperficie else GrisTextoSecundario
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = if (state.modoAuth == ModoAuth.REGISTRO) 2.dp else 0.dp)
                        ) {
                            Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Registrarse", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }

                // FORMULARIO DINÁMICO
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
                        if (state.modoAuth == ModoAuth.LOGIN) {
                            // --- VISTA INICIAR SESIÓN ---
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(shape = CircleShape, color = CremaFondo, modifier = Modifier.size(42.dp)) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(imageVector = Icons.AutoMirrored.Filled.Login, contentDescription = null, tint = VinoTintoOleo, modifier = Modifier.size(22.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Ingreso de Estudiantes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NegroTexto)
                                    Text("Digita tu código estudiantil o código 'ADMIN'", style = MaterialTheme.typography.bodySmall, color = GrisTextoSecundario)
                                }
                            }

                            HorizontalDivider(color = GrisPlata)

                            OutlinedTextField(
                                value = state.campoCodigo,
                                onValueChange = { viewModel.onCodigoCambiado(it) },
                                label = { Text("Código Estudiantil o Admin") },
                                placeholder = { Text("Ej. 1098765432 o ADMIN") },
                                isError = state.esError,
                                leadingIcon = { Icon(imageVector = Icons.Default.Badge, contentDescription = null) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .bringIntoViewRequester(bringIntoViewRequesterCodigo)
                                    .onFocusEvent { if (it.isFocused) coroutineScope.launch { delay(250); bringIntoViewRequesterCodigo.bringIntoView() } },
                                shape = RoundedCornerShape(16.dp),
                                colors = textFieldColors
                            )

                            Button(
                                onClick = { viewModel.iniciarSesion() },
                                enabled = state.campoCodigo.isNotBlank(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(containerColor = VinoTintoOleo)
                            ) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.Login, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Iniciar Sesión", fontWeight = FontWeight.Bold)
                            }

                            // Botón rápido para acceder como Administrador
                            OutlinedButton(
                                onClick = { viewModel.iniciarSesion("ADMIN") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.dp, GrisPlata)
                            ) {
                                Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = VinoTintoOleo, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Ingresar como Administrador (Demo)", color = VinoTintoOleo, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
                            }
                        } else {
                            // --- VISTA REGISTRO NUEVO ESTUDIANTE ---
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(shape = CircleShape, color = CremaFondo, modifier = Modifier.size(42.dp)) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, tint = VinoTintoOleo, modifier = Modifier.size(22.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("Nuevo Registro Estudiantil", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = NegroTexto)
                                    Text("Completa tus datos para crear tu cuenta UTS", style = MaterialTheme.typography.bodySmall, color = GrisTextoSecundario)
                                }
                            }

                            HorizontalDivider(color = GrisPlata)

                            OutlinedTextField(
                                value = state.campoNombre,
                                onValueChange = { viewModel.onNombreCambiado(it) },
                                label = { Text("Nombre Completo") },
                                placeholder = { Text("Ej. Gabriel Prado") },
                                leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .bringIntoViewRequester(bringIntoViewRequesterNombre)
                                    .onFocusEvent { if (it.isFocused) coroutineScope.launch { delay(250); bringIntoViewRequesterNombre.bringIntoView() } },
                                shape = RoundedCornerShape(16.dp),
                                colors = textFieldColors
                            )

                            OutlinedTextField(
                                value = state.campoCodigo,
                                onValueChange = { viewModel.onCodigoCambiado(it) },
                                label = { Text("Código Estudiantil") },
                                placeholder = { Text("Ej. 1098765432") },
                                isError = state.esError,
                                supportingText = if (state.esError) {
                                    { Text("El código ya está ocupado", color = VinoTintoOleo, fontWeight = FontWeight.SemiBold) }
                                } else null,
                                leadingIcon = { Icon(imageVector = Icons.Default.Badge, contentDescription = null) },
                                trailingIcon = if (state.esError) {
                                    { Icon(imageVector = Icons.Default.Error, contentDescription = null, tint = VinoTintoOleo) }
                                } else null,
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .bringIntoViewRequester(bringIntoViewRequesterCodigo)
                                    .onFocusEvent { if (it.isFocused) coroutineScope.launch { delay(250); bringIntoViewRequesterCodigo.bringIntoView() } },
                                shape = RoundedCornerShape(16.dp),
                                colors = textFieldColors
                            )

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
                                    leadingIcon = { Icon(imageVector = Icons.Default.School, contentDescription = null) },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.menuFacultadAbierto) },
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
                                            text = { Text(text = facultad, color = NegroTexto) },
                                            onClick = { viewModel.onFacultadSeleccionada(facultad) }
                                        )
                                    }
                                }
                            }

                            Button(
                                onClick = { viewModel.guardarPerfil() },
                                enabled = state.campoNombre.isNotBlank() && state.campoCodigo.isNotBlank(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(containerColor = VinoTintoOleo)
                            ) {
                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Registrarse y Crear Cuenta", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
