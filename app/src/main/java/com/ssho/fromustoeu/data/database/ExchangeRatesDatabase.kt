package com.ssho.fromustoeu.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ssho.fromustoeu.data.dto.ExchangeRatesDTO

private const val DATABASE_NAME = "exchange-rates-database"

@Database(entities = [ ExchangeRatesDTO::class ], version = 2, exportSchema = false)
@TypeConverters(ExchangeRatesTypeConverters::class)
abstract class ExchangeRatesDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: ExchangeRatesDatabase? = null

        private val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ExchangeRates RENAME TO ExchangeRatesDTO")
            }
        }

        fun getExchangeRatesDatabase(context: Context): ExchangeRatesDatabase {
            return INSTANCE ?: synchronized(this) {
                val database = Room.databaseBuilder(
                    context.applicationContext,
                    ExchangeRatesDatabase::class.java,
                    DATABASE_NAME
                ).addMigrations(migration_1_2)
                        .build()
                INSTANCE = database
                database
            }
        }
    }

    abstract fun exchangeRatesDao(): ExchangeRatesDao
}