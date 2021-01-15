package com.ssho.fromustoeu

interface BucketsProvidable {
    suspend fun getBucketList(measureSystemFrom: Int): List<ConvertBucket>
}