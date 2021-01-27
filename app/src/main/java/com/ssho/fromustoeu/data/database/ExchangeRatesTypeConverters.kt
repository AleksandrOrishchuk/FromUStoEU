package com.ssho.fromustoeu.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.HashMap

class ExchangeRatesTypeConverters {

    private val gSon = Gson()

    @TypeConverter
    fun toHashMap(string: String?): Map<String, Double> {
        val mapType = object : TypeToken<HashMap<String, Double>>(){}.type

        return gSon.fromJson(string, mapType)
    }

    @TypeConverter
    fun fromHashMap(hashMap: Map<String, Double>?): String {
        return gSon.toJson(hashMap)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it) }
    }
}