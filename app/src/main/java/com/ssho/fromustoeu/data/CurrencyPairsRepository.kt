package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.model.ConversionPair

class CurrencyPairsRepository(
    private val exchangeRatesDataRepository: ExchangeRatesDataRepository
    ) : ConversionPairsRepository {

    override suspend fun getConversionPairs(): List<ConversionPair> {
        val exchangeRatesData = exchangeRatesDataRepository.getExchangeRatesData()
        val rates = exchangeRatesData.exchangeRates

        return rates.map {
            ConversionPair(
                sourceUnitName = exchangeRatesData.baseCurrency,
                targetUnitName = it.key
            )
        }
    }

}
