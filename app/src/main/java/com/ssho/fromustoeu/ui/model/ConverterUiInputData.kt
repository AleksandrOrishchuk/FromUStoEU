package com.ssho.fromustoeu.ui.model

import com.ssho.fromustoeu.ui.AppTab
import com.ssho.fromustoeu.ui.ConversionDirection

internal data class ConverterUiInputData(
    val sourceValue: String,
    val conversionDirection: ConversionDirection,
    val appTab: AppTab,
)
