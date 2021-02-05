package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.database.MeasureBucketsDao
import com.ssho.fromustoeu.data.model.ConversionData
import com.ssho.fromustoeu.ui.FROM_IMPERIAL_US

class MeasureBucketsLocalDataSource(
        private val conversionDataMapper: ConversionDataMapper,
        private val measureBucketsDao: MeasureBucketsDao,
        private val sourceMeasureSystemToQueryDatabase: Int = FROM_IMPERIAL_US) {

    suspend fun getConversionData(): ResultWrapper<ConversionData> {
        return try {
            val measureBuckets = measureBucketsDao.getBuckets(sourceMeasureSystemToQueryDatabase)
            val conversionData = conversionDataMapper.map(measureBuckets)

            ResultWrapper.Success(conversionData)
        } catch (t: Throwable) {
            ResultWrapper.GenericError
        }
    }
}