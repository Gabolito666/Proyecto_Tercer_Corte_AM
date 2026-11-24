package com.uts.serviciosestudiantiles.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyecto_tercer_corte.R
import com.uts.serviciosestudiantiles.ui.theme.FondoAccionPrincipal
import com.uts.serviciosestudiantiles.ui.theme.TextoAccionPrincipal
import com.uts.serviciosestudiantiles.viewmodel.ModoAuth
import com.uts.serviciosestudiantiles.viewmodel.RolUsuario
import com.uts.serviciosestudiantiles.viewmodel.ServiciosViewModel

@Composable
fun AuthScreen(
    viewModel: ServiciosViewModel,
    modifier: Modifier = Modifier,
    onAbrirAjustes: () -> Unit = {}
) {
    AuthScreenContent(viewModel, modifier, onAbrirAjustes)
}

// El tema se aplica antes de leer sus colores; los eventos siguen siendo los mismos.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthScreenContent(
    viewModel: ServiciosViewModel,
    modifier: Modifier,
    onAbrirAjustes: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val colors = MaterialTheme.colorScheme

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = colors.primary,
        unfocusedBorderColor = colors.outline,
        focusedLabelColor = colors.primary,
        unfocusedLabelColor = colors.onSurfaceVariant,
        cursorColor = colors.primary,
        focusedLeadingIconColor = colors.primary,
        unfocusedLeadingIconColor = colors.onSurfaceVariant,
        focusedTrailingIconColor = colors.primary,
        unfocusedTrailingIconColor = colors.onSurfaceVariant,
        focusedContainerColor = colors.background,
        unfocusedContainerColor = colors.background,
        focusedTextColor = colors.onSurface,
        unfocusedTextColor = colors.onSurface,
        focusedPlaceholderColor = colors.onSurfaceVariant,
        unfocusedPlaceholderColor = colors.onSurfaceVariant
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Marca de agua discreta para mantener el fondo minimalista.
        Image(
            painter = painterResource(R.drawable.mascota_u_uts),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            alpha = 0.07f,
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(max = 680.dp)
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.85f)
        )
        val perfilesEnColumna = maxWidth < 340.dp || LocalDensity.current.fontScale > 1.3f
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .widthIn(max = 560.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 20.dp)
                .padding(top = 72.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logotipo y Cabecera Institucional UTS
            Surface(
                shape = CircleShape,
                color = colors.primaryContainer,
                modifier = Modifier.size(72.dp),
                border = BorderStroke(1.dp, colors.primary.copy(alpha = 0.5f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Logo UTS",
                        tint = colors.onPrimaryContainer,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "UTS Servicios Estudiantiles",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = colors.onBackground,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Portal de Autenticación y Matrícula",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant,
                    textAlign = TextAlign.Center
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
                        colors = CardDefaults.cardColors(containerColor = colors.surface),
                        border = BorderStroke(
                            width = 1.5.dp,
                            color = if (state.esError) colors.error else colors.primary
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
                                tint = if (state.esError) colors.error else colors.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = mensaje,
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.onSurface,
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
                color = colors.onBackground,
                modifier = Modifier.align(Alignment.Start)
            )

            // En teléfonos estrechos o con letra grande las tarjetas se apilan.
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                maxItemsInEachRow = if (perfilesEnColumna) 1 else 2
            ) {
                // Opción 1: Estudiante
                val isEstudiante = state.rolAuthSeleccionado == RolUsuario.ESTUDIANTE
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.onRolAuthCambiado(RolUsuario.ESTUDIANTE) },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isEstudiante) colors.secondaryContainer else colors.surface
                    ),
                    border = BorderStroke(
                        width = if (isEstudiante) 2.dp else 1.dp,
                        color = if (isEstudiante) colors.primary else colors.outlineVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = colors.primary)
                            RadioButton(
                                selected = isEstudiante,
                                onClick = { viewModel.onRolAuthCambiado(RolUsuario.ESTUDIANTE) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = colors.primary,
                                    unselectedColor = colors.onSurfaceVariant
                                )
                            )
                        }
                        Column {
                            Text(
                                text = "Estudiante",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.onSurface
                            )
                            Text(
                                text = "Matrículas y pagos",
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.onSurfaceVariant
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
                        containerColor = if (isAdmin) colors.secondaryContainer else colors.surface
                    ),
                    border = BorderStroke(
                        width = if (isAdmin) 2.dp else 1.dp,
                        color = if (isAdmin) colors.primary else colors.outlineVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = colors.primary)
                            RadioButton(
                                selected = isAdmin,
                                onClick = { viewModel.onRolAuthCambiado(RolUsuario.ADMINISTRADOR) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = colors.primary,
                                    unselectedColor = colors.onSurfaceVariant
                                )
                            )
                        }
                        Column {
                            Text(
                                text = "Administrador",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                color = colors.onSurface
                            )
                            Text(
                                text = "Gestión institucional",
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.onSurfaceVariant
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
                    color = colors.surface,
                    border = BorderStroke(1.dp, colors.outlineVariant)
                ) {
                    SecondaryTabRow(
                        selectedTabIndex = if (state.modoAuth == ModoAuth.LOGIN) 0 else 1,
                        containerColor = colors.surface,
                        contentColor = colors.primary,
                        indicator = {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(if (state.modoAuth == ModoAuth.LOGIN) 0 else 1),
                                height = 3.dp,
                                color = colors.primary
                            )
                        },
                        divider = {}
                    ) {
                        Tab(
                            selected = state.modoAuth == ModoAuth.LOGIN,
                            onClick = { viewModel.setModoAuth(ModoAuth.LOGIN) },
                            selectedContentColor = colors.primary,
                            unselectedContentColor = colors.onSurfaceVariant,
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
                            selectedContentColor = colors.primary,
                            unselectedContentColor = colors.onSurfaceVariant,
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
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    border = BorderStroke(1.dp, colors.outlineVariant),
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
                                color = colors.onSurface
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
                                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = FondoAccionPrincipal,
                                    contentColor = TextoAccionPrincipal
                                )
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Login, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Iniciar Sesión como Estudiante", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                            }
                        } else {
                            // MODO REGISTRO ESTUDIANTE
                            Text(
                                text = "Registro de Nuevo Estudiante UTS",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.onSurface
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

                            // Aviso institucional de asignación de contraseña y cambio obligatorio
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = colors.background),
                                border = BorderStroke(1.dp, colors.outlineVariant)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = colors.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Tu contraseña inicial será automáticamente tu código de estudiante. Al iniciar sesión por primera vez, deberás cambiarla por una diferente.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = colors.onSurfaceVariant
                                    )
                                }
                            }

                            Button(
                                onClick = { viewModel.guardarPerfil() },
                                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = FondoAccionPrincipal,
                                    contentColor = TextoAccionPrincipal
                                )
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
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    border = BorderStroke(1.dp, colors.outlineVariant),
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
                                color = colors.primaryContainer,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.AdminPanelSettings,
                                        contentDescription = null,
                                        tint = colors.onPrimaryContainer,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Acceso Administrativo UTS",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.onSurface
                                )
                                Text(
                                    text = "Dirección Académica y Financiera",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.onSurfaceVariant
                                )
                            }
                        }

                        HorizontalDivider(color = colors.outlineVariant)

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
                            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = FondoAccionPrincipal,
                                contentColor = TextoAccionPrincipal
                            )
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ingresar al Panel de Administrador", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.onCodigoCambiado("ADMIN")
                                viewModel.onPasswordCambiado("admin")
                                viewModel.iniciarSesion("ADMIN", "admin")
                            },
                            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, colors.primary),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.primary)
                        ) {
                            Text("Acceso Rápido Demo Administrador", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }

        IconButton(
            onClick = onAbrirAjustes,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 12.dp, end = 16.dp)
        ) {
            Icon(
                Icons.Default.Settings,
                contentDescription = "Ajustes de apariencia",
                tint = colors.onSurfaceVariant
            )
        }

        // Diálogo modal obligatorio de cambio de contraseña institucional
        CambioPasswordDialog(viewModel = viewModel)
    }
}
