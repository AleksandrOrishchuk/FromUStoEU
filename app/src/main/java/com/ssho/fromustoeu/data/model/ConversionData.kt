package com.ssho.fromustoeu.data.model

import java.util.*

data class ConversionData(
                          val exchangeRates: Map<String, Double> = hashMapOf(),
                          val baseCurrency: String = "USD",
                          val serverDataUpdatedDate: String = "",
                          val cachedDataDate: Date = Date(),
                          val conversionPairs: List<ConversionPair> = emptyList(),
) {
    fun swapConversionPairs(): ConversionData {
        conversionPairs.map {
            val newTargetName = it.sourceUnitName
            ConversionPair(
                    sourceUnitName = it.targetUnitName,
                    targetUnitName = newTargetName
            )
        }.let {
            return this.copy(conversionPairs = it)
        }
    }
}