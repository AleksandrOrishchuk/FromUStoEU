package com.ssho.fromustoeu

data class CurrencyBucket(
        val ratesValidForDate: String = "",
        val sourceCurrencyName: String = "",
        val targetCurrencyName: String = "",
        val conversionRate: Double = 1.0,
        val sourceValueText: String = ""
) : ConvertBucket()