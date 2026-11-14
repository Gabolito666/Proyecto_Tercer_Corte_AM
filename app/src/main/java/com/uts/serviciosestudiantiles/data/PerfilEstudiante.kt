package com.uts.serviciosestudiantiles.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerfilEstudiante(
    @SerialName("id") val id: Int = 1,
    @SerialName("nombre") val nombre: String = "",
    @SerialName("codigo") val codigo: String = "",
    @SerialName("facultad") val facultad: String = "Ingenierías",
    @SerialName("correo_institucional") val correoInstitucional: String = "",
    @SerialName("contrasena") val contrasena: String? = null,
    @SerialName("debe_cambiar_contrasena") val debeCambiarContrasena: Boolean? = true
) {
    /**
     * Retorna la contraseña activa del usuario. Si aún no está guardada explícitamente en la BD,
     * se asigna por defecto su mismo código (o "admin" si es el administrador).
     */
    val contrasenaEfectiva: String
        get() = if (!contrasena.isNullOrBlank()) {
            contrasena
        } else if (codigo.equals("ADMIN", ignoreCase = true)) {
            "admin"
        } else {
            codigo
        }

    /**
     * Determina si el usuario debe cambiar su contraseña por motivos de seguridad.
     * Para ADMIN siempre es false. Para estudiantes, es true si el flag está activo o si su contraseña
     * es igual a su código.
     */
    val requiereCambioContrasena: Boolean
        get() = if (codigo.equals("ADMIN", ignoreCase = true)) {
            false
        } else {
            debeCambiarContrasena ?: (contrasenaEfectiva == codigo)
        }
}
