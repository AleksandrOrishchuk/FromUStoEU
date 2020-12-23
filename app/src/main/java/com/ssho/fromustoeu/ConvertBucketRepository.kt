package com.ssho.fromustoeu

import android.content.Context
import androidx.room.Room
import com.ssho.fromustoeu.database.ConvertBucketDatabase

private const val DATABASE_NAME = "convert-bucket-database"

class ConvertBucketRepository private constructor(context: Context) {

    companion object {
        //todo можно заиспользовть by lazy {}
        private var INSTANCE: ConvertBucketRepository? = null

        //todo не совсем понятна логика разделения на два метода
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


    suspend fun getBuckets(appTab: String, measureSystemFrom: Int): List<ConvertBucket> {
        return convertBucketDao.getBuckets(appTab, measureSystemFrom)
    }
}