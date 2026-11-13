package com.uts.serviciosestudiantiles.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
        val perfilNormalizado = perfil.copy(codigo = codigoLimpio)
        perfilesLocales[codigoLimpio] = perfilNormalizado
        perfilActivo = perfilNormalizado

        if (isConfigured) {
            withContext(Dispatchers.IO) {
                runCatching {
                    client.from("perfiles").upsert(perfilNormalizado) {
                        onConflict = "codigo"
                    }
                }.getOrNull()
            }
        }
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

    fun obtenerPerfil(): PerfilEstudiante? = perfilActivo ?: perfilesLocales.values.lastOrNull()

    fun obtenerLiquidaciones(): List<LiquidacionMatricula> = liquidacionesLocales.toList()

    fun obtenerTodosLosPerfiles(): List<PerfilEstudiante> = perfilesLocales.values.toList()

    suspend fun agregarLiquidacion(liquidacion: LiquidacionMatricula) = guardarLiquidacion(liquidacion)
}
