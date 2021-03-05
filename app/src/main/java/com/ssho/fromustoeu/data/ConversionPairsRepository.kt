package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.model.ConversionPair

interface ConversionPairsRepository {
    suspend fun getConversionPairs(): List<ConversionPair>
}
