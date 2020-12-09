package com.ssho.fromustoeu

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.ssho.fromustoeu.database.ConvertBucketDatabase
import java.lang.IllegalStateException

private const val DATABASE_NAME = "convert-bucket-database"

class ConvertBucketRepository private constructor(context: Context) {

    companion object {
        private var INSTANCE: ConvertBucketRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null)
                INSTANCE = ConvertBucketRepository(context)
        }

        fun get(): ConvertBucketRepository {
            return INSTANCE ?: throw IllegalStateException("ConvertBucketRepository must be initialized.")
        }
    }

    private val database: ConvertBucketDatabase = Room.databaseBuilder(
            context.applicationContext,
            ConvertBucketDatabase::class.java,
            DATABASE_NAME
        ).createFromAsset("database/convert_buckets.db")
        .fallbackToDestructiveMigration()
        .build()

    private val convertBucketDao = database.convertBucketDao()


    fun getBuckets(appTab: String, measureSystemFrom: Int): LiveData<List<ConvertBucket>> {
        return convertBucketDao.getBuckets(appTab, measureSystemFrom)
    }
}