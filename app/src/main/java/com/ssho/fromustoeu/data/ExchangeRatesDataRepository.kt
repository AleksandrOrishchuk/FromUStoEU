package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.model.ExchangeRatesData

interface ExchangeRatesDataRepository {
    suspend fun getExchangeRatesData(): ExchangeRatesData
    suspend fun getLatestExchangeRatesData(): ExchangeRatesData
}
