package com.ssho.fromustoeu

import com.ssho.fromustoeu.api.ExchangeRatesApi
import java.util.*
import kotlin.reflect.full.memberProperties

class ExchangeRatesFetcher(private val exchangeRatesApi: ExchangeRatesApi) {
    suspend fun fetchLatestExchangeRates(): ExchangeRates {
        val exchangeRatesResponse = exchangeRatesApi.fetchLatestExchangeRates()
        val rates = exchangeRatesResponse.rates

        val map = HashMap<String, Double>()
        rates.javaClass.kotlin.memberProperties.forEach { property ->
            val key = property.name
            val value = property.get(rates) as Double
            map[key] = value
        }

        return ExchangeRates(
                base = exchangeRatesResponse.base,
                serverDate = exchangeRatesResponse.date,
                cachedDate = Date(),
                rates = map)
    }
}