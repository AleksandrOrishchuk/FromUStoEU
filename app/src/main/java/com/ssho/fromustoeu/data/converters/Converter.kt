package com.ssho.fromustoeu.data.converters

interface Converter {
    fun convert(sourceName: String, sourceValue: Double, targetName: String): Double
}