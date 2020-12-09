package com.ssho.fromustoeu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssho.fromustoeu.converters.Converters
import kotlin.math.*

private const val TAG = "ConvertBucket"

class ConvertBucketViewModel(private var inputValue: Double) {
    val bucketViewState: LiveData<ConvertBucketViewState> get() = _bucketViewState
    private val _bucketViewState: MutableLiveData<ConvertBucketViewState> = MutableLiveData()

    private lateinit var convertBucket: ConvertBucket

    init {
        _bucketViewState.value = ConvertBucketViewState()
    }

    fun setConvertBucket(convertBucket: ConvertBucket,
                         sourceUnitNameResId: Int,
                         targetUnitNameResId: Int) {
        this.convertBucket = convertBucket
        val convertedValue = convert()
        val valueText = getValueText(convertedValue)

        updateViewState(_bucketViewState.value?.copy(
            valueToDisplay = valueText,
            sourceUnitNameResID = sourceUnitNameResId,
            targetUnitNameResID = targetUnitNameResId)
        )
    }

    private fun getValueText(convertedValue: Double): String {
        return if (floor(convertedValue) == convertedValue)
            convertedValue.toInt().toString()
        else
            String.format("%.2f", round(convertedValue * 100) / 100)
    }

    private fun updateViewState(newViewState: ConvertBucketViewState?) {
        _bucketViewState.value = newViewState
    }

    private fun convert(): Double {
        val convertTargetName: String = convertBucket.measureTypeTo
        if (convertTargetName != "celsius" || convertTargetName != "fahrenheit")
            inputValue = abs(inputValue)

        val result = Converters.convert(inputValue, convertTargetName)

        Log.i(TAG,"Got converted value to $convertTargetName = $result")

        return result
    }

}

data class ConvertBucketViewState(var sourceUnitNameResID:Int = R.string.empty,
                                  var valueToDisplay: String = "",
                                  var targetUnitNameResID: Int = R.string.empty)
