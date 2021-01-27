package com.ssho.fromustoeu.data.model

import java.util.*

data class ConversionData(
                          val exchangeRates: Map<String, Double> = hashMapOf(),
                          val baseCurrency: String = "usd",
                          val serverDataUpdatedDate: String = "",
                          val cachedDataDate: Date = Date(),
                          val conversionPairs: List<ConversionPair> = emptyList(),
                          val sourceValue: Double = 0.0
)