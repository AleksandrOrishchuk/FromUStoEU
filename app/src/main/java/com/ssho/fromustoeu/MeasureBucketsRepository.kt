package com.ssho.fromustoeu

import com.ssho.fromustoeu.database.MeasureBucketsDao

class MeasureBucketsRepository
private constructor(private val measureBucketsDao: MeasureBucketsDao) {

    companion object {
        private var INSTANCE: MeasureBucketsRepository? = null

        fun initialize(measureBucketsDao: MeasureBucketsDao) {
            if (INSTANCE == null)
                INSTANCE = MeasureBucketsRepository(measureBucketsDao)
        }

        fun get(): MeasureBucketsRepository {
            return INSTANCE ?: throw IllegalStateException("ConvertBucketRepository must be initialized.")
        }
    }

    suspend fun getMeasureBuckets(appTab: String, measureSystemFrom: Int): List<MeasureBucket> {
        return measureBucketsDao.getBuckets(appTab, measureSystemFrom)
    }
}