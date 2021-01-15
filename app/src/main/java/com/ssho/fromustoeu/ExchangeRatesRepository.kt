package com.ssho.fromustoeu

import android.text.format.DateFormat
import android.util.Log
import com.ssho.fromustoeu.api.ExchangeRatesApi
import com.ssho.fromustoeu.database.ExchangeRatesDao
import java.util.*

private const val TAG = "ExchangeRepository"
private const val DATE_FORMAT = "MMM dd, yyyy"

class ExchangeRatesRepository private constructor(
        private val exchangeRatesDao: ExchangeRatesDao,
        private val exchangeRatesApi: ExchangeRatesApi) {

    companion object {
        private var INSTANCE: ExchangeRatesRepository? = null

        fun initialize(exchangeRatesDao: ExchangeRatesDao, exchangeRatesApi: ExchangeRatesApi) {
            if (INSTANCE == null)
                INSTANCE = ExchangeRatesRepository(exchangeRatesDao, exchangeRatesApi)
        }

        fun get(): ExchangeRatesRepository {
            return INSTANCE ?: throw IllegalStateException("ExchangeRatesRepository must be initialized.")
        }
    }

    private val exchangeRatesFetcher = ExchangeRatesFetcher(exchangeRatesApi)

    suspend fun getLatestExchangeRates(): ExchangeRates {
        val cachedExchangeRates = exchangeRatesDao.getCachedExchangeRates()
        if (cachedExchangeRates.isNotEmpty()) {
            val currentDate = DateFormat.format(DATE_FORMAT, Date())
                    .toString()
            val cachedDate = DateFormat.format(DATE_FORMAT, cachedExchangeRates[0].cachedDate)
                    .toString()
            if (currentDate == cachedDate)
                return cachedExchangeRates[0].also {
                    Log.d(TAG, "Got ${it.rates.size} currency rates from DB")
                }
        }

        return fetchAndCacheLatestExchangeRates()
    }

    private suspend fun fetchAndCacheLatestExchangeRates(): ExchangeRates {
        val latestExchangeRates = exchangeRatesFetcher.fetchLatestExchangeRates()
        exchangeRatesDao.cacheExchangeRates(latestExchangeRates)

        return latestExchangeRates.also {
            Log.d(TAG, "Got ${it.rates.size} currency rates from server")
        }
    }
}