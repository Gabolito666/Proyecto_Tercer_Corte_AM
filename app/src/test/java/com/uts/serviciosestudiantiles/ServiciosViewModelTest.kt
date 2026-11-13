package com.uts.serviciosestudiantiles

import com.uts.serviciosestudiantiles.viewmodel.ServiciosViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ServiciosViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        com.uts.serviciosestudiantiles.data.ServiciosRepository.isTestMode = true
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        com.uts.serviciosestudiantiles.data.ServiciosRepository.isTestMode = false
    }

    @Test
    fun testFiltradoPorEstudianteYTrazabilidad() = runTest(testDispatcher) {
        val viewModel = ServiciosViewModel()
        advanceUntilIdle()

        // 1. Guardar Estudiante 1 (Nuevo)
        viewModel.onNombreCambiado("Gabriel Prado")
        viewModel.onCodigoCambiado("10987654")
        viewModel.guardarPerfil()
        advanceUntilIdle()

        var state = viewModel.uiState.value
        assertFalse(state.esUsuarioExistente)
        assertEquals("Nuevo estudiante registrado: Perfil guardado con éxito", state.mensajeMensajeEstado)

        // Liquidar dos matrículas para Estudiante 1
        viewModel.onCursoSeleccionado("Inglés Técnico")
        viewModel.liquidarMatricula() // 300,000
        advanceUntilIdle()

        viewModel.onCursoSeleccionado("Ciberseguridad")
        viewModel.onBecaCambiada(true)
        viewModel.liquidarMatricula() // 500,000 * 0.5 = 250,000
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertEquals(2, state.listaLiquidaciones.size)
        assertEquals(550000.0, state.totalAcumulado, 0.01)
        assertTrue(state.listaLiquidaciones.all { it.codigoEstudiante == "10987654" })

        // 2. Guardar Estudiante 2 (Nuevo)
        viewModel.onNombreCambiado("Ana Maria")
        viewModel.onCodigoCambiado("20987654")
        viewModel.guardarPerfil()
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertFalse(state.esUsuarioExistente)
        // El historial para el Estudiante 2 debe estar vacío inmediatamente
        assertEquals(0, state.listaLiquidaciones.size)
        assertEquals(0.0, state.totalAcumulado, 0.01)

        // Liquidar una matrícula para Estudiante 2
        viewModel.onCursoSeleccionado("Programación Web")
        viewModel.liquidarMatricula() // 450,000
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertEquals(1, state.listaLiquidaciones.size)
        assertEquals(450000.0, state.totalAcumulado, 0.01)
        assertEquals("20987654", state.listaLiquidaciones.first().codigoEstudiante)

        // 3. Intentar ingresar con el código de Estudiante 1 ya usado -> Debe salir error de código ocupado
        viewModel.onNombreCambiado("Gabriel P.")
        viewModel.onCodigoCambiado("10987654")
        viewModel.guardarPerfil()
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertTrue(state.esError)
        assertEquals("El código ya está ocupado", state.mensajeMensajeEstado)
        // No debe cambiar automáticamente los nombres por el usuario ya asociado con el código
        assertEquals("Gabriel P.", state.campoNombre)
        assertEquals("10987654", state.campoCodigo)
        assertFalse(state.puedeLiquidar)
    }
}
