package com.uts.serviciosestudiantiles.viewmodel

import androidx.lifecycle.ViewModel
import com.uts.serviciosestudiantiles.data.LiquidacionMatricula
import com.uts.serviciosestudiantiles.data.PerfilEstudiante
import com.uts.serviciosestudiantiles.data.ServiciosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ServiciosViewModel(
    private val repository: ServiciosRepository = ServiciosRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServiciosUiState())
    val uiState: StateFlow<ServiciosUiState> = _uiState.asStateFlow()

    val facultades: List<String> = listOf("Ingenierías", "Ciencias Socioeconómicas")

    val cursosConCostos: Map<String, Double> = mapOf(
        "Inglés Técnico" to 300000.0,
        "Programación Web" to 450000.0,
        "Ciberseguridad" to 500000.0
    )

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

            repository.guardarPerfil(nuevoPerfil)

            _uiState.update {
                it.copy(
                    perfilGuardado = nuevoPerfil,
                    correoSimulado = correo,
                    puedeLiquidar = true
                )
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

        val costoBase = cursosConCostos[currentState.cursoSeleccionado] ?: 0.0
        val descuento = if (currentState.tieneBeca) costoBase * 0.5 else 0.0
        val recargo = if (currentState.tieneIcetex) costoBase * 0.05 else 0.0
        val totalAPagar = costoBase - descuento + recargo

        val liquidacionesActuales = repository.obtenerLiquidaciones()
        val nuevoId = (liquidacionesActuales.maxOfOrNull { it.id } ?: 0) + 1

        val nuevaLiquidacion = LiquidacionMatricula(
            id = nuevoId,
            cursoLibre = currentState.cursoSeleccionado,
            costoBase = costoBase,
            tieneBeca = currentState.tieneBeca,
            tieneIcetex = currentState.tieneIcetex,
            totalAPagar = totalAPagar
        )

        repository.agregarLiquidacion(nuevaLiquidacion)
        val listaActualizada = repository.obtenerLiquidaciones()
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
