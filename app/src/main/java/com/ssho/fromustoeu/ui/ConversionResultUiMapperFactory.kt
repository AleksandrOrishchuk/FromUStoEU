package com.ssho.fromustoeu.ui

import com.ssho.fromustoeu.data.converters.Converter
import com.ssho.fromustoeu.data.model.ConversionData

class ConversionResultUiMapperFactory(private val currencyConverter: Converter,
                                      private val measureConverter: Converter) {


    internal fun initializeMapper(appTab: String,
                                  conversionData: ConversionData
    ): ConversionOutputUiMappable {

        return when (appTab) {
            TAB_CURRENCY ->
                ConversionOutputUiCurrenciesMapper(conversionData, currencyConverter)
            TAB_HOME  ->
                ConversionOutputUiMeasuresMapper(conversionData, measureConverter)

            else ->
                throw ExceptionInInitializerError("No Ui mapper provided for such conversion")
        }
    }
}