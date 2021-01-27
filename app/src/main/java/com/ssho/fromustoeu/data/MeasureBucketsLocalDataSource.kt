package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.database.MeasureBucketsDao
import com.ssho.fromustoeu.data.model.ConversionData

class MeasureBucketsLocalDataSource(private val conversionDataMapper: ConversionDataMapper,
                                    private val measureBucketsDao: MeasureBucketsDao) {
    suspend fun getConversionData(sourceMeasureSystem: Int): ConversionData {
        val measureBuckets = measureBucketsDao.getBuckets(sourceMeasureSystem)

        return conversionDataMapper.map(measureBuckets)
    }
}