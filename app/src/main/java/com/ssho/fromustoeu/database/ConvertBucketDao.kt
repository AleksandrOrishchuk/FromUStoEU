package com.ssho.fromustoeu.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.ssho.fromustoeu.ConvertBucket

@Dao
interface ConvertBucketDao {
    @Query("SELECT * FROM ConvertBucket WHERE appTab LIKE :appTab AND measureSystemFrom LIKE :measureSystemFrom")
    fun getBuckets(appTab: String, measureSystemFrom: Int): LiveData<List<ConvertBucket>>
}