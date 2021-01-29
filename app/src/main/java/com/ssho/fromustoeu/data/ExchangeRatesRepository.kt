package com.ssho.fromustoeu.data

import android.text.format.DateFormat
import android.util.Log
import com.ssho.fromustoeu.data.model.ConversionData
import java.util.*

private const val TAG = "ExchangeRepository"
private const val DATE_FORMAT = "MMM dd, yyyy"

class ExchangeRatesRepository private constructor(
        private val exRatesLocalDataSource: ExRatesLocalDataSource,
        private val exRatesRemoteDataSource: ExRatesRemoteDataSource) {

    companion object {
        private var INSTANCE: ExchangeRatesRepository? = null

        fun initialize(exRatesLocalDataSource: ExRatesLocalDataSource,
                       exRatesRemoteDataSource: ExRatesRemoteDataSource) {
            if (INSTANCE == null)
                INSTANCE = ExchangeRatesRepository(
                        exRatesLocalDataSource,
                        exRatesRemoteDataSource
                )
        }

        fun get(): ExchangeRatesRepository {
            return INSTANCE ?: throw IllegalStateException("ExchangeRatesRepository must be initialized.")
        }
    }


    suspend fun getConversionData(sourceMeasureSystem: Int): ConversionData {
        val cachedData = exRatesLocalDataSource.getConversionData(sourceMeasureSystem)

        if (cachedData.conversionPairs.isNotEmpty()) {
            val currentDate = DateFormat.format(DATE_FORMAT, Date())
                    .toString()
            val cachedDate = DateFormat.format(DATE_FORMAT, cachedData.cachedDataDate)
                    .toString()
            if (currentDate == cachedDate)
                return cachedData
        }

        return getConversionDataFromInternet(sourceMeasureSystem)
    }

    private suspend fun getConversionDataFromInternet(sourceMeasureSystem: Int): ConversionData {
        return exRatesRemoteDataSource.getConversionData(sourceMeasureSystem).also {
            exRatesLocalDataSource.cacheConversionData(it)
            Log.d(TAG, "Got exchange rates from remote source and cached to local.")
        }
    }
}