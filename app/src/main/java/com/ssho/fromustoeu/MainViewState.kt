package com.ssho.fromustoeu

import java.io.Serializable

data class MainViewState(var currentValueText: String,
                         var measureSystemFrom: Int,
                         var appTab: String,
                         var isValueProvided: Boolean) : Serializable