package com.uts.serviciosestudiantiles.data

class ServiciosRepository {
    private var perfil: PerfilEstudiante? = null
    private val liquidaciones: MutableList<LiquidacionMatricula> = mutableListOf()

    fun guardarPerfil(perfil: PerfilEstudiante) {
        this.perfil = perfil
    }

    fun obtenerPerfil(): PerfilEstudiante? {
        return perfil
    }

    fun agregarLiquidacion(liquidacion: LiquidacionMatricula) {
        liquidaciones.add(liquidacion)
    }

    fun obtenerLiquidaciones(): List<LiquidacionMatricula> {
        return liquidaciones.toList()
    }
}
