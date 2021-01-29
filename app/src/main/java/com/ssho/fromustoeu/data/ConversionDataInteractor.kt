package com.ssho.fromustoeu.data


import com.ssho.fromustoeu.ui.TAB_CURRENCY
import com.ssho.fromustoeu.data.model.ConversionData
import com.ssho.fromustoeu.data.model.ConversionPair

class ConversionDataInteractor(private val measureBucketsRepository: MeasureBucketsRepository,
                               private val exchangeRatesRepository: ExchangeRatesRepository) {
    var conversionData = ConversionData()
        private set

    fun updateSourceValue(value: Double) {
        conversionData = conversionData.copy(sourceValue = value)
    }

    fun swapConversionPairs() {
        val swappedConversionPairs = conversionData.conversionPairs.map {
            val newTargetName = it.sourceUnitName
            ConversionPair(
                sourceUnitName = it.targetUnitName,
                targetUnitName = newTargetName
            )
        }
        conversionData = conversionData.copy(conversionPairs = swappedConversionPairs)
    }

    suspend fun updateConversionData(appTab: String, sourceMeasureSystem: Int) {
        when (appTab) {
            TAB_CURRENCY -> exchangeRatesRepository.getConversionData(sourceMeasureSystem)
            else -> measureBucketsRepository.getConversionData(sourceMeasureSystem)
        }.also {
            updateConversionData(it)
        }
    }

    private fun updateConversionData(newConversionData: ConversionData) {
        val sourceValue = conversionData.sourceValue
        conversionData = newConversionData.copy(sourceValue = sourceValue)
    }
}