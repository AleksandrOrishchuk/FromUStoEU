package com.ssho.fromustoeu.ui

import com.ssho.fromustoeu.data.converters.Converter
import com.ssho.fromustoeu.data.model.ConversionData

internal interface ConversionResultUiMappable {
    fun map(): List<ConversionResultUi>
}

internal class ConversionResultUiMapper(
    private val conversionData: ConversionData,
    private val converter: Converter
    ) : ConversionResultUiMappable
{
    override fun map(): List<ConversionResultUi> {
        val sourceValue = conversionData.sourceValue

        return conversionData.conversionPairs.map {
            val result = converter.convert(it.sourceUnitName, sourceValue, it.targetUnitName)
            val resultText = getValueText(result)

            ConversionResultUi(it.sourceUnitName, resultText, it.targetUnitName)
        }
    }
}