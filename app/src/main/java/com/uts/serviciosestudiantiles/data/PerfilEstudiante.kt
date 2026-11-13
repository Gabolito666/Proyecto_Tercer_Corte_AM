package com.uts.serviciosestudiantiles.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerfilEstudiante(
    @SerialName("id") val id: Int = 1,
    @SerialName("nombre") val nombre: String = "",
    @SerialName("codigo") val codigo: String = "",
    @SerialName("facultad") val facultad: String = "",
    @SerialName("correo_institucional") val correoInstitucional: String = ""
)
