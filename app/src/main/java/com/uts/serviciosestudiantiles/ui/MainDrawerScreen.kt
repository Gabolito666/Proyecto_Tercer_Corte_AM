package com.uts.serviciosestudiantiles.ui

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uts.serviciosestudiantiles.ui.theme.BlancoSuperficie
import com.uts.serviciosestudiantiles.ui.theme.CremaFondo
import com.uts.serviciosestudiantiles.ui.theme.GrisPlata
import com.uts.serviciosestudiantiles.ui.theme.GrisTextoSecundario
import com.uts.serviciosestudiantiles.ui.theme.NegroTexto
import com.uts.serviciosestudiantiles.ui.theme.VerdeOpalo
import com.uts.serviciosestudiantiles.ui.theme.VerdeOpaloClaro
import com.uts.serviciosestudiantiles.ui.theme.VinoTintoOleo
import com.uts.serviciosestudiantiles.viewmodel.ServiciosViewModel
import kotlinx.coroutines.launch

enum class DestinoDrawer(
    val titulo: String,
    val icono: ImageVector
) {
    PERFIL("Mi Perfil", Icons.Default.Person),
    MATRICULA("Matricular Cursos", Icons.Default.Receipt),
    HISTORIAL("Mi Historial Financiero", Icons.Default.History),
    ADMIN_PANEL("Panel de Administración", Icons.Default.AdminPanelSettings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDrawerScreen(
    viewModel: ServiciosViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // 1) PANTALLA DEDICADA DE AUTENTICACIÓN: Se muestra si no hay sesión activa
    if (state.perfilGuardado == null) {
        AuthScreen(viewModel = viewModel)
        return
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    var destinoActual by rememberSaveable { mutableStateOf(DestinoDrawer.PERFIL) }
    var subseccionAdmin by rememberSaveable { mutableIntStateOf(0) }

    // Segregación estricta de roles: Redirigir si el rol cambia y la pantalla no le corresponde
    LaunchedEffect(state.esAdmin) {
        if (state.esAdmin) {
            if (destinoActual == DestinoDrawer.MATRICULA || destinoActual == DestinoDrawer.HISTORIAL) {
                destinoActual = DestinoDrawer.ADMIN_PANEL
            }
        } else {
            if (destinoActual == DestinoDrawer.ADMIN_PANEL) {
                destinoActual = DestinoDrawer.PERFIL
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(320.dp),
                drawerContainerColor = BlancoSuperficie
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .padding(16.dp)
                ) {
                    // Cabecera del Drawer con perfil institucional activo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (state.esAdmin) VinoTintoOleo else VerdeOpaloClaro,
                            modifier = Modifier.size(50.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (state.esAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                                    contentDescription = null,
                                    tint = if (state.esAdmin) VerdeOpalo else VinoTintoOleo,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = state.perfilGuardado?.nombre ?: "Usuario UTS",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = NegroTexto,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = if (state.esAdmin) VinoTintoOleo else VerdeOpaloClaro
                            ) {
                                Text(
                                    text = if (state.esAdmin) "ADMINISTRADOR" else "ESTUDIANTE",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (state.esAdmin) BlancoSuperficie else NegroTexto,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = state.perfilGuardado?.correoInstitucional ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = VinoTintoOleo,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = state.perfilGuardado?.facultad ?: "",
                        style = MaterialTheme.typography.labelSmall,
                        color = GrisTextoSecundario,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = GrisPlata)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Opciones de menú según el rol activo
                    if (!state.esAdmin) {
                        // --- MENÚ ESTUDIANTE ---
                        NavigationDrawerItem(
                            label = { Text("Mi Perfil", fontWeight = FontWeight.Bold) },
                            icon = { Icon(Icons.Default.Person, contentDescription = null) },
                            selected = destinoActual == DestinoDrawer.PERFIL,
                            onClick = {
                                destinoActual = DestinoDrawer.PERFIL
                                coroutineScope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = CremaFondo,
                                selectedIconColor = VinoTintoOleo,
                                selectedTextColor = VinoTintoOleo
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        NavigationDrawerItem(
                            label = { Text("Matricular Cursos", fontWeight = FontWeight.Bold) },
                            icon = { Icon(Icons.Default.Receipt, contentDescription = null) },
                            selected = destinoActual == DestinoDrawer.MATRICULA,
                            onClick = {
                                destinoActual = DestinoDrawer.MATRICULA
                                coroutineScope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = CremaFondo,
                                selectedIconColor = VinoTintoOleo,
                                selectedTextColor = VinoTintoOleo
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        NavigationDrawerItem(
                            label = { Text("Mi Historial Financiero", fontWeight = FontWeight.Bold) },
                            icon = { Icon(Icons.Default.History, contentDescription = null) },
                            selected = destinoActual == DestinoDrawer.HISTORIAL,
                            onClick = {
                                destinoActual = DestinoDrawer.HISTORIAL
                                coroutineScope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = CremaFondo,
                                selectedIconColor = VinoTintoOleo,
                                selectedTextColor = VinoTintoOleo
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )
                    } else {
                        // --- MENÚ ADMINISTRADOR ---
                        NavigationDrawerItem(
                            label = { Text("Mi Perfil", fontWeight = FontWeight.Bold) },
                            icon = { Icon(Icons.Default.Person, contentDescription = null) },
                            selected = destinoActual == DestinoDrawer.PERFIL,
                            onClick = {
                                destinoActual = DestinoDrawer.PERFIL
                                coroutineScope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = CremaFondo,
                                selectedIconColor = VinoTintoOleo,
                                selectedTextColor = VinoTintoOleo
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        NavigationDrawerItem(
                            label = { Text("Panel de Administración", fontWeight = FontWeight.Bold) },
                            icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = null) },
                            selected = destinoActual == DestinoDrawer.ADMIN_PANEL,
                            onClick = {
                                destinoActual = DestinoDrawer.ADMIN_PANEL
                                coroutineScope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = CremaFondo,
                                selectedIconColor = VinoTintoOleo,
                                selectedTextColor = VinoTintoOleo
                            ),
                            shape = RoundedCornerShape(14.dp)
                        )

                        // Submenús específicos de administración
                        Column(modifier = Modifier.padding(start = 24.dp, top = 6.dp)) {
                            NavigationDrawerItem(
                                label = { Text("↳ Creación de Becas", style = MaterialTheme.typography.bodyMedium) },
                                icon = { Icon(Icons.Default.CardGiftcard, contentDescription = null, modifier = Modifier.size(18.dp)) },
                                selected = destinoActual == DestinoDrawer.ADMIN_PANEL && subseccionAdmin == 0,
                                onClick = {
                                    subseccionAdmin = 0
                                    destinoActual = DestinoDrawer.ADMIN_PANEL
                                    coroutineScope.launch { drawerState.close() }
                                },
                                shape = RoundedCornerShape(12.dp)
                            )
                            NavigationDrawerItem(
                                label = { Text("↳ Liquidaciones Globales", style = MaterialTheme.typography.bodyMedium) },
                                icon = { Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(18.dp)) },
                                selected = destinoActual == DestinoDrawer.ADMIN_PANEL && subseccionAdmin == 1,
                                onClick = {
                                    subseccionAdmin = 1
                                    destinoActual = DestinoDrawer.ADMIN_PANEL
                                    coroutineScope.launch { drawerState.close() }
                                },
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    HorizontalDivider(color = GrisPlata)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Botón para Cerrar Sesión en la parte inferior del Drawer
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = "Cerrar Sesión",
                                color = VinoTintoOleo,
                                fontWeight = FontWeight.ExtraBold
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = null,
                                tint = VinoTintoOleo
                            )
                        },
                        selected = false,
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                                viewModel.cerrarSesion()
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = VinoTintoOleo.copy(alpha = 0.08f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .background(CremaFondo),
            containerColor = CremaFondo,
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = VinoTintoOleo,
                                modifier = Modifier.size(34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = "Logo UTS",
                                        tint = VerdeOpalo,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = when (destinoActual) {
                                        DestinoDrawer.PERFIL -> "Mi Perfil"
                                        DestinoDrawer.MATRICULA -> "Matrícula de Cursos"
                                        DestinoDrawer.HISTORIAL -> "Historial Financiero"
                                        DestinoDrawer.ADMIN_PANEL -> "Panel Administrador"
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = NegroTexto,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (state.esAdmin) "UTS • Rol: Administrador" else "UTS • Rol: Estudiante",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = VinoTintoOleo,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú lateral deslizable",
                                tint = VinoTintoOleo
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = CremaFondo),
                    modifier = Modifier.statusBarsPadding()
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .navigationBarsPadding()
            ) {
                when (destinoActual) {
                    DestinoDrawer.PERFIL -> {
                        PerfilScreen(
                            viewModel = viewModel,
                            onNavegarAdmin = {
                                destinoActual = DestinoDrawer.ADMIN_PANEL
                            }
                        )
                    }
                    DestinoDrawer.MATRICULA -> {
                        MatriculaScreen(viewModel = viewModel)
                    }
                    DestinoDrawer.HISTORIAL -> {
                        HistorialScreen(viewModel = viewModel)
                    }
                    DestinoDrawer.ADMIN_PANEL -> {
                        AdminScreen(
                            viewModel = viewModel,
                            seccionInicial = subseccionAdmin
                        )
                    }
                }
            }
        }

        // Diálogo obligatorio o voluntario de cambio de contraseña institucional
        CambioPasswordDialog(viewModel = viewModel)
    }
}
