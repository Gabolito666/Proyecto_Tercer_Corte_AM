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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uts.serviciosestudiantiles.ui.theme.BlancoSuperficie
import com.uts.serviciosestudiantiles.ui.theme.CremaFondo
import com.uts.serviciosestudiantiles.ui.theme.GrisPlata
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
fun AdminScreen(
    viewModel: ServiciosViewModel,
    seccionInicial: Int = 0,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedSection by rememberSaveable { mutableIntStateOf(seccionInicial) }

    LaunchedEffect(seccionInicial) {
        selectedSection = seccionInicial
    }

    LaunchedEffect(Unit) {
        viewModel.cargarDatosAdmin()
        viewModel.cargarBecasRemotas()
    }

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CO")).apply {
        maximumFractionDigits = 0
    }

    val sections = listOf("Gestión de Becas", "Liquidaciones Globales", "Cursos Libres", "Estudiantes")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CremaFondo)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Administrador
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = VinoTintoOleo,
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = VerdeOpalo,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Panel de Administración UTS",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = NegroTexto
                    )
                    Text(
                        text = "Gestión institucional de becas, cursos y liquidaciones",
                        style = MaterialTheme.typography.bodySmall,
                        color = GrisTextoSecundario
                    )
                }
            }

            // Sub-pestañas de control administrativo
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = BlancoSuperficie,
                border = BorderStroke(1.dp, GrisPlata)
            ) {
                SecondaryTabRow(
                    selectedTabIndex = selectedSection,
                    containerColor = BlancoSuperficie,
                    contentColor = VinoTintoOleo,
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(selectedSection),
                            height = 3.dp,
                            color = VinoTintoOleo
                        )
                    },
                    divider = {}
                ) {
                    sections.forEachIndexed { index, title ->
                        val isSelected = selectedSection == index
                        Tab(
                            selected = isSelected,
                            onClick = { selectedSection = index },
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) VinoTintoOleo else GrisTextoSecundario,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        )
                    }
                }
            }

            // Contenido dinámico según la sección seleccionada
            when (selectedSection) {
                0 -> SeccionGestionBecas(viewModel)
                1 -> SeccionTodasLasLiquidaciones(state, currencyFormat)
                2 -> SeccionGestionCursos(viewModel, currencyFormat)
                3 -> SeccionListaEstudiantes(state)
            }
        }
    }
}

// --- SUB-MÓDULO: CREACIÓN Y GESTIÓN DE BECAS INSTITUCIONALES ---
@Composable
private fun SeccionGestionBecas(
    viewModel: ServiciosViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var nombreBeca by remember { mutableStateOf("") }
    var requisitosBeca by remember { mutableStateOf("") }
    var porcentajeBecaSlider by remember { mutableIntStateOf(50) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Tarjeta para Crear Nueva Beca
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
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CardGiftcard,
                        contentDescription = null,
                        tint = VinoTintoOleo,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Crear Nueva Beca Institucional",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = NegroTexto
                    )
                }

                Text(
                    text = "Define programas de descuento y estímulo económico para estudiantes UTS.",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrisTextoSecundario
                )

                HorizontalDivider(color = GrisPlata)

                OutlinedTextField(
                    value = nombreBeca,
                    onValueChange = { nombreBeca = it },
                    label = { Text("Nombre de la Beca") },
                    placeholder = { Text("Ej. Beca Talento Santander") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                OutlinedTextField(
                    value = requisitosBeca,
                    onValueChange = { requisitosBeca = it },
                    label = { Text("Requisitos Académicos") },
                    placeholder = { Text("Ej. Promedio ponderado mínimo 4.2 y estrato 1 o 2") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                // Slider interactivo de porcentaje de descuento
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Porcentaje de Descuento:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = NegroTexto
                        )
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = VerdeOpaloClaro
                        ) {
                            Text(
                                text = "$porcentajeBecaSlider%",
                                fontWeight = FontWeight.ExtraBold,
                                color = NegroTexto,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Slider(
                        value = porcentajeBecaSlider.toFloat(),
                        onValueChange = { porcentajeBecaSlider = it.toInt() },
                        valueRange = 5f..100f,
                        steps = 18,
                        colors = SliderDefaults.colors(
                            thumbColor = VerdeOpalo,
                            activeTrackColor = VerdeOpalo
                        )
                    )
                }

                Button(
                    onClick = {
                        if (nombreBeca.isNotBlank()) {
                            viewModel.crearBeca(
                                nombre = nombreBeca,
                                porcentaje = porcentajeBecaSlider / 100.0,
                                requisitos = requisitosBeca
                            )
                            nombreBeca = ""
                            requisitosBeca = ""
                            porcentajeBecaSlider = 50
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = VinoTintoOleo)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = VerdeOpalo)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Crear y Guardar Beca en Supabase", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Listado de Becas Registradas
        Text(
            text = "Becas Institucionales Disponibles (${state.becasDisponibles.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = NegroTexto
        )

        if (state.becasDisponibles.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
                border = BorderStroke(1.dp, GrisPlata)
            ) {
                Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                    Text("No hay becas registradas todavía.", color = GrisTextoSecundario)
                }
            }
        } else {
            state.becasDisponibles.forEach { beca ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
                    border = BorderStroke(1.dp, GrisPlata)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = beca.nombre,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = NegroTexto
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = VerdeOpaloClaro
                                ) {
                                    Text(
                                        text = "${(beca.porcentajeDescuento * 100).toInt()}% DESC",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = NegroTexto,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Requisitos: ${beca.requisitos}",
                                style = MaterialTheme.typography.bodySmall,
                                color = GrisTextoSecundario
                            )
                        }

                        IconButton(
                            onClick = { viewModel.eliminarBeca(beca.id) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar beca",
                                tint = MagentaOleo
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- SUB-MÓDULO: GESTIÓN DE CURSOS LIBRES ---
@Composable
private fun SeccionGestionCursos(
    viewModel: ServiciosViewModel,
    currencyFormat: NumberFormat
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var nuevoCursoNombre by remember { mutableStateOf("") }
    var nuevoCursoCosto by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = VinoTintoOleo)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Agregar Nuevo Curso Libre",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = NegroTexto
                    )
                }

                OutlinedTextField(
                    value = nuevoCursoNombre,
                    onValueChange = { nuevoCursoNombre = it },
                    label = { Text("Nombre del Curso") },
                    placeholder = { Text("Ej. Desarrollo con Flutter") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                OutlinedTextField(
                    value = nuevoCursoCosto,
                    onValueChange = { nuevoCursoCosto = it.filter { c -> c.isDigit() } },
                    label = { Text("Tarifa Base en COP") },
                    placeholder = { Text("Ej. 420000") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )

                Button(
                    onClick = {
                        val costo = nuevoCursoCosto.toDoubleOrNull() ?: 0.0
                        if (nuevoCursoNombre.isNotBlank() && costo > 0.0) {
                            viewModel.agregarCursoLibre(nuevoCursoNombre, costo)
                            nuevoCursoNombre = ""
                            nuevoCursoCosto = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = VinoTintoOleo)
                ) {
                    Text("Publicar Curso en Oferta", fontWeight = FontWeight.Bold)
                }
            }
        }

        Text(
            text = "Cursos Ofertados (${state.cursosDisponibles.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = NegroTexto
        )

        state.cursosDisponibles.forEach { (nombre, costo) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
                border = BorderStroke(1.dp, GrisPlata)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = nombre,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = NegroTexto
                        )
                        Text(
                            text = "Tarifa base: ${currencyFormat.format(costo)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = VinoTintoOleo,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    IconButton(
                        onClick = { viewModel.eliminarCursoLibre(nombre) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar curso",
                            tint = MagentaOleo
                        )
                    }
                }
            }
        }
    }
}

