package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.ui.TAB_CURRENCY
import com.ssho.fromustoeu.ui.TAB_HOME

class ConversionDataRepositoryProvider(
        private val measureBucketsRepository: ConversionDataRepository,
        private val exchangeRatesRepository: ConversionDataRepository) {
    fun getConversionDataRepository(appTab: Int): ConversionDataRepository {
        return when (appTab) {
            TAB_CURRENCY -> exchangeRatesRepository
            TAB_HOME -> measureBucketsRepository
            else -> measureBucketsRepository
        }
    }
}