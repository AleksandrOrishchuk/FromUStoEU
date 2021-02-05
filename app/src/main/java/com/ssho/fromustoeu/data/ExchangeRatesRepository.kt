package com.ssho.fromustoeu.data

import android.text.format.DateFormat
import com.ssho.fromustoeu.data.model.ConversionData
import java.util.*

private const val TAG = "ExchangeRepository"
private const val DATE_FORMAT = "MMM dd, yyyy"

class ExchangeRatesRepository private constructor(
        private val exRatesLocalDataSource: ExRatesLocalDataSource,
        private val exRatesRemoteDataSource: ExRatesRemoteDataSource) : ConversionDataRepository {

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


    override suspend fun getConversionData(): ResultWrapper<ConversionData> {
        val cachedConversionData = exRatesLocalDataSource.getConversionData()

        if (cachedConversionData is ResultWrapper.Success) {
            val currentDate =
                    DateFormat
                            .format(DATE_FORMAT, Date())
                            .toString()
            val cachedDate =
                    DateFormat
                            .format(DATE_FORMAT, cachedConversionData.value.cachedDataDate)
                            .toString()
            if (currentDate == cachedDate)

                return cachedConversionData
        }

        return getConversionDataFromRemote().also {
            if (it is ResultWrapper.Success)
                cacheConversionData(it.value)
        }
    }

    override suspend fun getLatestConversionData(): ResultWrapper<ConversionData> {
        return getConversionDataFromRemote().also {
            if (it is ResultWrapper.Success)
                cacheConversionData(it.value)
        }
    }

    override suspend fun cacheConversionData(conversionData: ConversionData) {
        exRatesLocalDataSource.cacheConversionData(conversionData)
    }

    private suspend fun getConversionDataFromRemote(): ResultWrapper<ConversionData> {
        return exRatesRemoteDataSource.getConversionData()
    }
}