package com.ssho.fromustoeu.data.database

import androidx.room.*
import com.ssho.fromustoeu.data.database.entities.ExchangeRatesEntity

@Dao
interface ExchangeRatesDao {

    @Query("SELECT * FROM ExchangeRatesEntity")
    suspend fun getCachedExchangeRates(): List<ExchangeRatesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheExchangeRates(exchangeRatesEntity: ExchangeRatesEntity)

}