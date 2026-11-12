package com.uts.serviciosestudiantiles.data

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class ServiciosRepository(
    context: Context? = null
) {
    private val appContext: Context? = context ?: globalContext

    // Almacenamiento en memoria para acceso rápido, unicidad y pruebas unitarias
    private val perfilesMap = mutableMapOf<String, PerfilEstudiante>()
    private val liquidacionesList = mutableListOf<LiquidacionMatricula>()
    private var perfilActivo: PerfilEstudiante? = null

    private val sharedPrefs: SharedPreferences? by lazy {
        try {
            appContext?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        } catch (_: Exception) {
            null
        }
    }

    private val jsonFile: File? by lazy {
        try {
            appContext?.filesDir?.let { File(it, FILE_NAME) }
        } catch (_: Exception) {
            null
        }
    }

    init {
        cargarDesdePersistencia()
    }

    @Synchronized
    fun guardarPerfil(perfil: PerfilEstudiante) {
        val codigoLimpio = perfil.codigo.trim()
        val perfilNormalizado = perfil.copy(codigo = codigoLimpio)
        // El código del estudiante actúa como ID único (clave del mapa)
        perfilesMap[codigoLimpio] = perfilNormalizado
        perfilActivo = perfilNormalizado
        guardarEnPersistencia()
    }

    @Synchronized
    fun buscarPerfilPorCodigo(codigo: String): PerfilEstudiante? {
        val codigoLimpio = codigo.trim()
        return perfilesMap[codigoLimpio]
    }

    @Synchronized
    fun guardarLiquidacion(liquidacion: LiquidacionMatricula) {
        val index = liquidacionesList.indexOfFirst { it.id == liquidacion.id }
        if (index >= 0) {
            liquidacionesList[index] = liquidacion
        } else {
            liquidacionesList.add(liquidacion)
        }
        guardarEnPersistencia()
    }

    @Synchronized
    fun obtenerLiquidacionesPorCodigo(codigo: String): List<LiquidacionMatricula> {
        val codigoLimpio = codigo.trim()
        return liquidacionesList.filter { it.codigoEstudiante == codigoLimpio }
    }

    @Synchronized
    fun obtenerTodasLasLiquidaciones(): List<LiquidacionMatricula> {
        return liquidacionesList.toList()
    }

    @Synchronized
    fun obtenerPerfil(): PerfilEstudiante? {
        return perfilActivo ?: perfilesMap.values.lastOrNull()
    }

    @Synchronized
    fun agregarLiquidacion(liquidacion: LiquidacionMatricula) {
        guardarLiquidacion(liquidacion)
    }

    @Synchronized
    fun obtenerLiquidaciones(): List<LiquidacionMatricula> {
        return obtenerTodasLasLiquidaciones()
    }

    @Synchronized
    fun obtenerLiquidacionesPorEstudiante(codigo: String): List<LiquidacionMatricula> {
        return obtenerLiquidacionesPorCodigo(codigo)
    }

    @Synchronized
    fun obtenerTodosLosPerfiles(): List<PerfilEstudiante> {
        return perfilesMap.values.toList()
    }

    @Synchronized
    private fun guardarEnPersistencia() {
        if (appContext == null) return
        try {
            val perfilesJson = JSONArray()
            perfilesMap.values.forEach { perfilesJson.put(it.toJson()) }

            val liquidacionesJson = JSONArray()
            liquidacionesList.forEach { liquidacionesJson.put(it.toJson()) }

            val ultimoCodigo = perfilActivo?.codigo ?: ""

            // 1. Guardar en SharedPreferences
            sharedPrefs?.edit()?.apply {
                putString(KEY_PERFILES, perfilesJson.toString())
                putString(KEY_LIQUIDACIONES, liquidacionesJson.toString())
                putString(KEY_ULTIMO_CODIGO, ultimoCodigo)
                apply()
            }

            // 2. Guardar en archivo JSON en Context.filesDir
            jsonFile?.let { file ->
                val root = JSONObject().apply {
                    put("perfiles", perfilesJson)
                    put("liquidaciones", liquidacionesJson)
                    put("ultimoCodigo", ultimoCodigo)
                }
                file.writeText(root.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    private fun cargarDesdePersistencia() {
        if (appContext == null) return
        try {
            var cargado = false

            // Intentar cargar desde SharedPreferences
            val perfilesRaw = sharedPrefs?.getString(KEY_PERFILES, null)
            val liquidacionesRaw = sharedPrefs?.getString(KEY_LIQUIDACIONES, null)
            val ultimoCodigo = sharedPrefs?.getString(KEY_ULTIMO_CODIGO, null)

            if (!perfilesRaw.isNullOrBlank()) {
                val array = JSONArray(perfilesRaw)
                for (i in 0 until array.length()) {
                    val p = array.getJSONObject(i).toPerfilEstudiante()
                    perfilesMap[p.codigo] = p
                }
                cargado = true
            }

            if (!liquidacionesRaw.isNullOrBlank()) {
                liquidacionesList.clear()
                val array = JSONArray(liquidacionesRaw)
                for (i in 0 until array.length()) {
                    liquidacionesList.add(array.getJSONObject(i).toLiquidacionMatricula())
                }
                cargado = true
            }

            // Fallback a Context.filesDir si SharedPreferences estaba vacío
            if (!cargado && jsonFile?.exists() == true) {
                val content = jsonFile?.readText().orEmpty()
                if (content.isNotBlank()) {
                    val root = JSONObject(content)
                    val pArray = root.optJSONArray("perfiles") ?: JSONArray()
                    for (i in 0 until pArray.length()) {
                        val p = pArray.getJSONObject(i).toPerfilEstudiante()
                        perfilesMap[p.codigo] = p
                    }

                    val lArray = root.optJSONArray("liquidaciones") ?: JSONArray()
                    liquidacionesList.clear()
                    for (i in 0 until lArray.length()) {
                        liquidacionesList.add(lArray.getJSONObject(i).toLiquidacionMatricula())
                    }
                }
            }

            if (!ultimoCodigo.isNullOrBlank()) {
                perfilActivo = perfilesMap[ultimoCodigo]
            } else if (perfilActivo == null && perfilesMap.isNotEmpty()) {
                perfilActivo = perfilesMap.values.lastOrNull()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        var globalContext: Context? = null
        private const val PREFS_NAME = "uts_servicios_estudiantiles_prefs"
        private const val FILE_NAME = "servicios_estudiantiles.json"
        private const val KEY_PERFILES = "key_perfiles_json"
        private const val KEY_LIQUIDACIONES = "key_liquidaciones_json"
        private const val KEY_ULTIMO_CODIGO = "key_ultimo_codigo"
    }
}

private fun PerfilEstudiante.toJson(): JSONObject {
    return JSONObject().apply {
        put("id", id)
        put("nombre", nombre)
        put("codigo", codigo)
        put("facultad", facultad)
        put("correoInstitucional", correoInstitucional)
    }
}

private fun JSONObject.toPerfilEstudiante(): PerfilEstudiante {
    return PerfilEstudiante(
        id = optInt("id", 1),
        nombre = optString("nombre", ""),
        codigo = optString("codigo", ""),
        facultad = optString("facultad", "Ingenierías"),
        correoInstitucional = optString("correoInstitucional", "")
    )
}

private fun LiquidacionMatricula.toJson(): JSONObject {
    return JSONObject().apply {
        put("id", id)
        put("codigoEstudiante", codigoEstudiante)
        put("cursoLibre", cursoLibre)
        put("costoBase", costoBase)
        put("tieneBeca", tieneBeca)
        put("tieneIcetex", tieneIcetex)
        put("totalAPagar", totalAPagar)
    }
}

private fun JSONObject.toLiquidacionMatricula(): LiquidacionMatricula {
    return LiquidacionMatricula(
        id = optInt("id", 1),
        codigoEstudiante = optString("codigoEstudiante", ""),
        cursoLibre = optString("cursoLibre", ""),
        costoBase = optDouble("costoBase", 0.0),
        tieneBeca = optBoolean("tieneBeca", false),
        tieneIcetex = optBoolean("tieneIcetex", false),
        totalAPagar = optDouble("totalAPagar", 0.0)
    )
}
