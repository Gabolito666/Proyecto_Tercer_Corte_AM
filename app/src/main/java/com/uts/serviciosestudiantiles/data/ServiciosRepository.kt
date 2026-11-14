package com.uts.serviciosestudiantiles.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActualizarContrasenaDto(
    @SerialName("contrasena") val contrasena: String,
    @SerialName("debe_cambiar_contrasena") val debeCambiarContrasena: Boolean
)

@Serializable
data class PerfilEstudianteBasico(
    @SerialName("id") val id: Int = 1,
    @SerialName("nombre") val nombre: String = "",
    @SerialName("codigo") val codigo: String = "",
    @SerialName("facultad") val facultad: String = "",
    @SerialName("correo_institucional") val correoInstitucional: String = ""
)

class ServiciosRepository(
    private val client: SupabaseClient = supabaseClient,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    companion object {
        var isTestMode: Boolean = false
    }

    // Almacenamiento local en memoria como fallback y cache
    private val perfilesLocales = mutableMapOf<String, PerfilEstudiante>()
    private val liquidacionesLocales = mutableListOf<LiquidacionMatricula>()
    private val becasLocales = mutableListOf(
        Beca(id = 1, nombre = "Beca de Excelencia Académica", porcentajeDescuento = 0.50, requisitos = "Promedio acumulado mínimo de 4.5 en el semestre anterior."),
        Beca(id = 2, nombre = "Beca Deportiva y Cultural UTS", porcentajeDescuento = 0.35, requisitos = "Pertenecer a una selección deportiva o grupo cultural oficial."),
        Beca(id = 3, nombre = "Beca de Honor Santander", porcentajeDescuento = 0.70, requisitos = "Primer puesto en pruebas de estado Saber Pro o de cohorte.")
    )
    private var perfilActivo: PerfilEstudiante? = null

    private val isConfigured: Boolean
        get() = !isTestMode &&
                SUPABASE_ANON_KEY.isNotBlank() &&
                !SUPABASE_ANON_KEY.contains("TU_ANON_KEY") &&
                !SUPABASE_ANON_KEY.contains("AQUI") &&
                SUPABASE_URL.isNotBlank() &&
                !SUPABASE_URL.contains("example.com")

    /**
     * Tabla 'perfiles': Guarda o actualiza el perfil utilizando upsert con 'codigo' como PK única.
     */
    suspend fun guardarPerfil(perfil: PerfilEstudiante) {
        val codigoLimpio = perfil.codigo.trim()
        val contrasenaDefecto = perfil.contrasena ?: if (codigoLimpio.equals("ADMIN", ignoreCase = true)) "admin" else codigoLimpio
        val debeCambiar = if (codigoLimpio.equals("ADMIN", ignoreCase = true)) false else (perfil.debeCambiarContrasena ?: true)

        val perfilNormalizado = perfil.copy(
            codigo = codigoLimpio,
            contrasena = contrasenaDefecto,
            debeCambiarContrasena = debeCambiar
        )
        perfilesLocales[codigoLimpio] = perfilNormalizado
        perfilActivo = perfilNormalizado

        if (isConfigured) {
            withContext(Dispatchers.IO) {
                val resultado = runCatching {
                    client.from("perfiles").upsert(perfilNormalizado) {
                        onConflict = "codigo"
                    }
                }
                // Si la tabla remota aún no tiene las nuevas columnas, guardar versión básica para compatibilidad
                if (resultado.isFailure) {
                    runCatching {
                        val basico = PerfilEstudianteBasico(
                            id = perfilNormalizado.id,
                            nombre = perfilNormalizado.nombre,
                            codigo = perfilNormalizado.codigo,
                            facultad = perfilNormalizado.facultad,
                            correoInstitucional = perfilNormalizado.correoInstitucional
                        )
                        client.from("perfiles").upsert(basico) {
                            onConflict = "codigo"
                        }
                    }
                }
            }
        }
    }

    /**
     * Actualiza la contraseña en Supabase y memoria local, marcando debe_cambiar_contrasena = false.
     */
    suspend fun actualizarContrasena(codigo: String, nuevaContrasena: String): Boolean {
        val codigoLimpio = codigo.trim()
        val actual = perfilesLocales[codigoLimpio] ?: perfilActivo
        if (actual != null) {
            val actualizado = actual.copy(
                contrasena = nuevaContrasena,
                debeCambiarContrasena = false
            )
            perfilesLocales[codigoLimpio] = actualizado
            if (perfilActivo?.codigo.equals(codigoLimpio, ignoreCase = true)) {
                perfilActivo = actualizado
            }
        }

        if (isConfigured) {
            return withContext(Dispatchers.IO) {
                runCatching {
                    client.from("perfiles").update(
                        ActualizarContrasenaDto(
                            contrasena = nuevaContrasena,
                            debeCambiarContrasena = false
                        )
                    ) {
                        filter {
                            eq("codigo", codigoLimpio)
                        }
                    }
                    true
                }.getOrElse { true }
            }
        }
        return true
    }

    /**
     * Función suspendida buscarPerfilPorCodigo: consulta a la tabla 'perfiles' de Supabase y devuelve el objeto o null.
     */
    suspend fun buscarPerfilPorCodigo(codigo: String): PerfilEstudiante? {
        val codigoLimpio = codigo.trim()
        if (codigoLimpio.isEmpty()) return null

        if (isConfigured) {
            val remoto = withContext(Dispatchers.IO) {
                runCatching {
                    client.from("perfiles")
                        .select {
                            filter {
                                eq("codigo", codigoLimpio)
                            }
                        }
                        .decodeSingleOrNull<PerfilEstudiante>()
                }.getOrNull()
            }
            if (remoto != null) {
                perfilesLocales[remoto.codigo] = remoto
                perfilActivo = remoto
                return remoto
            }
        }

        return perfilesLocales[codigoLimpio]
    }

    /**
     * Tabla 'liquidaciones': inserción remota de LiquidacionMatricula.
     */
    suspend fun guardarLiquidacion(liquidacion: LiquidacionMatricula) {
        val index = liquidacionesLocales.indexOfFirst { it.id == liquidacion.id }
        if (index >= 0) {
            liquidacionesLocales[index] = liquidacion
        } else {
            liquidacionesLocales.add(liquidacion)
        }

        if (isConfigured) {
            withContext(Dispatchers.IO) {
                runCatching {
                    client.from("liquidaciones").insert(liquidacion)
                }.getOrNull()

                runCatching {
                    val todosCursos = obtenerCursosRemotos()
                    val nombresCursos = liquidacion.cursoLibre.split(",").map { it.trim() }
                    for (nombreCurso in nombresCursos) {
                        val curso = todosCursos.firstOrNull { it.nombre.equals(nombreCurso, ignoreCase = true) }
                        if (curso != null) {
                            @kotlinx.serialization.Serializable
                            data class RelacionLiqCurso(
                                val liquidacion_id: Int,
                                val curso_id: Int
                            )
                            client.from("liquidaciones_cursos").upsert(
                                RelacionLiqCurso(
                                    liquidacion_id = liquidacion.id,
                                    curso_id = curso.id
                                )
                            )
                        }
                    }
                }.getOrNull()
            }
        }
    }

    /**
     * Función suspendida obtenerLiquidacionesPorCodigo: recupera las liquidaciones guardadas en la nube filtrando por 'codigoEstudiante'.
     */
    suspend fun obtenerLiquidacionesPorCodigo(codigo: String): List<LiquidacionMatricula> {
        val codigoLimpio = codigo.trim()

        if (isConfigured) {
            val remotas = withContext(Dispatchers.IO) {
                runCatching {
                    client.from("liquidaciones")
                        .select {
                            filter {
                                eq("codigo_estudiante", codigoLimpio)
                            }
                        }
                        .decodeList<LiquidacionMatricula>()
                }.getOrNull()
            }
            if (remotas != null) {
                remotas.forEach { remota ->
                    val idx = liquidacionesLocales.indexOfFirst { it.id == remota.id }
                    if (idx >= 0) liquidacionesLocales[idx] = remota else liquidacionesLocales.add(remota)
                }
                return remotas
            }
        }

        return liquidacionesLocales.filter { it.codigoEstudiante == codigoLimpio }
    }

    suspend fun obtenerSiguienteIdLiquidacion(): Int {
        val todas = obtenerTodasLasLiquidaciones()
        val maxId = todas.maxOfOrNull { it.id } ?: liquidacionesLocales.maxOfOrNull { it.id } ?: 0
        return maxId + 1
    }

    suspend fun obtenerCursosRemotos(): List<CursoLibre> {
        if (isConfigured) {
            val remotas = withContext(ioDispatcher) {
                runCatching {
                    client.from("cursos")
                        .select()
                        .decodeList<CursoLibre>()
                }.getOrNull()
            }
            if (remotas != null && remotas.isNotEmpty()) {
                return remotas
            }
        }
        return emptyList()
    }

    suspend fun guardarCursoRemoto(curso: CursoLibre) {
        if (isConfigured) {
            withContext(ioDispatcher) {
                runCatching {
                    client.from("cursos").upsert(curso) {
                        onConflict = "nombre"
                    }
                }.getOrNull()
            }
        }
    }

    suspend fun eliminarCursoRemoto(nombre: String) {
        if (isConfigured) {
            withContext(ioDispatcher) {
                runCatching {
                    client.from("cursos").delete {
                        filter {
                            eq("nombre", nombre)
                        }
                    }
                }.getOrNull()
            }
        }
    }

    suspend fun obtenerSiguienteIdBeca(): Int {
        val todas = obtenerBecasRemotas()
        val maxId = todas.maxOfOrNull { it.id } ?: becasLocales.maxOfOrNull { it.id } ?: 0
        return maxId + 1
    }

    suspend fun obtenerBecasRemotas(): List<Beca> {
        if (isConfigured) {
            val remotas = withContext(ioDispatcher) {
                runCatching {
                    client.from("becas")
                        .select()
                        .decodeList<Beca>()
                }.getOrNull()
            }
            if (remotas != null && remotas.isNotEmpty()) {
                remotas.forEach { remota ->
                    val idx = becasLocales.indexOfFirst { it.id == remota.id || it.nombre.equals(remota.nombre, ignoreCase = true) }
                    if (idx >= 0) becasLocales[idx] = remota else becasLocales.add(remota)
                }
                return remotas
            }
        }
        return becasLocales.toList()
    }

    suspend fun guardarBecaRemoto(beca: Beca) {
        val idx = becasLocales.indexOfFirst { it.id == beca.id || it.nombre.equals(beca.nombre, ignoreCase = true) }
        if (idx >= 0) becasLocales[idx] = beca else becasLocales.add(beca)

        if (isConfigured) {
            withContext(ioDispatcher) {
                runCatching {
                    client.from("becas").upsert(beca) {
                        onConflict = "nombre"
                    }
                }.getOrNull()
            }
        }
    }

    suspend fun eliminarBecaRemoto(id: Int) {
        becasLocales.removeAll { it.id == id }

        if (isConfigured) {
            withContext(ioDispatcher) {
                runCatching {
                    client.from("becas").delete {
                        filter {
                            eq("id", id)
                        }
                    }
                }.getOrNull()
            }
        }
    }

    suspend fun obtenerTodasLasLiquidaciones(): List<LiquidacionMatricula> {
        if (isConfigured) {
            val remotas = withContext(Dispatchers.IO) {
                runCatching {
                    client.from("liquidaciones")
                        .select()
                        .decodeList<LiquidacionMatricula>()
                }.getOrNull()
            }
            if (remotas != null) return remotas
        }
        return liquidacionesLocales.toList()
    }

    suspend fun obtenerTodosLosPerfilesRemotos(): List<PerfilEstudiante> {
        if (isConfigured) {
            val remotas = withContext(Dispatchers.IO) {
                runCatching {
                    client.from("perfiles")
                        .select()
                        .decodeList<PerfilEstudiante>()
                }.getOrNull()
            }
            if (remotas != null) {
                remotas.forEach { perfilesLocales[it.codigo] = it }
                return remotas
            }
        }
        return perfilesLocales.values.toList()
    }

    fun limpiarSesionActiva() {
        perfilActivo = null
    }

    fun obtenerPerfil(): PerfilEstudiante? = perfilActivo ?: perfilesLocales.values.lastOrNull()

    fun obtenerLiquidaciones(): List<LiquidacionMatricula> = liquidacionesLocales.toList()

    fun obtenerTodosLosPerfiles(): List<PerfilEstudiante> = perfilesLocales.values.toList()

    suspend fun agregarLiquidacion(liquidacion: LiquidacionMatricula) = guardarLiquidacion(liquidacion)
}
