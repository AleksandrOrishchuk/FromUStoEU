package com.ssho.fromustoeu.database

import androidx.room.Dao
import androidx.room.Query
import com.ssho.fromustoeu.MeasureBucket

@Dao
interface MeasureBucketsDao {
    @Query("SELECT * FROM MeasureBucket WHERE appTab=(:appTab) AND sourceMeasureSystem=(:measureSystemFrom)")
    suspend fun getBuckets(appTab: String, measureSystemFrom: Int): List<MeasureBucket>
}