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

    // Funciones de Módulo 1 (Gestión de Perfil)
    fun onNombreCambiado(nombre: String) {
        _uiState.update { it.copy(campoNombre = nombre) }
    }

    fun onCodigoCambiado(codigo: String) {
        _uiState.update { it.copy(campoCodigo = codigo) }
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

        if (nombre.isNotEmpty() && codigo.isNotEmpty()) {
            val nombreLimpio = nombre.replace(" ", "").lowercase()
            val codigoLimpio = codigo.replace(" ", "").lowercase()
            val correo = "$nombreLimpio.$codigoLimpio@uts.edu.co"

            val nuevoPerfil = PerfilEstudiante(
                id = 1,
                nombre = nombre,
                codigo = codigo,
                facultad = currentState.facultadSeleccionada,
                correoInstitucional = correo
            )

            viewModelScope.launch {
                runCatching {
                    repository.guardarPerfil(nuevoPerfil)
                }

                _uiState.update {
                    it.copy(
                        perfilGuardado = nuevoPerfil,
                        correoSimulado = correo,
                        puedeLiquidar = true
                    )
                }
            }
        }
    }

    fun buscarPerfilPorCodigo(codigo: String) {
        val codigoLimpio = codigo.trim()
        if (codigoLimpio.isEmpty()) return

        viewModelScope.launch {
            val perfilEncontrado = repository.buscarPerfilPorCodigo(codigoLimpio)
            if (perfilEncontrado != null) {
                _uiState.update {
                    it.copy(
                        campoNombre = perfilEncontrado.nombre,
                        campoCodigo = perfilEncontrado.codigo,
                        facultadSeleccionada = perfilEncontrado.facultad,
                        perfilGuardado = perfilEncontrado,
                        correoSimulado = perfilEncontrado.correoInstitucional,
                        puedeLiquidar = true
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
        if (!currentState.puedeLiquidar || currentState.perfilGuardado == null) {
            return
        }

        val codigoEstudiante = currentState.perfilGuardado.codigo
        val costoBase = cursosConCostos[currentState.cursoSeleccionado] ?: 0.0
        val descuento = if (currentState.tieneBeca) costoBase * 0.5 else 0.0
        val recargo = if (currentState.tieneIcetex) costoBase * 0.05 else 0.0
        val totalAPagar = costoBase - descuento + recargo

        val liquidacionesActuales = currentState.listaLiquidaciones
        val nuevoId = (liquidacionesActuales.maxOfOrNull { it.id } ?: 0) + 1

        val nuevaLiquidacion = LiquidacionMatricula(
            id = nuevoId,
            codigoEstudiante = codigoEstudiante,
            cursoLibre = currentState.cursoSeleccionado,
            costoBase = costoBase,
            tieneBeca = currentState.tieneBeca,
            tieneIcetex = currentState.tieneIcetex,
            totalAPagar = totalAPagar
        )

        viewModelScope.launch {
            runCatching {
                repository.agregarLiquidacion(nuevaLiquidacion)
            }

            val listaActualizada = runCatching {
                val remotas = repository.obtenerLiquidacionesPorCodigo(codigoEstudiante)
                if (remotas.isNotEmpty()) remotas else (liquidacionesActuales + nuevaLiquidacion)
            }.getOrElse {
                liquidacionesActuales + nuevaLiquidacion
            }
            val nuevoTotalAcumulado = listaActualizada.sumOf { it.totalAPagar }

            _uiState.update {
                it.copy(
                    listaLiquidaciones = listaActualizada,
                    totalAcumulado = nuevoTotalAcumulado,
                    tieneBeca = false,
                    tieneIcetex = false
                )
            }
        }
    }

    fun obtenerLiquidacionesPorCodigo(codigo: String) {
        val codigoLimpio = codigo.trim()
        if (codigoLimpio.isEmpty()) return

        viewModelScope.launch {
            val remotas = repository.obtenerLiquidacionesPorCodigo(codigoLimpio)
            _uiState.update {
                it.copy(
                    listaLiquidaciones = remotas,
                    totalAcumulado = remotas.sumOf { it.totalAPagar }
                )
            }
        }
    }
}
