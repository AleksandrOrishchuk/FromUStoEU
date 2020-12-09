package com.ssho.fromustoeu.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ssho.fromustoeu.ConvertBucket

@Database(entities = [ ConvertBucket::class ], version = 1, exportSchema = false)
abstract class ConvertBucketDatabase : RoomDatabase() {
    abstract fun convertBucketDao(): ConvertBucketDao
}