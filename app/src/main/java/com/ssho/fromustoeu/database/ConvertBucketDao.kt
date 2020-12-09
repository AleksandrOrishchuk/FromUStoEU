package com.ssho.fromustoeu.database

import androidx.room.Dao
import androidx.room.Query
import com.ssho.fromustoeu.ConvertBucket

@Dao
interface ConvertBucketDao {
    @Query("SELECT * FROM ConvertBucket WHERE appTab=(:appTab) AND measureSystemFrom=(:measureSystemFrom)")
    suspend fun getBuckets(appTab: String, measureSystemFrom: Int): List<ConvertBucket>
}