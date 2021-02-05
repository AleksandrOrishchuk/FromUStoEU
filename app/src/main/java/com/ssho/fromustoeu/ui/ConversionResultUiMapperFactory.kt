package com.ssho.fromustoeu.ui

import com.ssho.fromustoeu.data.converters.CurrencyConverter
import com.ssho.fromustoeu.data.converters.MeasureConverter
import com.ssho.fromustoeu.data.model.ConversionData

class ConversionResultUiMapperFactory {

    internal fun initializeMapper(
            appTab: Int,
            conversionData: ConversionData
    ): ConversionResultUiMappable {

        return when (appTab) {
            TAB_CURRENCY ->
                ConversionResultUiMapper(
                    conversionData,
                    CurrencyConverter(conversionData.exchangeRates)
                )
            TAB_HOME  ->
                ConversionResultUiMapper(
                    conversionData,
                    MeasureConverter()
                )

            else ->
                throw ExceptionInInitializerError("No Ui mapper provided for such conversion")
        }
    }
}