package com.ssho.fromustoeu.data

import com.ssho.fromustoeu.data.model.ConversionData

class ConversionDataInteractor(private val repositoryProvider: ConversionDataRepositoryProvider) {

    suspend fun getConversionData(
            appTab: Int,
    ): ResultWrapper<ConversionData> {

        return repositoryProvider
                .getConversionDataRepository(appTab)
                .getConversionData()
    }

    suspend fun getLatestConversionData(
            appTab: Int,
    ): ResultWrapper<ConversionData> {

        return repositoryProvider
                .getConversionDataRepository(appTab)
                .getLatestConversionData()
    }

}