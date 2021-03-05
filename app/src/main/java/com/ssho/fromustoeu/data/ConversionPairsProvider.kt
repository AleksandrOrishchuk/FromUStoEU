package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.model.ConversionPair
import com.ssho.fromustoeu.ui.AppTab


internal interface ConversionPairsProvidable {
    suspend fun getConversionPairs(appTab: AppTab): List<ConversionPair>
}

internal class ConversionPairsProvider(
    private val currencyPairsRepository: ConversionPairsRepository,
    private val measurePairsRepository: ConversionPairsRepository
) : ConversionPairsProvidable {
    override suspend fun getConversionPairs(appTab: AppTab): List<ConversionPair> {
        return when (appTab) {
            AppTab.TAB_CURRENCY -> currencyPairsRepository.getConversionPairs()
            AppTab.TAB_HOME -> measurePairsRepository.getConversionPairs()
        }
    }

}
