package com.ssho.fromustoeu

import com.ssho.fromustoeu.converters.CurrencyConverter
import java.util.*

class CurrencyBucketsProvider(
        private val exchangeRatesRepository: ExchangeRatesRepository,
        private val currencyConverter: CurrencyConverter
) : BucketsProvidable {

    override suspend fun getBucketList(measureSystemFrom: Int): List<ConvertBucket> {
        val currencyBuckets = mutableListOf<CurrencyBucket>()
        val exchangeRates = exchangeRatesRepository.getLatestExchangeRates()
        val baseCurrencyUSD = exchangeRates.base.toLowerCase(Locale.ROOT)
        val ratesValidDate = exchangeRates.serverDate

        val isSourceUSD = measureSystemFrom == FROM_IMPERIAL_US

        if (isSourceUSD) {
            exchangeRates.rates.forEach {
                val currencyBucket = CurrencyBucket(
                        ratesValidForDate = ratesValidDate,
                        sourceCurrencyName = baseCurrencyUSD,
                        targetCurrencyName = it.key,
                        conversionRate = it.value
                )
                currencyBuckets.add(currencyBucket)
            }
        } else {
            exchangeRates.rates.forEach {
                val currencyBucket = CurrencyBucket(
                        ratesValidForDate = ratesValidDate,
                        sourceCurrencyName = it.key,
                        targetCurrencyName = baseCurrencyUSD,
                        conversionRate = it.value
                )
                currencyBuckets.add(currencyBucket)
            }
        }

        currencyConverter.setExtras(exchangeRates.rates)


        return currencyBuckets
    }
}