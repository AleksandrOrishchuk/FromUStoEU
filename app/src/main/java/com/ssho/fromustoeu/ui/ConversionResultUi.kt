package com.ssho.fromustoeu.ui

import java.io.Serializable

data class ConversionResultUi(val sourceName: String = "",
                              val resultValueText: String = "",
                              val targetName: String = ""
) : Serializable