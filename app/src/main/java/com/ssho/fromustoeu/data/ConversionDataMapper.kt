package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.ui.FROM_IMPERIAL_US
import com.ssho.fromustoeu.data.database.MeasureBucket
import com.ssho.fromustoeu.data.dto.ExchangeRatesDTO
import com.ssho.fromustoeu.data.model.ConversionData
import com.ssho.fromustoeu.data.model.ConversionPair
import java.util.*

class ConversionDataMapper {

    fun map(measureBuckets: List<MeasureBucket>): ConversionData {
        val conversionPairs = measureBuckets.map {
            ConversionPair(it.sourceUnitName, it.targetUnitName)
        }

        return ConversionData(conversionPairs = conversionPairs)
    }

    fun map(exchangeRatesDTO: ExchangeRatesDTO, sourceMeasureSystem: Int): ConversionData {
        val rates = exchangeRatesDTO.rates
        val baseCurrencyUsd = exchangeRatesDTO.base.toLowerCase(Locale.ROOT)
        val serverDate = exchangeRatesDTO.serverDate
        val cachedDate = exchangeRatesDTO.cachedDate
        val conversionPairs = mutableListOf<ConversionPair>()

        val isSourceCurrencyUSD = sourceMeasureSystem == FROM_IMPERIAL_US
        if (isSourceCurrencyUSD)
            rates.forEach {
                val pair = ConversionPair(baseCurrencyUsd, it.key)
                conversionPairs.add(pair)
            }
        else
            rates.forEach {
                val pair = ConversionPair(it.key, baseCurrencyUsd)
                conversionPairs.add(pair)
            }

        return ConversionData(
                exchangeRates = rates,
                baseCurrency = baseCurrencyUsd,
                serverDataUpdatedDate = serverDate,
                cachedDataDate = cachedDate,
                conversionPairs = conversionPairs
        )
    }

    fun map(conversionData: ConversionData): ExchangeRatesDTO {
        return ExchangeRatesDTO(
                base = conversionData.baseCurrency,
                serverDate = conversionData.serverDataUpdatedDate,
                cachedDate = conversionData.cachedDataDate,
                rates = conversionData.exchangeRates
        )
    }
}