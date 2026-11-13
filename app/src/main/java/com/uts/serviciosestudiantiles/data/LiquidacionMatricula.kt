package com.uts.serviciosestudiantiles.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiquidacionMatricula(
    @SerialName("id") val id: Int = 0,
    @SerialName("codigo_estudiante") val codigoEstudiante: String = "",
    @SerialName("curso_libre") val cursoLibre: String = "",
    @SerialName("costo_base") val costoBase: Double = 0.0,
    @SerialName("tiene_beca") val tieneBeca: Boolean = false,
    @SerialName("tiene_icetex") val tieneIcetex: Boolean = false,
    @SerialName("total_a_pagar") val totalAPagar: Double = 0.0,
    @SerialName("curso_id") val cursoId: Int? = null,
    @SerialName("beca_id") val becaId: Int? = null
)
