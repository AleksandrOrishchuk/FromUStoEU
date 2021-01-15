package com.ssho.fromustoeu

import android.app.Application
import com.ssho.fromustoeu.database.MeasureBucketsDatabase
import com.ssho.fromustoeu.database.ExchangeRatesDatabase
import com.ssho.fromustoeu.dependency_injection.exchangeRatesApi

class FromUsToEUApplication : Application() {
    private lateinit var measureBucketsDatabase: MeasureBucketsDatabase
    private lateinit var exchangeRatesDatabase: ExchangeRatesDatabase

    override fun onCreate() {
        super.onCreate()

        measureBucketsDatabase = MeasureBucketsDatabase.getMeasureBucketsDatabase(this)
        MeasureBucketsRepository.initialize(
            measureBucketsDatabase.convertBucketDao()
        )

        exchangeRatesDatabase = ExchangeRatesDatabase.getExchangeRatesDatabase(this)
        ExchangeRatesRepository.initialize(
                exchangeRatesDatabase.exchangeRatesDao(),
                exchangeRatesApi
        )
    }
}