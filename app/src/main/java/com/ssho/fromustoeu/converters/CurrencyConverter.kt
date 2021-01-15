package com.ssho.fromustoeu.converters

import kotlin.math.abs

class CurrencyConverter : Converter {
    private var sourceCurrencyName: String = ""
    private var rates = HashMap<String, Double>()

    override fun convert(sourceValue: Double, convertTargetName: String): Double {
        val value = abs(sourceValue)

        return if (sourceCurrencyName == "usd") {
            val rate = rates[convertTargetName] ?: 1.0
            value * rate
        } else {
            val rate = rates[sourceCurrencyName] ?: 1.0
            value / rate
        }
    }

    override fun setExtras(rates: HashMap<String, Double>) {
        this.rates = rates
    }

    override fun setExtras(sourceCurrency: String) {
        sourceCurrencyName = sourceCurrency
    }
}