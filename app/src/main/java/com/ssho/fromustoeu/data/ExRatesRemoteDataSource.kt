package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.api.ApiRequestHandler
import com.ssho.fromustoeu.data.api.ExchangeRatesApi
import com.ssho.fromustoeu.data.api.ExchangeRatesResponse
import com.ssho.fromustoeu.data.model.ConversionData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ExRatesRemoteDataSource(private val conversionDataMapper: ConversionDataMapper,
                              private val exchangeRatesApi: ExchangeRatesApi,
                              private val apiRequestHandler: ApiRequestHandler,
                              private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getConversionData(): ResultWrapper<ConversionData> {
        return when (val fetchedResponse = fetchLatestExchangeRates()) {
            is ResultWrapper.Success -> {
                val conversionData = conversionDataMapper.map(fetchedResponse.value)
                ResultWrapper.Success(conversionData)
            }
            is ResultWrapper.NetworkError -> fetchedResponse
            is ResultWrapper.GenericError -> fetchedResponse
        }
    }

    private suspend fun fetchLatestExchangeRates(): ResultWrapper<ExchangeRatesResponse> {
        return apiRequestHandler.handleApiRequest(dispatcher) {
            exchangeRatesApi.fetchLatestExchangeRates()
        }
    }
}