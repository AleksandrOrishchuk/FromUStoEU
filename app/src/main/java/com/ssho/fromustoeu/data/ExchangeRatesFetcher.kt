package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.api.ExchangeRatesApi
import com.ssho.fromustoeu.data.dto.ExchangeRatesDTO
import java.util.*
import kotlin.reflect.full.memberProperties

class ExchangeRatesFetcher(private val exchangeRatesApi: ExchangeRatesApi) {
    suspend fun fetchLatestExchangeRates(): ExchangeRatesDTO {
        val exchangeRatesResponse = exchangeRatesApi.fetchLatestExchangeRates()
        val rates = exchangeRatesResponse.rates

        val map = HashMap<String, Double>()
        rates.javaClass.kotlin.memberProperties.forEach { property ->
            val key = property.name
            val value = property.get(rates) as Double
            map[key] = value
        }

        return ExchangeRatesDTO(
                base = exchangeRatesResponse.base,
                serverDate = exchangeRatesResponse.date,
                cachedDate = Date(),
                rates = map)
    }
}