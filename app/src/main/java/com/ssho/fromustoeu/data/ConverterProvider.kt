package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.converters.Converter
import com.ssho.fromustoeu.data.converters.CurrencyConverter
import com.ssho.fromustoeu.ui.AppTab

internal class ConverterProvider(
    private val exchangeRatesDataRepository: ExchangeRatesDataRepository,
    private val measureConverter: Converter
) {
    suspend fun getConverter(appTab: AppTab): Converter =
        when(appTab) {
            AppTab.TAB_HOME -> measureConverter
            AppTab.TAB_CURRENCY -> CurrencyConverter(exchangeRatesDataRepository.getExchangeRatesData().exchangeRates)
        }
}