// --- SUB-MÓDULO: LISTA DE ESTUDIANTES REGISTRADOS ---
@Composable
private fun SeccionListaEstudiantes(
    state: com.uts.serviciosestudiantiles.viewmodel.ServiciosUiState
) {
    if (state.todosLosPerfilesAdmin.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay estudiantes registrados aún en la base de datos.", color = GrisTextoSecundario)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(state.todosLosPerfilesAdmin) { estudiante ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
                    border = BorderStroke(1.dp, GrisPlata)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = estudiante.nombre,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = NegroTexto
                            )
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = VerdeOpaloClaro
                            ) {
                                Text(
                                    text = "Cód: ${estudiante.codigo}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = NegroTexto,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = "Facultad: ${estudiante.facultad}",
                            style = MaterialTheme.typography.bodySmall,
                            color = GrisTextoSecundario
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = VinoTintoOleo, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = estudiante.correoInstitucional,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = VinoTintoOleo
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- SUB-MÓDULO: LIQUIDACIONES GLOBALES ---
@Composable
private fun SeccionTodasLasLiquidaciones(
    state: com.uts.serviciosestudiantiles.viewmodel.ServiciosUiState,
    currencyFormat: NumberFormat
) {
    val totalGlobal = state.todasLasLiquidacionesAdmin.sumOf { it.totalAPagar }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Resumen institucional
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = VinoTintoOleo)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Recaudación Total Institucional UTS",
                    style = MaterialTheme.typography.labelMedium,
                    color = BlancoSuperficie.copy(alpha = 0.8f)
                )
                Text(
                    text = currencyFormat.format(totalGlobal),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = BlancoSuperficie
                )
                Text(
                    text = "${state.todasLasLiquidacionesAdmin.size} transacciones registradas en Supabase",
                    style = MaterialTheme.typography.bodySmall,
                    color = VerdeOpalo
                )
            }
        }

        if (state.todasLasLiquidacionesAdmin.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay liquidaciones registradas en el sistema.", color = GrisTextoSecundario)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.todasLasLiquidacionesAdmin) { liq ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = BlancoSuperficie),
                        border = BorderStroke(1.dp, GrisPlata)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Estudiante: ${liq.codigoEstudiante}",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = NegroTexto
                                )
                                Text(
                                    text = currencyFormat.format(liq.totalAPagar),
                                    fontWeight = FontWeight.ExtraBold,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = VinoTintoOleo
                                )
                            }
                            Text(
                                text = "Cursos: ${liq.cursoLibre}",
                                style = MaterialTheme.typography.bodySmall,
                                color = GrisTextoSecundario
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                if (liq.tieneBeca) {
                                    val becaNombre = state.becasDisponibles.firstOrNull { it.id == liq.becaId }?.nombre ?: "Beca"
                                    Surface(shape = RoundedCornerShape(50), color = VerdeOpaloClaro) {
                                        Text(becaNombre, color = NegroTexto, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                if (liq.tieneIcetex) {
                                    Surface(shape = RoundedCornerShape(50), color = MagentaOleoClaro) {
                                        Text("Icetex", color = MagentaOleo, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
