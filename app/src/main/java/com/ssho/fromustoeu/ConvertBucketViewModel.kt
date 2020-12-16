package com.ssho.fromustoeu

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssho.fromustoeu.converters.Converters
import kotlin.math.*

private const val TAG = "ConvertBucket"

class ConvertBucketViewModel {
    val bucketViewState: LiveData<ConvertBucketViewState> get() = _bucketViewState
    private val _bucketViewState: MutableLiveData<ConvertBucketViewState> = MutableLiveData()

    private lateinit var convertBucket: ConvertBucket

    val onLongClickListener = object : View.OnLongClickListener {
        override fun onLongClick(itemView: View): Boolean {
            if (bucketViewState.value == null)
                return false

            val convertedValueText = bucketViewState.value!!.convertedValueText
            val context = itemView.context
            copyTextToSystemClipboard(context, convertedValueText)
            showLongToast(context, R.string.copied_to_clipboard)

            return true
        }
    }

    init {
        _bucketViewState.value = ConvertBucketViewState()
    }

    fun setConvertBucket(convertBucket: ConvertBucket) {
        this.convertBucket = convertBucket

        val convertedValue = getConvertResult()
        val convertedValueText = getValueText(convertedValue)

        updateViewState(
                _bucketViewState.value?.copy(
                        convertedValueText = convertedValueText,
                        sourceUnitName = convertBucket.sourceUnitName,
                        targetUnitName = convertBucket.targetUnitName)
        )
    }

    private fun getValueText(convertedValue: Double): String {
        return if (floor(convertedValue) == convertedValue)
            convertedValue.toInt().toString()
        else
            String.format("%.2f", round(convertedValue * 100) / 100).replace(',', '.')
    }

    private fun getConvertResult(): Double {
        var inputValue = convertBucket.sourceValueText.toDouble()
        val targetUnitName: String = convertBucket.targetUnitName

        if (targetUnitName != "celsius" && targetUnitName != "fahrenheits")
            inputValue = abs(inputValue)

        val result = Converters.convert(inputValue, targetUnitName)
        Log.i(TAG,"Got converted value to $targetUnitName = $result")

        return result
    }

    private fun updateViewState(newViewState: ConvertBucketViewState?) {
        _bucketViewState.value = newViewState
    }
}

data class ConvertBucketViewState(var sourceUnitName: String = "",
                                  var convertedValueText: String = "",
                                  var targetUnitName: String = "")
