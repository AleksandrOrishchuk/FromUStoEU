package com.ssho.fromustoeu.dependency_injection

import android.content.Context
import com.ssho.fromustoeu.data.api.ExchangeRatesApi
import com.ssho.fromustoeu.data.converters.CurrencyConverter
import com.ssho.fromustoeu.data.converters.MeasureConverter
import com.ssho.fromustoeu.data.*
import com.ssho.fromustoeu.data.database.ExchangeRatesDatabase
import com.ssho.fromustoeu.data.database.MeasureBucketsDatabase
import com.ssho.fromustoeu.ui.ConversionResultUiMapperFactory
import com.ssho.fromustoeu.ui.MainViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val exchangeRatesApi: ExchangeRatesApi by lazy {
    val retrofit = Retrofit.Builder()
            .baseUrl("https://api.exchangeratesapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    retrofit.create(ExchangeRatesApi::class.java)
}

private val conversionDataMapper: ConversionDataMapper by lazy {
    ConversionDataMapper()
}

private val conversionResultUiMapperFactory: ConversionResultUiMapperFactory by lazy {
    ConversionResultUiMapperFactory(
        CurrencyConverter(),
        MeasureConverter()
    )
}

private val conversionDataInteractor: ConversionDataInteractor by lazy {
    ConversionDataInteractor(
        MeasureBucketsRepository.get(),
        ExchangeRatesRepository.get()
    )
}


internal fun provideMainViewModelFactory() : MainViewModelFactory = MainViewModelFactory(
    conversionDataInteractor,
    conversionResultUiMapperFactory
)

internal fun initializeMeasureBucketsRepository(context: Context) {
    val measureBucketsDatabase = MeasureBucketsDatabase.getMeasureBucketsDatabase(context)
    val measureBucketsLocalDataSource =
            MeasureBucketsLocalDataSource(
                    conversionDataMapper,
                    measureBucketsDatabase.convertBucketDao()
            )
    MeasureBucketsRepository
            .initialize(
                    measureBucketsLocalDataSource
            )
}

internal fun initializeExchangeRatesRepository(context: Context) {
    val exchangeRatesDatabase = ExchangeRatesDatabase.getExchangeRatesDatabase(context)

    val exRatesRemoteDataSource = ExRatesRemoteDataSource(
            conversionDataMapper,
            exchangeRatesApi
    )
    val exRatesLocalDataSource = ExRatesLocalDataSource(
            conversionDataMapper,
            exchangeRatesDatabase.exchangeRatesDao()
    )
    ExchangeRatesRepository
            .initialize(
                    exRatesLocalDataSource,
                    exRatesRemoteDataSource
            )
}
