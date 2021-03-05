package com.ssho.fromustoeu.data

import android.text.format.DateFormat
import com.ssho.fromustoeu.data.model.ExchangeRatesData
import java.util.*

private const val TAG = "ExchangeRepository"
private const val DATE_FORMAT = "MMM dd, yyyy"

class ExchangeRatesDataRepositoryImpl(
    private val exRatesLocalDataSource: ExRatesLocalDataSource,
    private val exRatesRemoteDataSource: ExRatesRemoteDataSource
) : ExchangeRatesDataRepository {

    override suspend fun getExchangeRatesData(): ExchangeRatesData {
        return try {
            val cachedConversionData = exRatesLocalDataSource.getConversionData()

            if (isRatesOutdated(cachedConversionData))
                getLatestExchangeRatesData()
            else
                cachedConversionData
        } catch (error: LocalDataSourceEmptyException) {
            getLatestExchangeRatesData()
        }
    }

    override suspend fun getLatestExchangeRatesData(): ExchangeRatesData {
        return getConversionDataFromRemote().also {
            cacheExchangeRatesData(it)
        }
    }

    private suspend fun cacheExchangeRatesData(exchangeRatesData: ExchangeRatesData) {
        exRatesLocalDataSource.cacheConversionData(exchangeRatesData)
    }

    private suspend fun getConversionDataFromRemote(): ExchangeRatesData {
        return exRatesRemoteDataSource.getConversionData()
    }

    private fun isRatesOutdated(cachedExchangeRatesData: ExchangeRatesData): Boolean {
        val currentDate =
            DateFormat
                .format(DATE_FORMAT, Date())
                .toString()
        val cachedDate =
            DateFormat
                .format(DATE_FORMAT, cachedExchangeRatesData.cachedDataDate)
                .toString()

        return currentDate != cachedDate
    }
}
