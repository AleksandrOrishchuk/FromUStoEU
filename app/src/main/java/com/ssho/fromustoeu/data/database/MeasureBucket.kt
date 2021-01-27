package com.ssho.fromustoeu.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MeasureBucket(
    @PrimaryKey val uid: Int,
    val appTab: String,
    val sourceMeasureSystem: Int,
    val sourceUnitName: String,
    val targetUnitName: String,
    val sourceValueText: String
)