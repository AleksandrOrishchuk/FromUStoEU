package com.ssho.fromustoeu

import java.io.Serializable

data class MainViewState(
    val currentValueText: String,
    val measureSystemFrom: Int,
    val appTab: String,
    val isValueProvided: Boolean,
    val convertBucketList: List<ConvertBucket> = emptyList()
) : Serializable