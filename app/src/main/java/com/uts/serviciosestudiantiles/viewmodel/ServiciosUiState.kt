package com.uts.serviciosestudiantiles.viewmodel

import com.uts.serviciosestudiantiles.data.Beca
import com.uts.serviciosestudiantiles.data.LiquidacionMatricula
import com.uts.serviciosestudiantiles.data.PerfilEstudiante

enum class ModoAuth {
    LOGIN,
    REGISTRO
}

enum class RolUsuario {
    ESTUDIANTE,
    ADMINISTRADOR
}

data class ServiciosUiState(
    val campoNombre: String = "",
    val campoCodigo: String = "",
    val campoPassword: String = "",
    val passwordVisible: Boolean = false,
    val rolAuthSeleccionado: RolUsuario = RolUsuario.ESTUDIANTE,
    val facultadSeleccionada: String = "Ingenierías",
    val menuFacultadAbierto: Boolean = false,
    val perfilGuardado: PerfilEstudiante? = null,
    val correoSimulado: String = "",
    val modoAuth: ModoAuth = ModoAuth.LOGIN,
    val esAdmin: Boolean = false,
    val cursoSeleccionado: String = "Inglés Técnico",
    val cursosSeleccionados: Set<String> = setOf("Inglés Técnico"),
    val menuCursoAbierto: Boolean = false,
    val tieneBeca: Boolean = false,
    val tieneIcetex: Boolean = false,
    val porcentajeBeca: Double = 0.50,
    val porcentajeIcetex: Double = 0.05,
    val becaHabilitada: Boolean = true,
    val icetexHabilitado: Boolean = true,
    val puedeLiquidar: Boolean = false,
    val becasDisponibles: List<Beca> = emptyList(),
    val becaSeleccionadaId: Int? = null,
    val listaLiquidaciones: List<LiquidacionMatricula> = emptyList(),
    val todasLasLiquidacionesAdmin: List<LiquidacionMatricula> = emptyList(),
    val todosLosPerfilesAdmin: List<PerfilEstudiante> = emptyList(),
    val totalAcumulado: Double = 0.0,
    val mensajeMensajeEstado: String? = null,
    val esUsuarioExistente: Boolean = false,
    val esError: Boolean = false,
    val mostrarDialogoCambioPassword: Boolean = false,
    val campoNuevoPassword: String = "",
    val campoConfirmarNuevoPassword: String = "",
    val nuevoPasswordVisible: Boolean = false,
    val confirmarPasswordVisible: Boolean = false,
    val errorCambioPassword: String? = null,
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
    val rolUsuario: RolUsuario
        get() = if (esAdmin) RolUsuario.ADMINISTRADOR else RolUsuario.ESTUDIANTE

    val mensajeEstado: String? get() = mensajeMensajeEstado

    // Cálculo matemático: el crédito del Icetex se aplica tras deducir los descuentos correspondientes
    val subtotalBase: Double
        get() = cursosSeleccionados.sumOf { cursosDisponibles[it] ?: 0.0 }

    val descuentoBeca: Double
        get() = if (tieneBeca && becaHabilitada) subtotalBase * porcentajeBeca else 0.0

    val baseConDescuento: Double
        get() = (subtotalBase - descuentoBeca).coerceAtLeast(0.0)

    val recargoIcetex: Double
        get() = if (tieneIcetex && icetexHabilitado) baseConDescuento * porcentajeIcetex else 0.0

    val totalCalculado: Double
        get() = baseConDescuento + recargoIcetex
}
