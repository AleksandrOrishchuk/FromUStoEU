package com.ssho.fromustoeu.ui

sealed class MainViewState {
    data class Result(
        val conversionResultUiList: List<ConversionResultUi>): MainViewState()
    object Error: MainViewState()
    object Loading: MainViewState()
    object NoSourceValue: MainViewState()
}
