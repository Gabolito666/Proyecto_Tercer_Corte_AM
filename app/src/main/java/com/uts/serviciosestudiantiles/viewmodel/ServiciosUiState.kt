package com.uts.serviciosestudiantiles.viewmodel

import com.uts.serviciosestudiantiles.data.LiquidacionMatricula
import com.uts.serviciosestudiantiles.data.PerfilEstudiante

data class ServiciosUiState(
    val campoNombre: String = "",
    val campoCodigo: String = "",
    val facultadSeleccionada: String = "Ingenierías",
    val menuFacultadAbierto: Boolean = false,
    val perfilGuardado: PerfilEstudiante? = null,
    val correoSimulado: String = "",
    val cursoSeleccionado: String = "Inglés Técnico",
    val menuCursoAbierto: Boolean = false,
    val tieneBeca: Boolean = false,
    val tieneIcetex: Boolean = false,
    val puedeLiquidar: Boolean = false,
    val listaLiquidaciones: List<LiquidacionMatricula> = emptyList(),
    val totalAcumulado: Double = 0.0,
    val mensajeMensajeEstado: String? = null,
    val esUsuarioExistente: Boolean = false,
    val esError: Boolean = false,
    val cursosDisponibles: Map<String, Double> = mapOf(
        "Inglés Técnico" to 300000.0,
        "Programación Web" to 450000.0,
        "Ciberseguridad" to 500000.0,
        "Diseño UX UI Avanzado" to 380000.0,
        "Inteligencia Artificial Aplicada" to 550000.0,
        "Marketing Digital y Analítica" to 320000.0,
        "Ciencia de Datos con Python" to 480000.0
    )
) {
    val mensajeEstado: String? get() = mensajeMensajeEstado
}
