package com.ssho.fromustoeu.data.converters

interface Converter {
    fun convert(sourceValue: Double, convertTargetName: String): Double

    fun setExtras(rates: Map<String, Double>)

    fun setExtras(sourceCurrency: String)
}