package com.ssho.fromustoeu

import android.app.Application
import com.ssho.fromustoeu.dependency_injection.ConverterModule

class FromUsToEUApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        ConverterModule.androidContext = applicationContext

    }
}
