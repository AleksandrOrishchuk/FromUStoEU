package com.ssho.fromustoeu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssho.fromustoeu.converters.Converter
import kotlin.math.floor
import kotlin.math.round

private const val TAG = "ConvertBucket"

class ConvertBucketViewModel(private val converter: Converter) {
    val bucketViewState: LiveData<ConvertBucketViewState> get() = _bucketViewState
    private val _bucketViewState = MutableLiveData(ConvertBucketViewState())

    fun setConvertBucketAndUpdateViewState(convertBucket: ConvertBucket) {
        val convertBucketViewState: ConvertBucketViewState = when (convertBucket) {
            is MeasureBucket -> {
                val sourceUnitName = convertBucket.sourceUnitName
                val targetUnitName = convertBucket.targetUnitName
                val sourceValue = convertBucket.sourceValueText.toDouble()

                val convertedValue = converter.convert(sourceValue, targetUnitName)

                val convertedValueText = getValueText(convertedValue)
                Log.i(TAG, "Got converted value to $targetUnitName = $convertedValue")

                ConvertBucketViewState(sourceUnitName, convertedValueText, targetUnitName)
            }
            is CurrencyBucket -> {
                val sourceCurrencyName = convertBucket.sourceCurrencyName
                val targetCurrencyName = convertBucket.targetCurrencyName
                val sourceValue = convertBucket.sourceValueText.toDouble()

                converter.setExtras(sourceCurrencyName)
                val convertedValue = converter.convert(sourceValue, targetCurrencyName)

                val convertedValueText = getValueText(convertedValue)
                Log.i(TAG, "Got converted value to $targetCurrencyName = $convertedValue")

                ConvertBucketViewState(sourceCurrencyName, convertedValueText, targetCurrencyName)
            }
            else -> ConvertBucketViewState()
        }

        updateViewState(convertBucketViewState)
    }

    private fun updateViewState(newViewState: ConvertBucketViewState?) {
        _bucketViewState.value = newViewState
    }

    private fun getValueText(convertedValue: Double): String {
        return if (floor(convertedValue) == convertedValue)
            convertedValue.toInt().toString()
        else
            String.format("%.2f", round(convertedValue * 100) / 100)
                .replace(',', '.')
    }
}

data class ConvertBucketViewState(
    val sourceUnitName: String = "",
    val convertedValueText: String = "",
    val targetUnitName: String = ""
)
