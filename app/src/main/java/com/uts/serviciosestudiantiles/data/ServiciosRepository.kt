package com.uts.serviciosestudiantiles.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ServiciosRepository(
    private val client: SupabaseClient = supabaseClient
) {
    private var perfil: PerfilEstudiante? = null
    private val liquidaciones: MutableList<LiquidacionMatricula> = mutableListOf()

    suspend fun guardarPerfil(perfil: PerfilEstudiante) = withContext(Dispatchers.IO) {
        this@ServiciosRepository.perfil = perfil
        runCatching {
            client.from("perfiles").upsert(perfil) {
                onConflict = "codigo"
            }
        }.getOrThrow()
    }

    suspend fun buscarPerfilPorCodigo(codigo: String): PerfilEstudiante? = withContext(Dispatchers.IO) {
        runCatching {
            client.from("perfiles")
                .select {
                    filter {
                        eq("codigo", codigo)
                    }
                }
                .decodeSingleOrNull<PerfilEstudiante>()
        }.getOrNull() ?: perfil?.takeIf { it.codigo == codigo }
    }

    suspend fun guardarLiquidacion(liquidacion: LiquidacionMatricula) = withContext(Dispatchers.IO) {
        liquidaciones.add(liquidacion)
        runCatching {
            client.from("liquidaciones").insert(liquidacion)
        }.getOrThrow()
    }

    suspend fun agregarLiquidacion(liquidacion: LiquidacionMatricula) = guardarLiquidacion(liquidacion)

    suspend fun obtenerLiquidacionesPorCodigo(codigo: String): List<LiquidacionMatricula> = withContext(Dispatchers.IO) {
        runCatching {
            client.from("liquidaciones")
                .select {
                    filter {
                        eq("codigo_estudiante", codigo)
                    }
                }
                .decodeList<LiquidacionMatricula>()
        }.getOrElse {
            liquidaciones.filter { it.codigoEstudiante == codigo || codigo.isEmpty() }
        }
    }

    fun obtenerPerfil(): PerfilEstudiante? = perfil

    fun obtenerLiquidaciones(): List<LiquidacionMatricula> = liquidaciones.toList()
}
