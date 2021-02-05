package com.ssho.fromustoeu.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class ExchangeRatesEntity(
        @PrimaryKey
        val base: String = "",
        @SerializedName("date")
        val serverDate: String = "",
        val cachedDate: Date = Date(),
        val rates: Map<String, Double> = hashMapOf()
)