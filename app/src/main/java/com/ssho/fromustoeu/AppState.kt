package com.ssho.fromustoeu

import java.io.Serializable

data class AppState(var inputValue: Double = 55.5,
                    var convertFrom: Int = CONVERT_FROM_US,
                    var appTab: String = TAB_HOME) : Serializable