package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.model.ConversionPair

class MeasurePairsRepository(
    private val measureBucketsLocalDataSource: MeasureBucketsLocalDataSource
) : ConversionPairsRepository {

    override suspend fun getConversionPairs(): List<ConversionPair> {
        val measureBuckets = measureBucketsLocalDataSource.getMeasureBuckets()
        return measureBuckets.map {
            ConversionPair(it.sourceUnitName, it.targetUnitName)
        }
    }

}
