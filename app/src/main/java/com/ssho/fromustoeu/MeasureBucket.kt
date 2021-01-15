package com.ssho.fromustoeu

import androidx.room.Entity
import androidx.room.PrimaryKey

//не самое удачное форматирование, стоит использовать горячие клавиши или прекоммит хук
@Entity
data class MeasureBucket(
    @PrimaryKey val uid: Int,
    val appTab: String,
    val sourceMeasureSystem: Int,
    val sourceUnitName: String,
    val targetUnitName: String,
    val sourceValueText: String
) : ConvertBucket()