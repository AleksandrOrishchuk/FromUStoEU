package com.ssho.fromustoeu.converters

interface Converter {
    fun convert(sourceValue: Double, convertTargetName: String): Double

    fun setExtras(rates: HashMap<String, Double>)

    fun setExtras(sourceCurrency: String)
}