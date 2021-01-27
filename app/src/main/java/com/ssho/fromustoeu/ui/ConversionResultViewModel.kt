package com.ssho.fromustoeu.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

private const val TAG = "ConversionResult"

class ConversionResultViewModel {
    val bucketViewState: LiveData<ConversionResultViewState> get() = _bucketViewState
    private val _bucketViewState = MutableLiveData(ConversionResultViewState())

    fun mapConversionResult(conversionResultUi: ConversionResultUi) {
        updateViewState(
                ConversionResultViewState(
                        conversionResultUi.sourceName,
                        conversionResultUi.resultValueText,
                        conversionResultUi.targetName)
        )
    }

    private fun updateViewState(newViewState: ConversionResultViewState?) {
        _bucketViewState.value = newViewState
    }
}

data class ConversionResultViewState(
    val sourceUnitName: String = "",
    val convertedValueText: String = "",
    val targetUnitName: String = ""
)
