package com.ssho.fromustoeu.data.database

import androidx.room.Dao
import androidx.room.Query
import com.ssho.fromustoeu.data.database.entities.MeasureBucket

@Dao
interface MeasureBucketsDao {
    @Query("SELECT * FROM MeasureBucket WHERE sourceMeasureSystem=(:measureSystemFrom)")
    suspend fun getBuckets(measureSystemFrom: Int): List<MeasureBucket>
}