package com.ssho.fromustoeu

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.ssho.fromustoeu.converters.Converters
import java.util.*
import kotlin.math.*

private const val TAG = "ConvertBucket"

class ConvertBucketViewModel(private val inputValue: Double): BaseObservable() {

    var convertToType: String? = null
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    val convertToTypeDisplay: String? get() = convertToType?.toUpperCase(Locale.ROOT)

    @get:Bindable
    val convertedValueDisplay: String get() {
        val convertedValue = convert()

        return if (floor(convertedValue) == convertedValue)
            convertedValue.toInt().toString()
        else
            String.format("%.2f", round(convertedValue * 100) / 100)
    }


    private fun convert(): Double {
        var result = Converters.convert(inputValue, convertToType!!)

//TODO("do not let other values than temperature get to negative state") BUG DETECTED
//        if (convertToType != "celsius" || convertToType != "fahrenheit")
//            result = abs(result)

        Log.i(TAG,"Got converted value to $convertToType = $result")

        return result
    }

}