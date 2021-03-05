package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.database.ExchangeRatesDao
import com.ssho.fromustoeu.data.database.entities.ExchangeRatesEntity
import com.ssho.fromustoeu.data.model.ExchangeRatesData

private const val TAG = "RatesLocalDS"

class ExRatesLocalDataSource(
    private val exchangeRatesDataMapper: ExchangeRatesDataMapper,
    private val exchangeRatesDao: ExchangeRatesDao
) {

    suspend fun cacheConversionData(exchangeRatesData: ExchangeRatesData) {
        val exchangeRates = exchangeRatesDataMapper.map(exchangeRatesData)
        exchangeRatesDao.cacheExchangeRates(exchangeRates)
    }

    suspend fun getConversionData(): ExchangeRatesData {
        val cachedExchangeRates = getCachedExchangeRates()
        if (cachedExchangeRates.isEmpty())
            throw LocalDataSourceEmptyException()

        val cachedExchangeRatesDTO = cachedExchangeRates[0]

        return exchangeRatesDataMapper.map(cachedExchangeRatesDTO)
    }

    private suspend fun getCachedExchangeRates(): List<ExchangeRatesEntity> {
        return exchangeRatesDao.getCachedExchangeRates()
    }

}

class LocalDataSourceEmptyException : RuntimeException()
