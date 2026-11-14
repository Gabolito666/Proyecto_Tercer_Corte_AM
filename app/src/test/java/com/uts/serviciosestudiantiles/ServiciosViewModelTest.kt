package com.uts.serviciosestudiantiles

import com.uts.serviciosestudiantiles.viewmodel.ModoAuth
import com.uts.serviciosestudiantiles.viewmodel.ServiciosViewModel
import io.github.jan.supabase.postgrest.from
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
        assertFalse(state.puedeLiquidar) // Segregación de roles: el administrador no realiza liquidaciones

        // Gestionar creación de becas
        viewModel.crearBeca("Beca Santander Tech", 0.45, "Promedio > 4.2")
        advanceUntilIdle()
        state = viewModel.uiState.value
        assertTrue(state.becasDisponibles.any { it.nombre == "Beca Santander Tech" })
        val becaCreada = state.becasDisponibles.first { it.nombre == "Beca Santander Tech" }
        assertEquals(0.45, becaCreada.porcentajeDescuento, 0.01)

        // Eliminar beca
        viewModel.eliminarBeca(becaCreada.id)
        advanceUntilIdle()
        state = viewModel.uiState.value
        assertFalse(state.becasDisponibles.any { it.nombre == "Beca Santander Tech" })

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

    @Test
    fun testFlujoContrasenasYAutenticacionSegura() = runTest(testDispatcher) {
        val viewModel = ServiciosViewModel()
        advanceUntilIdle()

        // 1. Registro de nuevo usuario solo con nombre y código
        viewModel.onNombreCambiado("Estudiante Prueba")
        viewModel.onCodigoCambiado("50123456")
        viewModel.guardarPerfil()
        advanceUntilIdle()

        var state = viewModel.uiState.value
        assertNotNull(state.perfilGuardado)
        // La contraseña por defecto es automáticamente su código
        assertEquals("50123456", state.perfilGuardado?.contrasenaEfectiva)
        // Requiere cambio obligatorio por seguridad
        assertTrue(state.perfilGuardado?.requiereCambioContrasena == true)

        // Cerrar sesión
        viewModel.cerrarSesion()
        advanceUntilIdle()

        // 2. Ingreso por segunda vez con contraseña incorrecta -> Debe fallar
        viewModel.onRolAuthCambiado(com.uts.serviciosestudiantiles.viewmodel.RolUsuario.ESTUDIANTE)
        viewModel.iniciarSesion("50123456", "claveErronea123")
        advanceUntilIdle()
        state = viewModel.uiState.value
        assertTrue(state.esError)
        assertTrue(state.mensajeMensajeEstado?.contains("Contraseña incorrecta") == true)

        // 3. Ingreso con su contraseña inicial (su mismo código) -> Debe exigir cambio de contraseña
        viewModel.iniciarSesion("50123456", "50123456")
        advanceUntilIdle()
        state = viewModel.uiState.value
        assertFalse(state.esError)
        assertTrue(state.mostrarDialogoCambioPassword)

        // 4. Intento de cambio con contraseña igual al código -> Debe rechazar por seguridad
        viewModel.onNuevoPasswordCambiado("50123456")
        viewModel.onConfirmarNuevoPasswordCambiado("50123456")
        viewModel.cambiarContrasenaObligatoria()
        advanceUntilIdle()
        state = viewModel.uiState.value
        assertNotNull(state.errorCambioPassword)
        assertTrue(state.mostrarDialogoCambioPassword)

        // 5. Cambio exitoso por una contraseña diferente
        viewModel.onNuevoPasswordCambiado("MiClaveSegura2026")
        viewModel.onConfirmarNuevoPasswordCambiado("MiClaveSegura2026")
        viewModel.cambiarContrasenaObligatoria()
        advanceUntilIdle()
        state = viewModel.uiState.value
        assertFalse(state.mostrarDialogoCambioPassword)
        assertEquals("MiClaveSegura2026", state.perfilGuardado?.contrasenaEfectiva)
        assertFalse(state.perfilGuardado?.requiereCambioContrasena == true)

        // 6. Cerrar sesión y verificar que inicia con la nueva contraseña sin pedir cambio
        viewModel.cerrarSesion()
        advanceUntilIdle()

        viewModel.iniciarSesion("50123456", "MiClaveSegura2026")
        advanceUntilIdle()
        state = viewModel.uiState.value
        assertFalse(state.esError)
        assertFalse(state.mostrarDialogoCambioPassword)
        assertEquals("Estudiante Prueba", state.perfilGuardado?.nombre)

        // 7. Verificación de Administrador: contraseña obligatoria 'admin'
        viewModel.cerrarSesion()
        advanceUntilIdle()

        viewModel.onRolAuthCambiado(com.uts.serviciosestudiantiles.viewmodel.RolUsuario.ADMINISTRADOR)
        // Con contraseña incorrecta
        viewModel.iniciarSesion("ADMIN", "incorrecta")
        advanceUntilIdle()
        state = viewModel.uiState.value
        assertTrue(state.esError)
        assertFalse(state.esAdmin)

        // Con contraseña correcta 'admin'
        viewModel.iniciarSesion("ADMIN", "admin")
        advanceUntilIdle()
        state = viewModel.uiState.value
        assertFalse(state.esError)
        assertTrue(state.esAdmin)
        assertFalse(state.mostrarDialogoCambioPassword)
    }

    @Test
    fun testConexionRealSupabase() = runTest {
        try {
            val repo = com.uts.serviciosestudiantiles.data.ServiciosRepository()
            com.uts.serviciosestudiantiles.data.ServiciosRepository.isTestMode = false
            val perfiles = repo.obtenerTodosLosPerfilesRemotos()
            println("TEST CONEXION PERFILES: ${perfiles.size}")
            assertTrue(perfiles.isNotEmpty())

            val gabriel = repo.buscarPerfilPorCodigo("1142714246")
            println("TEST BUSCAR GABRIEL: $gabriel")
            assertNotNull(gabriel)

            val inexistente = repo.buscarPerfilPorCodigo("00000000")
            println("TEST BUSCAR INEXISTENTE: $inexistente")
            assertNull(inexistente)

            // Test guardar un nuevo perfil en Supabase (ej. codigo 42)
            val codigoPrueba = "42"
            val nuevo = com.uts.serviciosestudiantiles.data.PerfilEstudiante(
                id = null,
                nombre = "Estudiante Cuarentaydos",
                codigo = codigoPrueba,
                facultad = "Ingenierías",
                correoInstitucional = "estudiante42.42@uts.edu.co",
                contrasena = codigoPrueba,
                debeCambiarContrasena = true
            )
            val guardadoExito = repo.guardarPerfil(nuevo)
            assertTrue("guardarPerfil debió retornar true", guardadoExito)

            val encontrado = repo.buscarPerfilPorCodigo(codigoPrueba)
            assertNotNull("El perfil 42 debió ser recuperado de Supabase", encontrado)
            assertEquals(codigoPrueba, encontrado?.codigo)
            assertEquals("42", encontrado?.contrasenaEfectiva)
            assertTrue(encontrado?.requiereCambioContrasena == true)
        } catch (e: Throwable) {
            println("TEST CONEXION ERROR: ${e.javaClass.name}: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }
}
