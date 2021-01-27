package com.ssho.fromustoeu.data.api

import com.google.gson.annotations.SerializedName

data class Rates(
        @SerializedName("EUR")
        val eur: Double,
        @SerializedName("CAD")
        val cad: Double,
        @SerializedName("SEK")
        val sek: Double,
        @SerializedName("NOK")
        val nok: Double,
        @SerializedName("RUB")
        val rub: Double,
        @SerializedName("CHF")
        val chf: Double,
        @SerializedName("AUD")
        val aud: Double,
        @SerializedName("GBP")
        val gbp: Double)
