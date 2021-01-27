package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.api.ExchangeRatesApi
import com.ssho.fromustoeu.data.dto.ExchangeRatesDTO
import com.ssho.fromustoeu.data.model.ConversionData

class ExRatesRemoteDataSource(private val conversionDataMapper: ConversionDataMapper,
                              exchangeRatesApi: ExchangeRatesApi) {

    private val exchangeRatesFetcher = ExchangeRatesFetcher(exchangeRatesApi)

    suspend fun getConversionData(sourceMeasureSystem: Int): ConversionData {
        val fetchedRates = fetchLatestExchangeRates()

        return conversionDataMapper.map(fetchedRates, sourceMeasureSystem)
    }

    private suspend fun fetchLatestExchangeRates(): ExchangeRatesDTO {

        return exchangeRatesFetcher.fetchLatestExchangeRates()
    }
}