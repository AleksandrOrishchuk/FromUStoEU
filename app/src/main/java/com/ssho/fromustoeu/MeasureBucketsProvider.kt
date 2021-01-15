package com.ssho.fromustoeu

class MeasureBucketsProvider(
        private val measureBucketsRepository: MeasureBucketsRepository) : BucketsProvidable {

    override suspend fun getBucketList(measureSystemFrom: Int): List<MeasureBucket> {
        return measureBucketsRepository.getMeasureBuckets(TAB_HOME, measureSystemFrom)
    }
}