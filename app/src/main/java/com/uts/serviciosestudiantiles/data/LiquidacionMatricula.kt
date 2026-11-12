package com.uts.serviciosestudiantiles.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiquidacionMatricula(
    @SerialName("id") val id: Int = 0,
    @SerialName("codigoEstudiante") val codigoEstudiante: String = "",
    @SerialName("cursoLibre") val cursoLibre: String = "",
    @SerialName("costoBase") val costoBase: Double = 0.0,
    @SerialName("tieneBeca") val tieneBeca: Boolean = false,
    @SerialName("tieneIcetex") val tieneIcetex: Boolean = false,
    @SerialName("totalAPagar") val totalAPagar: Double = 0.0
)
