package com.ssho.fromustoeu.database

import androidx.room.*
import com.ssho.fromustoeu.ExchangeRates

@Dao
interface ExchangeRatesDao {

    @Query("SELECT * FROM ExchangeRates")
    suspend fun getCachedExchangeRates(): List<ExchangeRates>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheExchangeRates(exchangeRates: ExchangeRates)

}