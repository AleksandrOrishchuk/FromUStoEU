package com.ssho.fromustoeu.data.api

import retrofit2.http.GET

interface ExchangeRatesApi {

    @GET("latest?base=USD" +
                "&symbols=EUR,GBP,SEK,NOK,CAD,AUD,RUB,CHF")
    suspend fun fetchLatestExchangeRates(): ExchangeRatesResponse
}