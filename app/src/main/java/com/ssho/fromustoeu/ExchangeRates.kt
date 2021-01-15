package com.ssho.fromustoeu

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.HashMap

@Entity
data class ExchangeRates(
        @PrimaryKey
        val base: String = "",
        @SerializedName("date")
        val serverDate: String = "",
        val cachedDate: Date = Date(),
        val rates: HashMap<String, Double>
)