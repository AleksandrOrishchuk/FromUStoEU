package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.database.ExchangeRatesDao
import com.ssho.fromustoeu.data.database.entities.ExchangeRatesEntity
import com.ssho.fromustoeu.data.model.ConversionData

private const val TAG = "RatesLocalDS"

class ExRatesLocalDataSource(private val conversionDataMapper: ConversionDataMapper,
                             private val exchangeRatesDao: ExchangeRatesDao) {

    suspend fun cacheConversionData(conversionData: ConversionData) {
        val exchangeRates = conversionDataMapper.map(conversionData)
        exchangeRatesDao.cacheExchangeRates(exchangeRates)
    }

    suspend fun getConversionData(): ResultWrapper<ConversionData> {
        val cachedExchangeRates = getCachedExchangeRates()
        if (cachedExchangeRates.isEmpty())
            return ResultWrapper.GenericError

        val cachedExchangeRatesDTO = cachedExchangeRates[0]
        val conversionData = conversionDataMapper.map(cachedExchangeRatesDTO)

        return ResultWrapper.Success(conversionData)
    }

    private suspend fun getCachedExchangeRates(): List<ExchangeRatesEntity> {
        return exchangeRatesDao.getCachedExchangeRates()
    }

}