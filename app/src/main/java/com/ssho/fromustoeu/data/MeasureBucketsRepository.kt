package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.model.ConversionData

class MeasureBucketsRepository
private constructor(private val measureBucketsLocalDataSource: MeasureBucketsLocalDataSource) {

    companion object {
        private var INSTANCE: MeasureBucketsRepository? = null

        fun initialize(measureBucketsLocalDataSource: MeasureBucketsLocalDataSource) {
            if (INSTANCE == null)
                INSTANCE = MeasureBucketsRepository(measureBucketsLocalDataSource
                )
        }

        fun get(): MeasureBucketsRepository {
            return INSTANCE ?: throw IllegalStateException("ConvertBucketRepository must be initialized.")
        }
    }

    suspend fun getConversionData(sourceMeasureSystem: Int): ConversionData {
        return measureBucketsLocalDataSource.getConversionData(sourceMeasureSystem)
    }

}