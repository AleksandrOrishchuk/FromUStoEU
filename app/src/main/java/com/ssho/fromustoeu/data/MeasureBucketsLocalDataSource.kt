package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.database.MeasureBucketsDao
import com.ssho.fromustoeu.data.database.entities.MeasureBucket

class MeasureBucketsLocalDataSource(
    private val measureBucketsDao: MeasureBucketsDao
) {

    companion object{
        private const val SOURCE_MEASURE_SYSTEM: Int = 1
    }

    suspend fun getMeasureBuckets(): List<MeasureBucket> {
        return measureBucketsDao.getBuckets(SOURCE_MEASURE_SYSTEM)
    }
}
