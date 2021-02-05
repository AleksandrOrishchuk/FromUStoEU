package com.ssho.fromustoeu.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ssho.fromustoeu.data.database.entities.ExchangeRatesEntity

private const val DATABASE_NAME = "exchange-rates-database"

@Database(entities = [ ExchangeRatesEntity::class ], version = 3, exportSchema = false)
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
                ).fallbackToDestructiveMigration()
                        .build()
                INSTANCE = database
                database
            }
        }
    }

    abstract fun exchangeRatesDao(): ExchangeRatesDao
}