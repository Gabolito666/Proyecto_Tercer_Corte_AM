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
    val totalAcumulado: Double = 0.0
)
