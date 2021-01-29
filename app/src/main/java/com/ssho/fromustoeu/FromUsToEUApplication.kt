package com.ssho.fromustoeu

import android.app.Application
import com.ssho.fromustoeu.dependency_injection.initializeExchangeRatesRepository
import com.ssho.fromustoeu.dependency_injection.initializeMeasureBucketsRepository

class FromUsToEUApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initializeMeasureBucketsRepository(this)
        initializeExchangeRatesRepository(this)

    }
}