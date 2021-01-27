package com.ssho.fromustoeu.data


import com.ssho.fromustoeu.ui.TAB_CURRENCY
import com.ssho.fromustoeu.data.model.ConversionData

class ConversionDataInteractor(private val measureBucketsRepository: MeasureBucketsRepository,
                               private val exchangeRatesRepository: ExchangeRatesRepository) {
    suspend fun getConversionData(appTab: String, sourceMeasureSystem: Int): ConversionData {
        return when (appTab) {
            TAB_CURRENCY -> exchangeRatesRepository.getConversionData(sourceMeasureSystem)
            else -> measureBucketsRepository.getConversionData(sourceMeasureSystem)
        }
    }
}