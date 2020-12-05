package com.ssho.fromustoeu

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.floor

private const val TAG = "AppStateViewModel"

class AppStateViewModel : ViewModel() {
    private val appState = AppState()

    val appStateLiveData: LiveData<AppState> get() = _appStateLiveData
    private val _appStateLiveData: MutableLiveData<AppState> = MutableLiveData()

    private var currentValue: Double = appState.inputValue
        set(value) {
            if (value != appState.inputValue)
                appState.inputValue = value

            field = value
//            currentValueLiveData.postValue(currentValueToString)
            _appStateLiveData.postValue(appState)
        }

    val currentValueText: String
        get() = if (floor(currentValue) == currentValue)
                    floor(currentValue).toString()
                else currentValue.toString()

//    val currentValueLiveData: MutableLiveData<String> = MutableLiveData()


    val valueWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //nothing
        }

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            try {
                currentValue = charSequence.toString().toDouble()
                //stash currentValueDisplay
            } catch (e: NumberFormatException) {
                Log.e(TAG, "Number Format Exception in TextEdit")
            }
        }

        override fun afterTextChanged(s: Editable?) {
            //nothing
        }

    }

    private var regionFrom: Int = CONVERT_FROM_US
        set(value) {
            field = value
            appState.convertFrom = value
            _appStateLiveData.postValue(appState)
        }

    private var tab: String = TAB_HOME

    fun loadInitialAppState() {
        _appStateLiveData.postValue(appState)
    }

    fun onRegionClicked(view: View) {
        regionFrom = if (regionFrom == CONVERT_FROM_US) CONVERT_FROM_EU else CONVERT_FROM_US
    }

    private fun exposeValueLiveData(value: Double?): LiveData<Double> {
        val result = MutableLiveData<Double>()
        if (value == null)
            return result.apply {
                this.value = 0.0
            }

        return result.apply {
            this.value = value
        }
    }

}