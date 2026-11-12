package com.uts.serviciosestudiantiles

import com.uts.serviciosestudiantiles.viewmodel.ServiciosViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ServiciosViewModelTest {

    @Test
    fun testFiltradoPorEstudianteYTrazabilidad() {
        val viewModel = ServiciosViewModel()

        // 1. Guardar Estudiante 1 (Nuevo)
        viewModel.onNombreCambiado("Gabriel Prado")
        viewModel.onCodigoCambiado("10987654")
        viewModel.guardarPerfil()

        var state = viewModel.uiState.value
        assertFalse(state.esUsuarioExistente)
        assertEquals("Nuevo estudiante registrado: Perfil guardado con éxito", state.mensajeMensajeEstado)

        // Liquidar dos matrículas para Estudiante 1
        viewModel.onCursoSeleccionado("Inglés Técnico")
        viewModel.liquidarMatricula() // 300,000

        viewModel.onCursoSeleccionado("Ciberseguridad")
        viewModel.onBecaCambiada(true)
        viewModel.liquidarMatricula() // 500,000 * 0.5 = 250,000

        state = viewModel.uiState.value
        assertEquals(2, state.listaLiquidaciones.size)
        assertEquals(550000.0, state.totalAcumulado, 0.01)
        assertTrue(state.listaLiquidaciones.all { it.codigoEstudiante == "10987654" })

        // 2. Guardar Estudiante 2 (Nuevo)
        viewModel.onNombreCambiado("Ana Maria")
        viewModel.onCodigoCambiado("20987654")
        viewModel.guardarPerfil()

        state = viewModel.uiState.value
        assertFalse(state.esUsuarioExistente)
        // El historial para el Estudiante 2 debe estar vacío inmediatamente
        assertEquals(0, state.listaLiquidaciones.size)
        assertEquals(0.0, state.totalAcumulado, 0.01)

        // Liquidar una matrícula para Estudiante 2
        viewModel.onCursoSeleccionado("Programación Web")
        viewModel.liquidarMatricula() // 450,000

        state = viewModel.uiState.value
        assertEquals(1, state.listaLiquidaciones.size)
        assertEquals(450000.0, state.totalAcumulado, 0.01)
        assertEquals("20987654", state.listaLiquidaciones.first().codigoEstudiante)

        // 3. Volver a ingresar el código de Estudiante 1 (Existente -> Restauración)
        viewModel.onNombreCambiado("Gabriel P.")
        viewModel.onCodigoCambiado("10987654")
        viewModel.guardarPerfil()

        state = viewModel.uiState.value
        assertTrue(state.esUsuarioExistente)
        assertEquals("Sesión cargada: Datos e historial restaurados", state.mensajeMensajeEstado)
        // Debe recuperar el perfil guardado original y su historial completo
        assertEquals("Gabriel Prado", state.campoNombre)
        assertEquals("Gabriel Prado", state.perfilGuardado?.nombre)
        assertEquals(2, state.listaLiquidaciones.size)
        assertEquals(550000.0, state.totalAcumulado, 0.01)
        assertTrue(state.listaLiquidaciones.all { it.codigoEstudiante == "10987654" })
    }
}
