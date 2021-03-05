package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.api.ExchangeRatesApi
import com.ssho.fromustoeu.data.model.ExchangeRatesData

class ExRatesRemoteDataSource(
    private val exchangeRatesDataMapper: ExchangeRatesDataMapper,
    private val exchangeRatesApi: ExchangeRatesApi,
) {
    suspend fun getConversionData(): ExchangeRatesData {
        val fetchedResponse = exchangeRatesApi.fetchLatestExchangeRates()

        return exchangeRatesDataMapper.map(fetchedResponse)
    }
}
