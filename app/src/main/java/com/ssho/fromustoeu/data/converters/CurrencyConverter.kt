package com.ssho.fromustoeu.data.converters

import kotlin.math.abs

class CurrencyConverter(private val exchangeRates: Map<String, Double>) : Converter {

    override fun convert(sourceName: String, sourceValue: Double, targetName: String): Double {
        val value = abs(sourceValue)

        return if (sourceName == "usd") {
            val rate = exchangeRates[targetName] ?: 1.0
            value * rate
        } else {
            val rate = exchangeRates[sourceName] ?: 1.0
            value / rate
        }
    }
}