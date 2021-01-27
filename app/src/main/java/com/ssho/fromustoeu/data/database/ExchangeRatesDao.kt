package com.ssho.fromustoeu.data.database

import androidx.room.*
import com.ssho.fromustoeu.data.dto.ExchangeRatesDTO

@Dao
interface ExchangeRatesDao {

    @Query("SELECT * FROM ExchangeRatesDTO")
    suspend fun getCachedExchangeRates(): List<ExchangeRatesDTO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheExchangeRates(exchangeRatesDTO: ExchangeRatesDTO)

}