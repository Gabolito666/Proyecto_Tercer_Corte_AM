package com.uts.serviciosestudiantiles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uts.serviciosestudiantiles.data.LiquidacionMatricula
import com.uts.serviciosestudiantiles.data.PerfilEstudiante
import com.uts.serviciosestudiantiles.data.ServiciosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServiciosViewModel(
    private val repository: ServiciosRepository = ServiciosRepository()
) : ViewModel() {

    val facultades: List<String> = listOf("Ingenierías", "Ciencias Socioeconómicas")

    val cursosConCostos: Map<String, Double> = mapOf(
        "Inglés Técnico" to 300000.0,
        "Programación Web" to 450000.0,
        "Ciberseguridad" to 500000.0,
        "Diseño UX UI Avanzado" to 380000.0,
        "Inteligencia Artificial Aplicada" to 550000.0,
        "Marketing Digital y Analítica" to 320000.0,
        "Ciencia de Datos con Python" to 480000.0
    )

    private val _uiState = MutableStateFlow(
        ServiciosUiState(
            cursoSeleccionado = "Inglés Técnico",
            cursosDisponibles = cursosConCostos
        )
    )
    val uiState: StateFlow<ServiciosUiState> = _uiState.asStateFlow()

    init {
        cargarDatosPersistidos()
    }

    fun cargarDatosPersistidos() {
        val ultimoPerfil = repository.obtenerPerfil()
        if (ultimoPerfil != null) {
            viewModelScope.launch {
                val liquidaciones = repository.obtenerLiquidacionesPorCodigo(ultimoPerfil.codigo)
                val total = liquidaciones.sumOf { it.totalAPagar }
                _uiState.update {
                    it.copy(
                        campoNombre = ultimoPerfil.nombre,
                        campoCodigo = ultimoPerfil.codigo,
                        facultadSeleccionada = ultimoPerfil.facultad,
                        perfilGuardado = ultimoPerfil,
                        correoSimulado = ultimoPerfil.correoInstitucional,
                        puedeLiquidar = true,
                        esUsuarioExistente = true,
                        mensajeMensajeEstado = "Sesión cargada: Datos e historial restaurados",
                        listaLiquidaciones = liquidaciones,
                        totalAcumulado = total
                    )
                }
            }
        }
    }

    // Funciones de Módulo 1 (Gestión de Perfil)
    fun onNombreCambiado(nombre: String) {
        _uiState.update { it.copy(campoNombre = nombre, mensajeMensajeEstado = null) }
    }

    fun onCodigoCambiado(codigo: String) {
        _uiState.update { it.copy(campoCodigo = codigo, mensajeMensajeEstado = null) }
    }

    fun onFacultadSeleccionada(facultad: String) {
        _uiState.update {
            it.copy(
                facultadSeleccionada = facultad,
                menuFacultadAbierto = false
            )
        }
    }

    fun toggleMenuFacultad(abierto: Boolean) {
        _uiState.update { it.copy(menuFacultadAbierto = abierto) }
    }

    fun guardarPerfil() {
        val currentState = _uiState.value
        val nombre = currentState.campoNombre.trim()
        val codigo = currentState.campoCodigo.trim()

        if (nombre.isEmpty() || codigo.isEmpty()) {
            return
        }

        viewModelScope.launch {
            // Validación de unicidad de código de estudiante en Supabase / local
            val perfilExistente = repository.buscarPerfilPorCodigo(codigo)

            if (perfilExistente != null) {
                // El código YA existe en la nube o almacenamiento local
                val liquidacionesEstudiante = repository.obtenerLiquidacionesPorCodigo(codigo)
                val totalEstudiante = liquidacionesEstudiante.sumOf { it.totalAPagar }

                _uiState.update {
                    it.copy(
                        campoNombre = perfilExistente.nombre,
                        campoCodigo = perfilExistente.codigo,
                        facultadSeleccionada = perfilExistente.facultad,
                        perfilGuardado = perfilExistente,
                        correoSimulado = perfilExistente.correoInstitucional,
                        puedeLiquidar = true,
                        esUsuarioExistente = true,
                        mensajeMensajeEstado = "Sesión cargada: Datos e historial restaurados",
                        listaLiquidaciones = liquidacionesEstudiante,
                        totalAcumulado = totalEstudiante
                    )
                }
            } else {
                // El código NO existe: registrar nuevo perfil y persistir en Supabase
                val nombreLimpio = nombre.replace(" ", "").lowercase()
                val codigoLimpio = codigo.replace(" ", "").lowercase()
                val correo = "$nombreLimpio.$codigoLimpio@uts.edu.co"

                val nuevoPerfil = PerfilEstudiante(
                    id = (repository.obtenerTodosLosPerfiles().maxOfOrNull { it.id } ?: 0) + 1,
                    nombre = nombre,
                    codigo = codigo,
                    facultad = currentState.facultadSeleccionada,
                    correoInstitucional = correo
                )

                repository.guardarPerfil(nuevoPerfil)

                val liquidacionesEstudiante = repository.obtenerLiquidacionesPorCodigo(codigo)
                val totalEstudiante = liquidacionesEstudiante.sumOf { it.totalAPagar }

                _uiState.update {
                    it.copy(
                        perfilGuardado = nuevoPerfil,
                        correoSimulado = correo,
                        puedeLiquidar = true,
                        esUsuarioExistente = false,
                        mensajeMensajeEstado = "Nuevo estudiante registrado: Perfil guardado con éxito",
                        listaLiquidaciones = liquidacionesEstudiante,
                        totalAcumulado = totalEstudiante
                    )
                }
            }
        }
    }

    // Funciones de Módulo 2 (Liquidación de Matrícula)
    fun onCursoSeleccionado(curso: String) {
        _uiState.update {
            it.copy(
                cursoSeleccionado = curso,
                menuCursoAbierto = false
            )
        }
    }

    fun toggleMenuCurso(abierto: Boolean) {
        _uiState.update { it.copy(menuCursoAbierto = abierto) }
    }

    fun onBecaCambiada(tieneBeca: Boolean) {
        _uiState.update { it.copy(tieneBeca = tieneBeca) }
    }

    fun onIcetexCambiado(tieneIcetex: Boolean) {
        _uiState.update { it.copy(tieneIcetex = tieneIcetex) }
    }

    fun liquidarMatricula() {
        val currentState = _uiState.value
        val perfilActual = currentState.perfilGuardado ?: return
        if (!currentState.puedeLiquidar) {
            return
        }

        val costoBase = cursosConCostos[currentState.cursoSeleccionado] ?: 0.0
        val descuento = if (currentState.tieneBeca) costoBase * 0.5 else 0.0
        val recargo = if (currentState.tieneIcetex) costoBase * 0.05 else 0.0
        val totalAPagar = costoBase - descuento + recargo

        val liquidacionesActuales = currentState.listaLiquidaciones
        val todasLasLiquidaciones = repository.obtenerLiquidaciones()
        val nuevoId = (todasLasLiquidaciones.maxOfOrNull { it.id } ?: liquidacionesActuales.maxOfOrNull { it.id } ?: 0) + 1

        val nuevaLiquidacion = LiquidacionMatricula(
            id = nuevoId,
            codigoEstudiante = perfilActual.codigo,
            cursoLibre = currentState.cursoSeleccionado,
            costoBase = costoBase,
            tieneBeca = currentState.tieneBeca,
            tieneIcetex = currentState.tieneIcetex,
            totalAPagar = totalAPagar
        )

        viewModelScope.launch {
            // Guardado asíncrono en Supabase
            repository.guardarLiquidacion(nuevaLiquidacion)

            val listaActualizada = repository.obtenerLiquidacionesPorCodigo(perfilActual.codigo)
            val listaFinal = if (listaActualizada.isNotEmpty()) listaActualizada else (liquidacionesActuales + nuevaLiquidacion)
            val nuevoTotalAcumulado = listaFinal.sumOf { it.totalAPagar }

            _uiState.update {
                it.copy(
                    listaLiquidaciones = listaFinal,
                    totalAcumulado = nuevoTotalAcumulado,
                    tieneBeca = false,
                    tieneIcetex = false
                )
            }
        }
    }

    fun actualizarHistorialPorEstudiante(codigoEstudiante: String? = _uiState.value.perfilGuardado?.codigo) {
        if (codigoEstudiante.isNullOrBlank()) {
            _uiState.update {
                it.copy(
                    listaLiquidaciones = emptyList(),
                    totalAcumulado = 0.0
                )
            }
            return
        }

        viewModelScope.launch {
            val liquidacionesFiltradas = repository.obtenerLiquidacionesPorCodigo(codigoEstudiante)
            val nuevoTotal = liquidacionesFiltradas.sumOf { it.totalAPagar }

            _uiState.update {
                it.copy(
                    listaLiquidaciones = liquidacionesFiltradas,
                    totalAcumulado = nuevoTotal
                )
            }
        }
    }
}
