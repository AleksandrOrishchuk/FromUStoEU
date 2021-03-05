package com.ssho.fromustoeu.ui

import com.ssho.fromustoeu.ui.model.ConversionResultUi

sealed class ConverterViewState {
    data class Result(
        val conversionResultUiList: List<ConversionResultUi>): ConverterViewState()
    object Error: ConverterViewState()
    object Loading: ConverterViewState()
    object NoValueProvided: ConverterViewState()
}
