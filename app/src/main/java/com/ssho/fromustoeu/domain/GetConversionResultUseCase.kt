package com.ssho.fromustoeu.domain

import com.ssho.fromustoeu.data.ConverterProvider
import com.ssho.fromustoeu.ui.model.ConverterUiInputData

internal interface GetConversionResultUseCase {
    suspend operator fun invoke(converterUiInputData: ConverterUiInputData): List<ConversionResult>
}

internal class GetConversionResultUseCaseImpl(
    private val conversionPairsManager: ConversionPairsManager,
    private val converterProvider: ConverterProvider
) : GetConversionResultUseCase {

    override suspend fun invoke(converterUiInputData: ConverterUiInputData): List<ConversionResult> {
        if (converterUiInputData.sourceValue.isBlank()) throw NoValueProvidedException()
        val converter = converterProvider.getConverter(converterUiInputData.appTab)
        val conversionPairs = conversionPairsManager.getConversionPairs(converterUiInputData)

        return conversionPairs.map {
            val result = converter.convert(it.sourceUnitName, converterUiInputData.sourceValue.toDouble(), it.targetUnitName)

            ConversionResult(
                sourceName = it.sourceUnitName,
                targetName = it.targetUnitName,
                result = result
            )
        }
    }

}

class NoValueProvidedException : RuntimeException()

data class ConversionResult(
    val sourceName: String,
    val targetName: String,
    val result: Double
)
