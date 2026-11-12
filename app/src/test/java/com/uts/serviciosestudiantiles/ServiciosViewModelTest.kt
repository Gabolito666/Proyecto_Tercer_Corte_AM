package com.uts.serviciosestudiantiles

import com.uts.serviciosestudiantiles.viewmodel.ServiciosViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ServiciosViewModelTest {

    @Test
    fun testFiltradoPorEstudianteYTrazabilidad() {
        val viewModel = ServiciosViewModel()

        // 1. Guardar Estudiante 1
        viewModel.onNombreCambiado("Gabriel Prado")
        viewModel.onCodigoCambiado("10987654")
        viewModel.guardarPerfil()

        // Liquidar dos matrículas para Estudiante 1
        viewModel.onCursoSeleccionado("Inglés Técnico")
        viewModel.liquidarMatricula() // 300,000

        viewModel.onCursoSeleccionado("Ciberseguridad")
        viewModel.onBecaCambiada(true)
        viewModel.liquidarMatricula() // 500,000 * 0.5 = 250,000

        var state = viewModel.uiState.value
        assertEquals(2, state.listaLiquidaciones.size)
        assertEquals(550000.0, state.totalAcumulado, 0.01)
        assertTrue(state.listaLiquidaciones.all { it.codigoEstudiante == "10987654" })

        // 2. Guardar Estudiante 2
        viewModel.onNombreCambiado("Ana Maria")
        viewModel.onCodigoCambiado("20987654")
        viewModel.guardarPerfil()

        // El historial para el Estudiante 2 debe estar vacío inmediatamente
        state = viewModel.uiState.value
        assertEquals(0, state.listaLiquidaciones.size)
        assertEquals(0.0, state.totalAcumulado, 0.01)

        // Liquidar una matrícula para Estudiante 2
        viewModel.onCursoSeleccionado("Programación Web")
        viewModel.liquidarMatricula() // 450,000

        state = viewModel.uiState.value
        assertEquals(1, state.listaLiquidaciones.size)
        assertEquals(450000.0, state.totalAcumulado, 0.01)
        assertEquals("20987654", state.listaLiquidaciones.first().codigoEstudiante)

        // 3. Volver a cambiar al Estudiante 1 (guardarPerfil)
        viewModel.onNombreCambiado("Gabriel Prado")
        viewModel.onCodigoCambiado("10987654")
        viewModel.guardarPerfil()

        state = viewModel.uiState.value
        // Debe recuperar solo las liquidaciones del Estudiante 1
        assertEquals(2, state.listaLiquidaciones.size)
        assertEquals(550000.0, state.totalAcumulado, 0.01)
        assertTrue(state.listaLiquidaciones.all { it.codigoEstudiante == "10987654" })
    }
}
