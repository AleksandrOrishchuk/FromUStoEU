package com.ssho.fromustoeu.data.model

import java.util.*

data class ExchangeRatesData(
    val exchangeRates: Map<String, Double> = hashMapOf(),
    val baseCurrency: String = "USD",
    val serverDataUpdatedDate: String = "",
    val cachedDataDate: Date = Date(),
)
