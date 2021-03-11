package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.api.ExchangeRatesResponse
import com.ssho.fromustoeu.data.database.entities.ExchangeRatesEntity
import com.ssho.fromustoeu.data.model.ExchangeRatesData
import java.util.*
import kotlin.reflect.full.memberProperties

class ExchangeRatesDataMapper {

//    fun map(measureBuckets: List<MeasureBucket>): ExchangeRatesData {
//        val conversionPairs = measureBuckets.map {
//            ConversionPair(it.sourceUnitName, it.targetUnitName)
//        }
//
//        return ExchangeRatesData(conversionPairs = conversionPairs)
//    }

    fun map(exchangeRatesEntity: ExchangeRatesEntity): ExchangeRatesData {
        return ExchangeRatesData(
            exchangeRates = exchangeRatesEntity.rates,
            baseCurrency = exchangeRatesEntity.base,
            serverDataUpdatedDate = exchangeRatesEntity.serverDate,
            cachedDataDate = exchangeRatesEntity.cachedDate,
        )
    }

    fun map(response: ExchangeRatesResponse): ExchangeRatesData {
        val rates = response.rates

        val ratesMap = HashMap<String, Double>()
        rates.javaClass.kotlin.memberProperties.forEach { property ->
            val key = property.name
            val value = property.get(rates) as Double
            ratesMap[key] = value
        }

        val sourceCurrency = response.base.toLowerCase(Locale.ROOT)


        return ExchangeRatesData(
            exchangeRates = ratesMap,
            baseCurrency = sourceCurrency,
            serverDataUpdatedDate = response.date,
            cachedDataDate = Date(),
        )
    }

    fun map(exchangeRatesData: ExchangeRatesData): ExchangeRatesEntity {
        return ExchangeRatesEntity(
            base = exchangeRatesData.baseCurrency,
            serverDate = exchangeRatesData.serverDataUpdatedDate,
            cachedDate = exchangeRatesData.cachedDataDate,
            rates = exchangeRatesData.exchangeRates
        )
    }
}
