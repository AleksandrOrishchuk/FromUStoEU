package com.ssho.fromustoeu.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ssho.fromustoeu.MeasureBucket

private const val DATABASE_NAME = "measure-buckets-database"

@Database(entities = [ MeasureBucket::class ], version = 1, exportSchema = false)
abstract class MeasureBucketsDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: MeasureBucketsDatabase? = null

        fun getMeasureBucketsDatabase(context: Context): MeasureBucketsDatabase {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    MeasureBucketsDatabase::class.java,
                    DATABASE_NAME
                ).createFromAsset("database/measure_buckets.db")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = database
                database
            }
        }
    }

    abstract fun convertBucketDao(): MeasureBucketsDao
}
