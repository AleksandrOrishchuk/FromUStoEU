package com.ssho.fromustoeu.dependency_injection

import android.annotation.SuppressLint
import android.content.Context
import com.ssho.fromustoeu.data.*
import com.ssho.fromustoeu.data.api.ExchangeRatesApi
import com.ssho.fromustoeu.data.converters.MeasureConverter
import com.ssho.fromustoeu.data.database.ExchangeRatesDatabase
import com.ssho.fromustoeu.data.database.MeasureBucketsDatabase
import com.ssho.fromustoeu.domain.ConversionPairsManager
import com.ssho.fromustoeu.domain.GetConversionResultUseCase
import com.ssho.fromustoeu.domain.GetConversionResultUseCaseImpl
import com.ssho.fromustoeu.ui.ConverterViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@SuppressLint("StaticFieldLeak")
object ConverterModule {

    lateinit var androidContext: Context

    private val exchangeRatesApi: ExchangeRatesApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.exchangeratesapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ExchangeRatesApi::class.java)
    }

    private val conversionDataMapper: ExchangeRatesDataMapper by lazy {
        ExchangeRatesDataMapper()
    }

    private val exchangeRatesDataRepository: ExchangeRatesDataRepository by lazy {
        val exchangeRatesDatabase = ExchangeRatesDatabase.getExchangeRatesDatabase(androidContext)

        val exRatesRemoteDataSource = ExRatesRemoteDataSource(
            conversionDataMapper,
            exchangeRatesApi,
        )
        val exRatesLocalDataSource = ExRatesLocalDataSource(
            conversionDataMapper,
            exchangeRatesDatabase.exchangeRatesDao()
        )

        ExchangeRatesDataRepositoryImpl(
            exRatesLocalDataSource,
            exRatesRemoteDataSource
        )
    }

    private val measurePairsRepository: ConversionPairsRepository by lazy {
        val measureBucketsDatabase = MeasureBucketsDatabase.getMeasureBucketsDatabase(androidContext)
        val measureBucketsLocalDataSource =
            MeasureBucketsLocalDataSource(
                measureBucketsDatabase.convertBucketDao()
            )
        MeasurePairsRepository(measureBucketsLocalDataSource)
    }

    private val conversionPairsProvider: ConversionPairsProvidable by lazy {
        ConversionPairsProvider(
            CurrencyPairsRepository(exchangeRatesDataRepository),
            measurePairsRepository
        )
    }

    private val conversionPairsManager: ConversionPairsManager by lazy {
        ConversionPairsManager(
            conversionPairsProvider
        )
    }

    private val getConversionResultUseCase: GetConversionResultUseCase by lazy {
        GetConversionResultUseCaseImpl(
            conversionPairsManager,
            ConverterProvider(exchangeRatesDataRepository, MeasureConverter())
        )
    }

    internal fun provideMainViewModelFactory(): ConverterViewModelFactory = ConverterViewModelFactory(
        getConversionResultUseCase
    )

}
