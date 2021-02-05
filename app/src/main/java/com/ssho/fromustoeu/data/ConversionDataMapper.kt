package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.api.ExchangeRatesResponse
import com.ssho.fromustoeu.data.database.entities.MeasureBucket
import com.ssho.fromustoeu.data.database.entities.ExchangeRatesEntity
import com.ssho.fromustoeu.data.model.ConversionData
import com.ssho.fromustoeu.data.model.ConversionPair
import java.util.*
import kotlin.reflect.full.memberProperties

class ConversionDataMapper {

    fun map(measureBuckets: List<MeasureBucket>): ConversionData {
        val conversionPairs = measureBuckets.map {
            ConversionPair(it.sourceUnitName, it.targetUnitName)
        }

        return ConversionData(conversionPairs = conversionPairs)
    }

    fun map(exchangeRatesEntity: ExchangeRatesEntity): ConversionData {
        val rates = exchangeRatesEntity.rates
        val sourceCurrency = exchangeRatesEntity.base

        val conversionPairs = rates.map {
            ConversionPair(
                    sourceUnitName = sourceCurrency,
                    targetUnitName = it.key
            )
        }

        return ConversionData(
                exchangeRates = rates,
                baseCurrency = sourceCurrency,
                serverDataUpdatedDate = exchangeRatesEntity.serverDate,
                cachedDataDate = exchangeRatesEntity.cachedDate,
                conversionPairs = conversionPairs
        )
    }

    fun map(response: ExchangeRatesResponse): ConversionData {
        val rates = response.rates

        val ratesMap = HashMap<String, Double>()
        rates.javaClass.kotlin.memberProperties.forEach { property ->
            val key = property.name
            val value = property.get(rates) as Double
            ratesMap[key] = value
        }

        val sourceCurrency = response.base.toLowerCase(Locale.ROOT)
        val conversionPairs = ratesMap.map {
            ConversionPair(
                    sourceUnitName = sourceCurrency,
                    targetUnitName = it.key
            )
        }

        return ConversionData(
                exchangeRates = ratesMap,
                baseCurrency = sourceCurrency,
                serverDataUpdatedDate = response.date,
                cachedDataDate = Date(),
                conversionPairs = conversionPairs
        )
    }

    fun map(conversionData: ConversionData): ExchangeRatesEntity {
        return ExchangeRatesEntity(
                base = conversionData.baseCurrency,
                serverDate = conversionData.serverDataUpdatedDate,
                cachedDate = conversionData.cachedDataDate,
                rates = conversionData.exchangeRates
        )
    }
}