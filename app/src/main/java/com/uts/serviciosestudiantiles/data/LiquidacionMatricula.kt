package com.uts.serviciosestudiantiles.data

data class LiquidacionMatricula(
    val id: Int,
    val cursoLibre: String,
    val costoBase: Double,
    val tieneBeca: Boolean,
    val tieneIcetex: Boolean,
    val totalAPagar: Double
)
