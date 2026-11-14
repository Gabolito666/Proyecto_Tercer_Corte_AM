package com.uts.serviciosestudiantiles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uts.serviciosestudiantiles.data.Beca
import com.uts.serviciosestudiantiles.data.CursoLibre
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

    val cursosConCostosIniciales: Map<String, Double> = mapOf(
        "Inglés Técnico" to 300000.0,
        "Programación Web" to 450000.0,
        "Ciberseguridad" to 500000.0,
        "Diseño UX UI Avanzado" to 380000.0,
        "Inteligencia Artificial Aplicada" to 550000.0,
        "Marketing Digital y Analítica" to 320000.0,
        "Ciencia de Datos con Python" to 480000.0
    )

    // Oferta dinámica accesible desde el ViewModel
    val cursosConCostos: Map<String, Double>
        get() = _uiState.value.cursosDisponibles

    private val _uiState = MutableStateFlow(
        ServiciosUiState(
            cursoSeleccionado = "Inglés Técnico",
            cursosSeleccionados = setOf("Inglés Técnico"),
            cursosDisponibles = cursosConCostosIniciales
        )
    )
    val uiState: StateFlow<ServiciosUiState> = _uiState.asStateFlow()

    init {
        cargarDatosPersistidos()
        cargarCursosRemotos()
        cargarBecasRemotas()
    }

    private fun cargarCursosRemotos() {
        viewModelScope.launch {
            val cursos = repository.obtenerCursosRemotos()
            if (cursos.isNotEmpty()) {
                val mapa = cursos.associate { it.nombre to it.costoBase }
                _uiState.update { current ->
                    current.copy(cursosDisponibles = current.cursosDisponibles + mapa)
                }
            }
        }
    }

    fun cargarBecasRemotas() {
        viewModelScope.launch {
            val becas = repository.obtenerBecasRemotas()
            _uiState.update { current ->
                current.copy(becasDisponibles = becas)
            }
        }
    }

    fun cargarDatosPersistidos() {
        val ultimoPerfil = repository.obtenerPerfil()
        if (ultimoPerfil != null) {
            viewModelScope.launch {
                val liquidaciones = repository.obtenerLiquidacionesPorCodigo(ultimoPerfil.codigo)
                val total = liquidaciones.sumOf { it.totalAPagar }
                _uiState.update {
                    it.copy(
                        campoNombre = ultimoPerfil.nombre,
                        campoCodigo = ultimoPerfil.codigo,
                        facultadSeleccionada = ultimoPerfil.facultad,
                        perfilGuardado = ultimoPerfil,
                        correoSimulado = ultimoPerfil.correoInstitucional,
                        puedeLiquidar = true,
                        esUsuarioExistente = true,
                        esAdmin = ultimoPerfil.codigo.equals("ADMIN", ignoreCase = true),
                        mensajeMensajeEstado = "Sesión cargada: Datos e historial restaurados",
                        listaLiquidaciones = liquidaciones,
                        totalAcumulado = total
                    )
                }
            }
        }
    }

    // --- MÓDULO 1: AUTENTICACIÓN (LOGIN, REGISTRO Y GESTIÓN DE ROLES) ---

    fun setModoAuth(modo: ModoAuth) {
        _uiState.update {
            it.copy(
                modoAuth = modo,
                mensajeMensajeEstado = null,
                esError = false
            )
        }
    }

    fun onRolAuthCambiado(rol: RolUsuario) {
        _uiState.update {
            it.copy(
                rolAuthSeleccionado = rol,
                mensajeMensajeEstado = null,
                esError = false,
                campoCodigo = if (rol == RolUsuario.ADMINISTRADOR) "ADMIN" else if (it.campoCodigo == "ADMIN") "" else it.campoCodigo
            )
        }
    }

    fun onNombreCambiado(nombre: String) {
        _uiState.update { it.copy(campoNombre = nombre, mensajeMensajeEstado = null, esError = false) }
    }

    fun onCodigoCambiado(codigo: String) {
        _uiState.update { it.copy(campoCodigo = codigo, mensajeMensajeEstado = null, esError = false) }
    }

    fun onPasswordCambiado(password: String) {
        _uiState.update { it.copy(campoPassword = password, mensajeMensajeEstado = null, esError = false) }
    }

    fun togglePasswordVisible() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
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

    /**
     * Iniciar sesión para estudiantes registrados o Administrador del sistema.
     */
    fun iniciarSesion(
        codigoIngresado: String = _uiState.value.campoCodigo,
        passwordIngresado: String = _uiState.value.campoPassword
    ) {
        val codigoLimpio = codigoIngresado.trim()
        val passwordLimpio = passwordIngresado.trim()
        val rolActual = _uiState.value.rolAuthSeleccionado

        if (codigoLimpio.isEmpty()) {
            _uiState.update { it.copy(esError = true, mensajeMensajeEstado = "Por favor ingresa tu código de acceso") }
            return
        }

        // Acceso especial como Administrador institucional
        if (rolActual == RolUsuario.ADMINISTRADOR || codigoLimpio.equals("ADMIN", ignoreCase = true)) {
            val passEfectivo = if (passwordLimpio.isEmpty() && ServiciosRepository.isTestMode) "admin" else passwordLimpio
            if (passEfectivo != "admin") {
                _uiState.update {
                    it.copy(
                        esError = true,
                        mensajeMensajeEstado = "Contraseña incorrecta para Administrador. La contraseña requerida es 'admin'."
                    )
                }
                return
            }

            val adminPerfil = PerfilEstudiante(
                id = 0,
                nombre = "Administrador UTS",
                codigo = "ADMIN",
                facultad = "Dirección Académica",
                correoInstitucional = "admin.uts@uts.edu.co",
                contrasena = "admin",
                debeCambiarContrasena = false
            )
            viewModelScope.launch {
                val todasLasLiquidaciones = repository.obtenerTodasLasLiquidaciones()
                val todosLosPerfiles = repository.obtenerTodosLosPerfilesRemotos()
                val becas = repository.obtenerBecasRemotas()
                _uiState.update {
                    it.copy(
                        esAdmin = true,
                        perfilGuardado = adminPerfil,
                        campoNombre = adminPerfil.nombre,
                        campoCodigo = adminPerfil.codigo,
                        campoPassword = "",
                        correoSimulado = adminPerfil.correoInstitucional,
                        puedeLiquidar = false, // El administrador no realiza liquidaciones para sí mismo
                        esUsuarioExistente = true,
                        esError = false,
                        mostrarDialogoCambioPassword = false,
                        mensajeMensajeEstado = "Sesión iniciada como Administrador del Sistema UTS",
                        todasLasLiquidacionesAdmin = todasLasLiquidaciones,
                        todosLosPerfilesAdmin = todosLosPerfiles,
                        becasDisponibles = becas,
                        listaLiquidaciones = todasLasLiquidaciones,
                        totalAcumulado = todasLasLiquidaciones.sumOf { l -> l.totalAPagar }
                    )
                }
            }
            return
        }

        // Acceso para Estudiantes
        val passEfectivoEstudiante = if (passwordLimpio.isEmpty() && ServiciosRepository.isTestMode) codigoLimpio else passwordLimpio
        if (passEfectivoEstudiante.isEmpty()) {
            _uiState.update { it.copy(esError = true, mensajeMensajeEstado = "Por favor ingresa tu contraseña") }
            return
        }

        // Consulta de estudiante registrado en Supabase / persistencia local
        viewModelScope.launch {
            val estudiante = repository.buscarPerfilPorCodigo(codigoLimpio)
            if (estudiante != null) {
                // Validación estricta de contraseña
                if (passEfectivoEstudiante != estudiante.contrasenaEfectiva) {
                    _uiState.update {
                        it.copy(
                            esError = true,
                            mensajeMensajeEstado = "Contraseña incorrecta para el estudiante $codigoLimpio. Verifica tus credenciales."
                        )
                    }
                    return@launch
                }

                val liquidacionesEstudiante = repository.obtenerLiquidacionesPorCodigo(codigoLimpio)
                val totalEstudiante = liquidacionesEstudiante.sumOf { it.totalAPagar }

                // Si debe cambiar la contraseña (primer ingreso con código como contraseña)
                if (estudiante.requiereCambioContrasena) {
                    _uiState.update {
                        it.copy(
                            perfilGuardado = estudiante,
                            campoNombre = estudiante.nombre,
                            campoCodigo = estudiante.codigo,
                            campoPassword = "",
                            facultadSeleccionada = estudiante.facultad,
                            correoSimulado = estudiante.correoInstitucional,
                            puedeLiquidar = true,
                            esUsuarioExistente = true,
                            esAdmin = false,
                            esError = false,
                            mostrarDialogoCambioPassword = true,
                            campoNuevoPassword = "",
                            campoConfirmarNuevoPassword = "",
                            errorCambioPassword = null,
                            mensajeMensajeEstado = "Por motivos de seguridad, debes cambiar tu contraseña inicial.",
                            listaLiquidaciones = liquidacionesEstudiante,
                            totalAcumulado = totalEstudiante
                        )
                    }
                    return@launch
                }

                _uiState.update {
                    it.copy(
                        perfilGuardado = estudiante,
                        campoNombre = estudiante.nombre,
                        campoCodigo = estudiante.codigo,
                        campoPassword = "",
                        facultadSeleccionada = estudiante.facultad,
                        correoSimulado = estudiante.correoInstitucional,
                        puedeLiquidar = true,
                        esUsuarioExistente = true,
                        esAdmin = false,
                        esError = false,
                        mostrarDialogoCambioPassword = false,
                        mensajeMensajeEstado = "Bienvenido de nuevo, ${estudiante.nombre}",
                        listaLiquidaciones = liquidacionesEstudiante,
                        totalAcumulado = totalEstudiante
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        esError = true,
                        esUsuarioExistente = false,
                        mensajeMensajeEstado = "El código '$codigoLimpio' no está registrado. Regístrate como estudiante nuevo.",
                        puedeLiquidar = false
                    )
                }
            }
        }
    }

    /**
     * Registro de nuevo estudiante: solo requiere nombre y código.
     * Su contraseña inicial será automáticamente su mismo código y se exigirá cambiarla por seguridad.
     */
    fun guardarPerfil() {
        val currentState = _uiState.value
        val nombre = currentState.campoNombre.trim()
        val codigo = currentState.campoCodigo.trim()

        if (nombre.isEmpty() || codigo.isEmpty()) {
            _uiState.update { it.copy(esError = true, mensajeMensajeEstado = "Completa tu nombre y código para continuar con el registro") }
            return
        }

        if (codigo.equals("ADMIN", ignoreCase = true)) {
            _uiState.update { it.copy(esError = true, mensajeMensajeEstado = "El código 'ADMIN' está reservado para el Administrador") }
            return
        }

        viewModelScope.launch {
            val perfilExistente = repository.buscarPerfilPorCodigo(codigo)

            if (perfilExistente != null) {
                // Código ya ocupado: notificar error y no sobrescribir nombre
                _uiState.update {
                    it.copy(
                        esError = true,
                        esUsuarioExistente = false,
                        mensajeMensajeEstado = "El código ya está ocupado",
                        puedeLiquidar = false
                    )
                }
            } else {
                val nombreLimpio = nombre.replace(" ", "").lowercase()
                val codigoLimpio = codigo.replace(" ", "").lowercase()
                val correo = "$nombreLimpio.$codigoLimpio@uts.edu.co"

                val nuevoPerfil = PerfilEstudiante(
                    id = null,
                    nombre = nombre,
                    codigo = codigo,
                    facultad = "Ingenierías",
                    correoInstitucional = correo,
                    contrasena = codigo, // Contraseña por defecto es su propio código
                    debeCambiarContrasena = true // Requiere cambio obligatorio por seguridad
                )

                repository.guardarPerfil(nuevoPerfil)

                val liquidacionesEstudiante = repository.obtenerLiquidacionesPorCodigo(codigo)
                val totalEstudiante = liquidacionesEstudiante.sumOf { it.totalAPagar }

                _uiState.update {
                    it.copy(
                        perfilGuardado = nuevoPerfil,
                        correoSimulado = correo,
                        puedeLiquidar = true,
                        esUsuarioExistente = false,
                        esAdmin = false,
                        esError = false,
                        campoPassword = "",
                        mostrarDialogoCambioPassword = false,
                        mensajeMensajeEstado = "Nuevo estudiante registrado: Perfil guardado con éxito",
                        listaLiquidaciones = liquidacionesEstudiante,
                        totalAcumulado = totalEstudiante
                    )
                }
            }
        }
    }

    // --- MÉTODOS DE GESTIÓN Y CAMBIO DE CONTRASEÑA ---

    fun onNuevoPasswordCambiado(valor: String) {
        _uiState.update { it.copy(campoNuevoPassword = valor, errorCambioPassword = null) }
    }

    fun onConfirmarNuevoPasswordCambiado(valor: String) {
        _uiState.update { it.copy(campoConfirmarNuevoPassword = valor, errorCambioPassword = null) }
    }

    fun toggleNuevoPasswordVisible() {
        _uiState.update { it.copy(nuevoPasswordVisible = !it.nuevoPasswordVisible) }
    }

    fun toggleConfirmarPasswordVisible() {
        _uiState.update { it.copy(confirmarPasswordVisible = !it.confirmarPasswordVisible) }
    }

    fun abrirDialogoCambioPassword() {
        _uiState.update {
            it.copy(
                mostrarDialogoCambioPassword = true,
                campoNuevoPassword = "",
                campoConfirmarNuevoPassword = "",
                errorCambioPassword = null
            )
        }
    }

    fun cancelarCambioPassword() {
        val requiere = _uiState.value.perfilGuardado?.requiereCambioContrasena ?: false
        if (requiere) {
            cerrarSesion()
        } else {
            _uiState.update {
                it.copy(
                    mostrarDialogoCambioPassword = false,
                    campoNuevoPassword = "",
                    campoConfirmarNuevoPassword = "",
                    errorCambioPassword = null
                )
            }
        }
    }

    fun cambiarContrasenaObligatoria() {
        val state = _uiState.value
        val perfilActual = state.perfilGuardado ?: return
        val nueva = state.campoNuevoPassword.trim()
        val confirmacion = state.campoConfirmarNuevoPassword.trim()

        if (nueva.isEmpty()) {
            _uiState.update { it.copy(errorCambioPassword = "Por favor ingresa la nueva contraseña") }
            return
        }

        if (nueva.length < 4) {
            _uiState.update { it.copy(errorCambioPassword = "La nueva contraseña debe tener al menos 4 caracteres") }
            return
        }

        if (nueva.equals(perfilActual.codigo.trim(), ignoreCase = true)) {
            _uiState.update { it.copy(errorCambioPassword = "Por seguridad, la nueva contraseña debe ser diferente a tu código estudiantil") }
            return
        }

        if (nueva != confirmacion) {
            _uiState.update { it.copy(errorCambioPassword = "Las contraseñas ingresadas no coinciden") }
            return
        }

        viewModelScope.launch {
            val exitoso = repository.actualizarContrasena(perfilActual.codigo, nueva)
            if (exitoso) {
                val perfilActualizado = perfilActual.copy(
                    contrasena = nueva,
                    debeCambiarContrasena = false
                )
                val liquidacionesEstudiante = repository.obtenerLiquidacionesPorCodigo(perfilActual.codigo)
                val totalEstudiante = liquidacionesEstudiante.sumOf { it.totalAPagar }

                _uiState.update {
                    it.copy(
                        perfilGuardado = perfilActualizado,
                        mostrarDialogoCambioPassword = false,
                        campoPassword = "",
                        campoNuevoPassword = "",
                        campoConfirmarNuevoPassword = "",
                        errorCambioPassword = null,
                        puedeLiquidar = true,
                        esError = false,
                        mensajeMensajeEstado = "¡Contraseña actualizada exitosamente! Bienvenido al portal, ${perfilActualizado.nombre}.",
                        listaLiquidaciones = liquidacionesEstudiante,
                        totalAcumulado = totalEstudiante
                    )
                }
            } else {
                _uiState.update {
                    it.copy(errorCambioPassword = "Error al actualizar la contraseña en Supabase. Intenta nuevamente.")
                }
            }
        }
    }

    fun cerrarSesion() {
        repository.limpiarSesionActiva()
        _uiState.update {
            it.copy(
                perfilGuardado = null,
                campoNombre = "",
                campoCodigo = "",
                campoPassword = "",
                campoNuevoPassword = "",
                campoConfirmarNuevoPassword = "",
                errorCambioPassword = null,
                mostrarDialogoCambioPassword = false,
                correoSimulado = "",
                puedeLiquidar = false,
                esAdmin = false,
                esUsuarioExistente = false,
                esError = false,
                mensajeMensajeEstado = "Sesión cerrada correctamente",
                listaLiquidaciones = emptyList(),
                totalAcumulado = 0.0,
                modoAuth = ModoAuth.LOGIN,
                rolAuthSeleccionado = RolUsuario.ESTUDIANTE
            )
        }
    }

    // --- MÓDULO 2: LIQUIDACIÓN DE MÚLTIPLES CURSOS Y CÁLCULOS ---

    fun onCursoSeleccionado(curso: String) {
        _uiState.update {
            it.copy(
                cursoSeleccionado = curso,
                cursosSeleccionados = setOf(curso),
                menuCursoAbierto = false
            )
        }
    }

    fun toggleCursoSeleccionado(curso: String) {
        _uiState.update { current ->
            val actual = current.cursosSeleccionados
            val nuevaSeleccion = if (actual.contains(curso)) {
                if (actual.size > 1) actual - curso else actual
            } else {
                actual + curso
            }
            current.copy(
                cursosSeleccionados = nuevaSeleccion,
                cursoSeleccionado = nuevaSeleccion.firstOrNull() ?: curso
            )
        }
    }

    fun toggleMenuCurso(abierto: Boolean) {
        _uiState.update { it.copy(menuCursoAbierto = abierto) }
    }

    fun onBecaCambiada(tieneBeca: Boolean) {
        if (tieneBeca) {
            val primeraBeca = _uiState.value.becasDisponibles.firstOrNull()
            _uiState.update {
                it.copy(
                    tieneBeca = true,
                    becaSeleccionadaId = primeraBeca?.id ?: 1,
                    porcentajeBeca = primeraBeca?.porcentajeDescuento ?: it.porcentajeBeca
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    tieneBeca = false,
                    becaSeleccionadaId = null
                )
            }
        }
    }

    fun onIcetexCambiado(tieneIcetex: Boolean) {
        _uiState.update { it.copy(tieneIcetex = tieneIcetex) }
    }

    /**
     * Liquidación de matrícula para los cursos seleccionados con cálculo de Icetex
     * deducido tras aplicar el descuento de becas correspondiente.
     */
    fun liquidarMatricula() {
        val currentState = _uiState.value
        val perfilActual = currentState.perfilGuardado ?: return
        if (!currentState.puedeLiquidar || currentState.cursosSeleccionados.isEmpty()) {
            return
        }

        val subtotal = currentState.subtotalBase
        val totalAPagar = currentState.totalCalculado
        val cursosNombres = currentState.cursosSeleccionados.joinToString(", ")

        val liquidacionesActuales = currentState.listaLiquidaciones

        viewModelScope.launch {
            val nuevoId = repository.obtenerSiguienteIdLiquidacion()
            val todosCursos = repository.obtenerCursosRemotos()
            val primerCursoId = if (currentState.cursosSeleccionados.size == 1) {
                todosCursos.firstOrNull { it.nombre.equals(currentState.cursosSeleccionados.first(), ignoreCase = true) }?.id
            } else null

            val becaAsociadaId = if (currentState.tieneBeca) {
                currentState.becaSeleccionadaId ?: repository.obtenerBecasRemotas().firstOrNull()?.id ?: 1
            } else null

            val nuevaLiquidacion = LiquidacionMatricula(
                id = nuevoId,
                codigoEstudiante = perfilActual.codigo,
                cursoLibre = cursosNombres,
                costoBase = subtotal,
                tieneBeca = currentState.tieneBeca,
                tieneIcetex = currentState.tieneIcetex,
                totalAPagar = totalAPagar,
                cursoId = primerCursoId,
                becaId = becaAsociadaId
            )

            repository.guardarLiquidacion(nuevaLiquidacion)

            val listaActualizada = repository.obtenerLiquidacionesPorCodigo(perfilActual.codigo)
            val listaFinal = if (listaActualizada.isNotEmpty()) listaActualizada else (liquidacionesActuales + nuevaLiquidacion)
            val nuevoTotalAcumulado = listaFinal.sumOf { it.totalAPagar }

            _uiState.update {
                it.copy(
                    listaLiquidaciones = listaFinal,
                    totalAcumulado = nuevoTotalAcumulado,
                    tieneBeca = false,
                    tieneIcetex = false,
                    mensajeMensajeEstado = "Matrícula liquidada exitosamente por $nuevoTotalAcumulado"
                )
            }
        }
    }

    fun actualizarHistorialPorEstudiante(codigoEstudiante: String? = _uiState.value.perfilGuardado?.codigo) {
        val currentState = _uiState.value
        if (currentState.esAdmin) {
            cargarDatosAdmin()
            return
        }

        if (codigoEstudiante.isNullOrBlank()) {
            _uiState.update {
                it.copy(
                    listaLiquidaciones = emptyList(),
                    totalAcumulado = 0.0
                )
            }
            return
        }

        viewModelScope.launch {
            val liquidacionesFiltradas = repository.obtenerLiquidacionesPorCodigo(codigoEstudiante)
            val nuevoTotal = liquidacionesFiltradas.sumOf { it.totalAPagar }

            _uiState.update {
                it.copy(
                    listaLiquidaciones = liquidacionesFiltradas,
                    totalAcumulado = nuevoTotal
                )
            }
        }
    }

    // --- MÓDULO 3: GESTIÓN DE ADMINISTRADOR Y CREACIÓN DE BECAS ---

    fun cargarDatosAdmin() {
        viewModelScope.launch {
            val liquidaciones = repository.obtenerTodasLasLiquidaciones()
            val perfiles = repository.obtenerTodosLosPerfilesRemotos()
            val becas = repository.obtenerBecasRemotas()
            _uiState.update {
                it.copy(
                    todasLasLiquidacionesAdmin = liquidaciones,
                    todosLosPerfilesAdmin = perfiles,
                    becasDisponibles = becas,
                    listaLiquidaciones = liquidaciones,
                    totalAcumulado = liquidaciones.sumOf { l -> l.totalAPagar }
                )
            }
        }
    }

    fun crearBeca(nombre: String, porcentaje: Double, requisitos: String) {
        val nombreLimpio = nombre.trim()
        val requisitosLimpios = requisitos.trim()
        if (nombreLimpio.isEmpty()) {
            _uiState.update { it.copy(esError = true, mensajeMensajeEstado = "Ingresa el nombre de la beca") }
            return
        }
        if (porcentaje <= 0.0 || porcentaje > 1.0) {
            _uiState.update { it.copy(esError = true, mensajeMensajeEstado = "El descuento debe ser entre 1% y 100%") }
            return
        }

        viewModelScope.launch {
            val nuevoId = repository.obtenerSiguienteIdBeca()
            val nuevaBeca = Beca(
                id = nuevoId,
                nombre = nombreLimpio,
                porcentajeDescuento = porcentaje,
                requisitos = if (requisitosLimpios.isNotEmpty()) requisitosLimpios else "Sin requisitos específicos",
                activa = true
            )
            repository.guardarBecaRemoto(nuevaBeca)
            val becasActualizadas = repository.obtenerBecasRemotas()
            _uiState.update {
                it.copy(
                    becasDisponibles = becasActualizadas,
                    porcentajeBeca = porcentaje,
                    mensajeMensajeEstado = "Beca '$nombreLimpio' creada y guardada exitosamente (${(porcentaje * 100).toInt()}%)",
                    esError = false
                )
            }
        }
    }

    fun eliminarBeca(id: Int) {
        viewModelScope.launch {
            repository.eliminarBecaRemoto(id)
            val becasActualizadas = repository.obtenerBecasRemotas()
            _uiState.update {
                it.copy(
                    becasDisponibles = becasActualizadas,
                    mensajeMensajeEstado = "Beca eliminada correctamente",
                    esError = false
                )
            }
        }
    }

    fun seleccionarBeca(id: Int?) {
        _uiState.update { current ->
            val beca = current.becasDisponibles.firstOrNull { it.id == id }
            current.copy(
                becaSeleccionadaId = id,
                porcentajeBeca = beca?.porcentajeDescuento ?: current.porcentajeBeca,
                tieneBeca = id != null
            )
        }
    }

    fun agregarCursoLibre(nombre: String, costo: Double) {
        val nombreLimpio = nombre.trim()
        if (nombreLimpio.isEmpty() || costo <= 0.0) {
            _uiState.update { it.copy(esError = true, mensajeMensajeEstado = "Nombre y precio válidos requeridos") }
            return
        }

        val nuevaOferta = _uiState.value.cursosDisponibles + (nombreLimpio to costo)
        _uiState.update {
            it.copy(
                cursosDisponibles = nuevaOferta,
                mensajeMensajeEstado = "Curso '$nombreLimpio' agregado a la oferta académica",
                esError = false
            )
        }

        viewModelScope.launch {
            repository.guardarCursoRemoto(
                CursoLibre(
                    id = (repository.obtenerCursosRemotos().maxOfOrNull { it.id } ?: 0) + 1,
                    nombre = nombreLimpio,
                    costoBase = costo
                )
            )
        }
    }

    fun eliminarCursoLibre(nombre: String) {
        val nuevaOferta = _uiState.value.cursosDisponibles - nombre
        val nuevaSeleccion = _uiState.value.cursosSeleccionados - nombre
        _uiState.update {
            it.copy(
                cursosDisponibles = nuevaOferta,
                cursosSeleccionados = if (nuevaSeleccion.isEmpty() && nuevaOferta.isNotEmpty()) setOf(nuevaOferta.keys.first()) else nuevaSeleccion,
                cursoSeleccionado = nuevaOferta.keys.firstOrNull() ?: "",
                mensajeMensajeEstado = "Curso '$nombre' eliminado de la oferta",
                esError = false
            )
        }

        viewModelScope.launch {
            repository.eliminarCursoRemoto(nombre)
        }
    }

    fun actualizarConfiguracionFinanciera(
        porcentajeBeca: Double,
        porcentajeIcetex: Double,
        becaHabilitada: Boolean,
        icetexHabilitado: Boolean
    ) {
        _uiState.update {
            it.copy(
                porcentajeBeca = porcentajeBeca.coerceIn(0.0, 1.0),
                porcentajeIcetex = porcentajeIcetex.coerceIn(0.0, 1.0),
                becaHabilitada = becaHabilitada,
                icetexHabilitado = icetexHabilitado,
                mensajeMensajeEstado = "Parámetros de Becas e Icetex actualizados exitosamente",
                esError = false
            )
        }
    }
}
