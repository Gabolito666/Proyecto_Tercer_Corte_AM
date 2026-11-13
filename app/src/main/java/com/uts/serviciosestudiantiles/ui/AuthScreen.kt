package com.uts.serviciosestudiantiles.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.uts.serviciosestudiantiles.viewmodel.ModoAuth
import com.uts.serviciosestudiantiles.viewmodel.RolUsuario
import com.uts.serviciosestudiantiles.viewmodel.ServiciosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: ServiciosViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

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
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logotipo y Cabecera Institucional UTS
            Surface(
                shape = CircleShape,
                color = VinoTintoOleo,
                modifier = Modifier.size(60.dp),
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Logo UTS",
                        tint = VerdeOpalo,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "UTS Servicios Estudiantiles",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = NegroTexto
                )
                Text(
                    text = "Portal de Autenticación y Matrícula",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTextoSecundario
                )
            }

            // Notificación o Mensaje de Estado / Error
            AnimatedVisibility(
                visible = state.mensajeMensajeEstado != null,
                enter = fadeIn() + slideInVertically()
            ) {
                state.mensajeMensajeEstado?.let { mensaje ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
                        border = BorderStroke(
                            width = 1.5.dp,
                            color = if (state.esError) VinoTintoOleo else VerdeOpalo
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (state.esError) Icons.Default.Error else Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (state.esError) VinoTintoOleo else VerdeOpalo,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = mensaje,
                                style = MaterialTheme.typography.bodySmall,
                                color = NegroTexto,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // --- SELECTOR DE ROL DE USUARIO (ESTUDIANTE VS ADMINISTRADOR) ---
            Text(
                text = "Selecciona tu Tipo de Perfil",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = NegroTexto,
                modifier = Modifier.align(Alignment.Start)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Opción 1: Estudiante
                val isEstudiante = state.rolAuthSeleccionado == RolUsuario.ESTUDIANTE
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.onRolAuthCambiado(RolUsuario.ESTUDIANTE) },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isEstudiante) VerdeOpaloClaro else BlancoSuperficie
                    ),
                    border = BorderStroke(
                        width = if (isEstudiante) 2.dp else 1.dp,
                        color = if (isEstudiante) VerdeOpalo else GrisPlata
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isEstudiante,
                            onClick = { viewModel.onRolAuthCambiado(RolUsuario.ESTUDIANTE) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = VinoTintoOleo
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = "Estudiante",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                color = NegroTexto
                            )
                            Text(
                                text = "Matrículas y pagos",
                                style = MaterialTheme.typography.labelSmall,
                                color = GrisTextoSecundario
                            )
                        }
                    }
                }

                // Opción 2: Administrador
                val isAdmin = state.rolAuthSeleccionado == RolUsuario.ADMINISTRADOR
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.onRolAuthCambiado(RolUsuario.ADMINISTRADOR) },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isAdmin) CremaFondo else BlancoSuperficie
                    ),
                    border = BorderStroke(
                        width = if (isAdmin) 2.dp else 1.dp,
                        color = if (isAdmin) VinoTintoOleo else GrisPlata
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isAdmin,
                            onClick = { viewModel.onRolAuthCambiado(RolUsuario.ADMINISTRADOR) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = VinoTintoOleo
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = "Administrador",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                color = NegroTexto
                            )
                            Text(
                                text = "Gestión institucional",
                                style = MaterialTheme.typography.labelSmall,
                                color = GrisTextoSecundario
                            )
                        }
                    }
                }
            }

            // --- FORMULARIO PARA ESTUDIANTE (LOGIN O REGISTRO) ---
            if (state.rolAuthSeleccionado == RolUsuario.ESTUDIANTE) {
                // Selector de modo de autenticación (Iniciar Sesión vs Registrarse)
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = BlancoSuperficie,
                    border = BorderStroke(1.dp, GrisPlata)
                ) {
                    SecondaryTabRow(
                        selectedTabIndex = if (state.modoAuth == ModoAuth.LOGIN) 0 else 1,
                        containerColor = BlancoSuperficie,
                        contentColor = VinoTintoOleo,
                        indicator = {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(if (state.modoAuth == ModoAuth.LOGIN) 0 else 1),
                                height = 3.dp,
                                color = VinoTintoOleo
                            )
                        },
                        divider = {}
                    ) {
                        Tab(
                            selected = state.modoAuth == ModoAuth.LOGIN,
                            onClick = { viewModel.setModoAuth(ModoAuth.LOGIN) },
                            text = {
                                Text(
                                    text = "Iniciar Sesión",
                                    fontWeight = if (state.modoAuth == ModoAuth.LOGIN) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Login,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        )
                        Tab(
                            selected = state.modoAuth == ModoAuth.REGISTRO,
                            onClick = { viewModel.setModoAuth(ModoAuth.REGISTRO) },
                            text = {
                                Text(
                                    text = "Registrarse",
                                    fontWeight = if (state.modoAuth == ModoAuth.REGISTRO) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.PersonAdd,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        )
                    }
                }

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
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        if (state.modoAuth == ModoAuth.LOGIN) {
                            // MODO LOGIN ESTUDIANTE
                            Text(
                                text = "Ingreso con Código Estudiantil",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = NegroTexto
                            )

                            OutlinedTextField(
                                value = state.campoCodigo,
                                onValueChange = viewModel::onCodigoCambiado,
                                label = { Text("Código de Estudiante") },
                                placeholder = { Text("Ej. 10987654") },
                                leadingIcon = {
                                    Icon(Icons.Default.Badge, contentDescription = null)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = textFieldColors
                            )

                            OutlinedTextField(
                                value = state.campoPassword,
                                onValueChange = viewModel::onPasswordCambiado,
                                label = { Text("Contraseña") },
                                placeholder = { Text("Ingresa tu contraseña") },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = null)
                                },
                                trailingIcon = {
                                    IconButton(onClick = viewModel::togglePasswordVisible) {
                                        Icon(
                                            imageVector = if (state.passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = "Mostrar u ocultar contraseña"
                                        )
                                    }
                                },
                                visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = textFieldColors
                            )

                            Button(
                                onClick = { viewModel.iniciarSesion() },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(containerColor = VinoTintoOleo)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Login, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Iniciar Sesión como Estudiante", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            // MODO REGISTRO ESTUDIANTE
                            Text(
                                text = "Registro de Nuevo Estudiante UTS",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = NegroTexto
                            )

                            OutlinedTextField(
                                value = state.campoNombre,
                                onValueChange = viewModel::onNombreCambiado,
                                label = { Text("Nombre Completo") },
                                placeholder = { Text("Ej. Gabriel Prada") },
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = textFieldColors
                            )

                            OutlinedTextField(
                                value = state.campoCodigo,
                                onValueChange = viewModel::onCodigoCambiado,
                                label = { Text("Código Estudiantil Único") },
                                placeholder = { Text("Ej. 10987654") },
                                leadingIcon = {
                                    Icon(Icons.Default.Badge, contentDescription = null)
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = textFieldColors
                            )

                            // Selector desplegable de Facultad
                            ExposedDropdownMenuBox(
                                expanded = state.menuFacultadAbierto,
                                onExpandedChange = viewModel::toggleMenuFacultad,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = state.facultadSeleccionada,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Facultad Académica") },
                                    leadingIcon = {
                                        Icon(Icons.Default.School, contentDescription = null)
                                    },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.menuFacultadAbierto)
                                    },
                                    modifier = Modifier
                                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = textFieldColors
                                )
                                ExposedDropdownMenu(
                                    expanded = state.menuFacultadAbierto,
                                    onDismissRequest = { viewModel.toggleMenuFacultad(false) }
                                ) {
                                    viewModel.facultades.forEach { facultad ->
                                        DropdownMenuItem(
                                            text = { Text(facultad, color = NegroTexto) },
                                            onClick = { viewModel.onFacultadSeleccionada(facultad) }
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = state.campoPassword,
                                onValueChange = viewModel::onPasswordCambiado,
                                label = { Text("Contraseña de Acceso") },
                                placeholder = { Text("Crea tu contraseña") },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = null)
                                },
                                trailingIcon = {
                                    IconButton(onClick = viewModel::togglePasswordVisible) {
                                        Icon(
                                            imageVector = if (state.passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = "Mostrar u ocultar contraseña"
                                        )
                                    }
                                },
                                visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = textFieldColors
                            )

                            Button(
                                onClick = { viewModel.guardarPerfil() },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(containerColor = VinoTintoOleo)
                            ) {
                                Icon(Icons.Default.PersonAdd, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Completar Registro", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                // --- FORMULARIO PARA ADMINISTRADOR ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
                    border = BorderStroke(1.5.dp, VinoTintoOleo),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = VinoTintoOleo,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.AdminPanelSettings,
                                        contentDescription = null,
                                        tint = VerdeOpalo,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Acceso Administrativo UTS",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = NegroTexto
                                )
                                Text(
                                    text = "Dirección Académica y Financiera",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GrisTextoSecundario
                                )
                            }
                        }

                        HorizontalDivider(color = GrisPlata)

                        OutlinedTextField(
                            value = state.campoCodigo,
                            onValueChange = viewModel::onCodigoCambiado,
                            label = { Text("Código de Administrador") },
                            placeholder = { Text("ADMIN") },
                            leadingIcon = {
                                Icon(Icons.Default.AdminPanelSettings, contentDescription = null)
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = textFieldColors
                        )

                        OutlinedTextField(
                            value = state.campoPassword,
                            onValueChange = viewModel::onPasswordCambiado,
                            label = { Text("Contraseña Administrativa") },
                            placeholder = { Text("Clave institucional") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null)
                            },
                            trailingIcon = {
                                IconButton(onClick = viewModel::togglePasswordVisible) {
                                    Icon(
                                        imageVector = if (state.passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Mostrar u ocultar contraseña"
                                    )
                                }
                            },
                            visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = textFieldColors
                        )

                        Button(
                            onClick = { viewModel.iniciarSesion("ADMIN") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = VinoTintoOleo)
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = VerdeOpalo)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ingresar al Panel de Administrador", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.onCodigoCambiado("ADMIN")
                                viewModel.iniciarSesion("ADMIN")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, VerdeOpalo)
                        ) {
                            Text("Acceso Rápido Demo Administrador", color = VinoTintoOleo, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
