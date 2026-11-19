package com.uts.serviciosestudiantiles.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CursoLibre(
    @SerialName("id") val id: Int = 0,
    @SerialName("nombre") val nombre: String = "",
    @SerialName("costo_base") val costoBase: Double = 0.0
)
