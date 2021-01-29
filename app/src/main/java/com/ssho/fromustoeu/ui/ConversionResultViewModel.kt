package com.ssho.fromustoeu.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ConversionResultViewModel {
    val viewState: LiveData<ConversionResultViewState> get() = _viewState
    private val _viewState = MutableLiveData(ConversionResultViewState())

    fun mapConversionResult(conversionResultUi: ConversionResultUi) {
        updateViewState(
                ConversionResultViewState(
                        conversionResultUi.sourceName,
                        conversionResultUi.resultValueText,
                        conversionResultUi.targetName)
        )
    }

    private fun updateViewState(newViewState: ConversionResultViewState?) {
        _viewState.value = newViewState
    }
}

data class ConversionResultViewState(
    val sourceUnitName: String = "",
    val convertedValueText: String = "",
    val targetUnitName: String = ""
)
