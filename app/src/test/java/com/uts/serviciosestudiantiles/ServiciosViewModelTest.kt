package com.uts.serviciosestudiantiles

import com.uts.serviciosestudiantiles.viewmodel.ModoAuth
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
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
        assertEquals("Gabriel P.", state.campoNombre)
        assertEquals("10987654", state.campoCodigo)
        assertFalse(state.puedeLiquidar)
    }

    @Test
    fun testIniciarSesionYCerrarSesion() = runTest(testDispatcher) {
        val viewModel = ServiciosViewModel()
        advanceUntilIdle()

        // Registrar estudiante
        viewModel.onNombreCambiado("Carlos Mario")
        viewModel.onCodigoCambiado("30123456")
        viewModel.guardarPerfil()
        advanceUntilIdle()

        // Liquidar un curso para Carlos
        viewModel.onCursoSeleccionado("Ciberseguridad")
        viewModel.liquidarMatricula() // 500,000
        advanceUntilIdle()

        // Cerrar sesión
        viewModel.cerrarSesion()
        var state = viewModel.uiState.value
        assertNull(state.perfilGuardado)
        assertFalse(state.puedeLiquidar)
        assertEquals(0, state.listaLiquidaciones.size)

        // Intentar iniciar sesión con código inexistente
        viewModel.iniciarSesion("99999999")
        advanceUntilIdle()
        state = viewModel.uiState.value
        assertTrue(state.esError)
        assertFalse(state.puedeLiquidar)

        // Iniciar sesión con el código registrado
        viewModel.iniciarSesion("30123456")
        advanceUntilIdle()
        state = viewModel.uiState.value
        assertFalse(state.esError)
        assertTrue(state.puedeLiquidar)
        assertEquals("Carlos Mario", state.campoNombre)
        assertEquals("30123456", state.campoCodigo)
        assertEquals(1, state.listaLiquidaciones.size)
        assertEquals(500000.0, state.totalAcumulado, 0.01)
    }

    @Test
    fun testPerfilAdministradorYGestion() = runTest(testDispatcher) {
        val viewModel = ServiciosViewModel()
        advanceUntilIdle()

        // Iniciar sesión como Administrador con código "ADMIN"
        viewModel.iniciarSesion("ADMIN")
        advanceUntilIdle()

        var state = viewModel.uiState.value
        assertTrue(state.esAdmin)
        assertEquals("Administrador UTS", state.perfilGuardado?.nombre)
        assertTrue(state.puedeLiquidar)

        // Gestionar cursos libres: agregar nuevo curso
        viewModel.agregarCursoLibre("Robótica y Automatización", 620000.0)
        state = viewModel.uiState.value
        assertTrue(state.cursosDisponibles.containsKey("Robótica y Automatización"))
        assertEquals(620000.0, state.cursosDisponibles["Robótica y Automatización"] ?: 0.0, 0.01)

        // Eliminar curso
        viewModel.eliminarCursoLibre("Robótica y Automatización")
        state = viewModel.uiState.value
        assertFalse(state.cursosDisponibles.containsKey("Robótica y Automatización"))

        // Gestionar parámetros financieros (Becas e Icetex)
        viewModel.actualizarConfiguracionFinanciera(
            porcentajeBeca = 0.60,
            porcentajeIcetex = 0.08,
            becaHabilitada = true,
            icetexHabilitado = true
        )
        state = viewModel.uiState.value
        assertEquals(0.60, state.porcentajeBeca, 0.001)
        assertEquals(0.08, state.porcentajeIcetex, 0.001)
    }

    @Test
    fun testSeleccionMultipleCursosYRecargoIcetexSobreDescuento() = runTest(testDispatcher) {
        val viewModel = ServiciosViewModel()
        advanceUntilIdle()

        // Registrar estudiante
        viewModel.onNombreCambiado("Laura Sanchez")
        viewModel.onCodigoCambiado("40123456")
        viewModel.guardarPerfil()
        advanceUntilIdle()

        // Seleccionar múltiples cursos: Inglés Técnico ($300,000) y Ciberseguridad ($500,000)
        viewModel.onCursoSeleccionado("Inglés Técnico")
        viewModel.toggleCursoSeleccionado("Ciberseguridad")

        var state = viewModel.uiState.value
        assertEquals(2, state.cursosSeleccionados.size)
        assertEquals(800000.0, state.subtotalBase, 0.01)

        // Activar Beca (50%)
        viewModel.onBecaCambiada(true)
        state = viewModel.uiState.value
        assertEquals(400000.0, state.descuentoBeca, 0.01)
        assertEquals(400000.0, state.baseConDescuento, 0.01)

        // Activar Icetex (5%): debe aplicarse tras deducir el descuento (5% de $400,000 = $20,000)
        viewModel.onIcetexCambiado(true)
        state = viewModel.uiState.value
        assertEquals(20000.0, state.recargoIcetex, 0.01)
        assertEquals(420000.0, state.totalCalculado, 0.01)

        // Liquidar matrícula
        viewModel.liquidarMatricula()
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertEquals(1, state.listaLiquidaciones.size)
        assertEquals(420000.0, state.listaLiquidaciones.first().totalAPagar, 0.01)
        assertEquals(800000.0, state.listaLiquidaciones.first().costoBase, 0.01)
        assertTrue(state.listaLiquidaciones.first().cursoLibre.contains("Inglés Técnico"))
        assertTrue(state.listaLiquidaciones.first().cursoLibre.contains("Ciberseguridad"))
    }
}
