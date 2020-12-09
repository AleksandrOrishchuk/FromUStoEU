package com.ssho.fromustoeu

import android.app.Application

class FromUsToEUApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ConvertBucketRepository.initialize(this)
    }
}