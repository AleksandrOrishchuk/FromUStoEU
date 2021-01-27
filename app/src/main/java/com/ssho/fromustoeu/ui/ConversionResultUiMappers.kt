package com.ssho.fromustoeu.ui

import com.ssho.fromustoeu.data.converters.Converter
import com.ssho.fromustoeu.data.model.ConversionData

internal interface ConversionOutputUiMappable {
    fun map(): List<ConversionResultUi>
}

internal class ConversionOutputUiCurrenciesMapper(
    private val conversionData: ConversionData,
    private val converter: Converter
    ) : ConversionOutputUiMappable
{
    override fun map(): List<ConversionResultUi> {
        val sourceValue = conversionData.sourceValue
        converter.setExtras(conversionData.exchangeRates)

        return conversionData.conversionPairs.map {
            converter.setExtras(it.sourceUnitName)
            val result = converter.convert(sourceValue, it.targetUnitName)
            val resultText = getValueText(result)

            ConversionResultUi(it.sourceUnitName, resultText, it.targetUnitName)
        }
    }
}

internal class ConversionOutputUiMeasuresMapper(
    private val conversionData: ConversionData,
    private val converter: Converter
    ) : ConversionOutputUiMappable
{
    override fun map(): List<ConversionResultUi> {
        val sourceValue = conversionData.sourceValue

        return conversionData.conversionPairs.map {
            val result = converter.convert(sourceValue, it.targetUnitName)
            val resultText = getValueText(result)

            ConversionResultUi(it.sourceUnitName, resultText, it.targetUnitName)
        }
    }
}