package com.ssho.fromustoeu

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ConvertBucket(@PrimaryKey val uid: Int,
                         var appTab: String,
                         var sourceMeasureSystem: Int,
                         var sourceUnitName: String,
                         var targetUnitName: String,
                         var sourceValueText: String
                         ) : Serializable