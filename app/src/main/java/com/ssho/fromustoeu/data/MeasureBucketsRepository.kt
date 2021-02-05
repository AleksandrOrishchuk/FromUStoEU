package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.model.ConversionData

class MeasureBucketsRepository
private constructor(private val measureBucketsLocalDataSource: MeasureBucketsLocalDataSource
) : ConversionDataRepository {

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

    override suspend fun getConversionData(): ResultWrapper<ConversionData> {
        return measureBucketsLocalDataSource.getConversionData()

    }

    override suspend fun getLatestConversionData(): ResultWrapper<ConversionData> {
        return getConversionData()
    }

    override suspend fun cacheConversionData(conversionData: ConversionData) {
        throw IllegalAccessException("Can not write into Measure Buckets database.")
    }

}