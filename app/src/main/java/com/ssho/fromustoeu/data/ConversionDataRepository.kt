package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.model.ConversionData

interface ConversionDataRepository {
    suspend fun getConversionData(): ResultWrapper<ConversionData>
    suspend fun getLatestConversionData(): ResultWrapper<ConversionData>
    suspend fun cacheConversionData(conversionData: ConversionData)
}