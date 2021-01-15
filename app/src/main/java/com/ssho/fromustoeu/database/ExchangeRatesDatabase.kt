package com.ssho.fromustoeu.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ssho.fromustoeu.ExchangeRates

private const val DATABASE_NAME = "exchange-rates-database"

@Database(entities = [ ExchangeRates::class ], version = 1, exportSchema = false)
@TypeConverters(ExchangeRatesTypeConverters::class)
abstract class ExchangeRatesDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: ExchangeRatesDatabase? = null

        fun getExchangeRatesDatabase(context: Context): ExchangeRatesDatabase {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    ExchangeRatesDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = database
                database
            }
        }
    }

    abstract fun exchangeRatesDao(): ExchangeRatesDao
}