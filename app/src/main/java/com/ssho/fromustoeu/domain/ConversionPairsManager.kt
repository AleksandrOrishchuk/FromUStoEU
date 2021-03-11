package com.ssho.fromustoeu.domain

import com.ssho.fromustoeu.data.ConversionPairsProvidable
import com.ssho.fromustoeu.data.model.ConversionPair
import com.ssho.fromustoeu.ui.ConversionDirection
import com.ssho.fromustoeu.ui.model.ConverterUiInputData

internal class ConversionPairsManager(
    private val conversionPairsProvider: ConversionPairsProvidable
) {

    private var previousConvertUiInput: ConverterUiInputData? = null
    private var currentConversionPairs: List<ConversionPair>? = null

    suspend fun getConversionPairs(converterUiInputData: ConverterUiInputData): List<ConversionPair> {
        updateCurrentConversionPairs(converterUiInputData)

        return currentConversionPairs.also {
            previousConvertUiInput = converterUiInputData
        } ?: emptyList()
    }

    private suspend fun updateCurrentConversionPairs(converterUiInputData: ConverterUiInputData) {
        val isAppTabChanged = previousConvertUiInput?.appTab != converterUiInputData.appTab
        val isMeasureSystemChanged =
            previousConvertUiInput?.conversionDirection != converterUiInputData.conversionDirection

        when {
            isAppTabChanged -> {
                currentConversionPairs =
                    conversionPairsProvider.getConversionPairs(converterUiInputData.appTab)

                val isSourceMeasureSystemMetric =
                    converterUiInputData.conversionDirection != ConversionDirection.FROM_IMPERIAL_US

                if (isSourceMeasureSystemMetric)
                    reverseCurrentConversionPairs()
            }

            isMeasureSystemChanged -> reverseCurrentConversionPairs()
        }
    }

    private fun reverseCurrentConversionPairs() {
        currentConversionPairs = currentConversionPairs?.map {
            ConversionPair(
                sourceUnitName = it.targetUnitName,
                targetUnitName = it.sourceUnitName
            )
        }
    }

}
