package com.ssho.fromustoeu.ui

import java.io.Serializable

data class MainViewState(
    val currentValueText: String,
    val measureSystemFrom: Int,
    val appTab: String,
    val isValueProvided: Boolean,
    val conversionResultUiList: List<ConversionResultUi> = emptyList()
) : Serializable