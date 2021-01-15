package com.ssho.fromustoeu.dependency_injection

import com.ssho.fromustoeu.*
import com.ssho.fromustoeu.api.ExchangeRatesApi
import com.ssho.fromustoeu.converters.CurrencyConverter
import com.ssho.fromustoeu.converters.MeasureConverter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val currencyBucketsProvider: CurrencyBucketsProvider by lazy {
    CurrencyBucketsProvider(ExchangeRatesRepository.get(), currencyConverter)
}

private val measureBucketsProvider: MeasureBucketsProvider by lazy {
    MeasureBucketsProvider(MeasureBucketsRepository.get())
}

internal val currencyConverter: CurrencyConverter by lazy { CurrencyConverter() }
internal val measureConverter: MeasureConverter by lazy { MeasureConverter() }

internal val exchangeRatesApi: ExchangeRatesApi by lazy {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.exchangeratesapi.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    retrofit.create(ExchangeRatesApi::class.java)
}

internal fun provideMainViewModelFactory() : MainViewModelFactory = MainViewModelFactory(
    currencyBucketsProvider,
    measureBucketsProvider
)