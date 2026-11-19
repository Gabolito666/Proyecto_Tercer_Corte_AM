package com.uts.serviciosestudiantiles.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Beca(
    @SerialName("id") val id: Int = 0,
    @SerialName("nombre") val nombre: String = "",
    @SerialName("porcentaje_descuento") val porcentajeDescuento: Double = 0.50,
    @SerialName("requisitos") val requisitos: String = "",
    @SerialName("activa") val activa: Boolean = true
)
