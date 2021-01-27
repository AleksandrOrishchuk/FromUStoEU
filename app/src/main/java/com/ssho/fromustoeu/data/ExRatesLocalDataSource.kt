package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.database.ExchangeRatesDao
import com.ssho.fromustoeu.data.dto.ExchangeRatesDTO
import com.ssho.fromustoeu.data.model.ConversionData

class ExRatesLocalDataSource(private val conversionDataMapper: ConversionDataMapper,
                             private val exchangeRatesDao: ExchangeRatesDao) {

    suspend fun cacheConversionData(conversionData: ConversionData) {
        val exchangeRates: ExchangeRatesDTO = conversionDataMapper.map(conversionData)
        exchangeRatesDao.cacheExchangeRates(exchangeRates)
    }

    suspend fun getConversionData(sourceMeasureSystem: Int): ConversionData {
        val cachedExchangeRates = getCachedExchangeRates()
        if (cachedExchangeRates.isEmpty())
            return ConversionData()

        val cachedExchangeRatesDTO = cachedExchangeRates[0]

        return conversionDataMapper.map(cachedExchangeRatesDTO, sourceMeasureSystem)
    }

    private suspend fun getCachedExchangeRates(): List<ExchangeRatesDTO> {
        return exchangeRatesDao.getCachedExchangeRates()
    }

}