package com.ssho.fromustoeu

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val TAG = "MainViewModel"
private const val INITIAL_VALUE = 55.5
private const val INITIAL_CONVERT_FROM = CONVERT_FROM_US
private const val INITIAL_APP_TAB = TAB_HOME
private const val INITIAL_IS_VALUE_PROVIDED = false

class MainViewModel : ViewModel() {

    val mainViewStateLiveData: LiveData<MainViewState> get() = _mainViewStateLiveData
    val currentValueLiveData: LiveData<Double> get() = _currentValueLiveData
    private val _mainViewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()
    private val _currentValueLiveData: MutableLiveData<Double> = MutableLiveData()

    val valueWatcher = object : TextWatcher {
        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            if (charSequence == null || charSequence.isEmpty()) {
                updateViewState(
                        _mainViewStateLiveData.value?.copy(
                                currentValueText = "",
                                isValueProvided = false
                        )
                )
                return
            }

            try {
                val newValueText = charSequence.toString()
                val newValue = newValueText.toDouble()

                updateCurrentValue(newValue)
                updateViewState(
                        _mainViewStateLiveData.value?.copy(
                                currentValueText = newValueText,
                                isValueProvided = true
                        )
                )

                Log.d(TAG, "Value received from TextEdit: $newValue + string_$newValueText")

            } catch (e: NumberFormatException) {
                Log.e(TAG, "Number Format Exception in TextEdit")
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //nothing
        }

        override fun afterTextChanged(s: Editable?) {
            //nothing
        }
    }


    init {
        setInitialState()
        Log.d(TAG, "MainViewModel Initial load complete.")
    }

    fun onRegionClicked(view: View) {
        val regionFrom =
                if (_mainViewStateLiveData.value?.convertFrom == CONVERT_FROM_US)
                    CONVERT_FROM_EU
                else
                    CONVERT_FROM_US

        updateViewState(
                _mainViewStateLiveData.value?.copy(
                        convertFrom = regionFrom
                )
        )
    }


    private fun updateCurrentValue(value: Double) {
        _currentValueLiveData.value = value
    }


    private fun updateViewState(newMainViewState: MainViewState?) {
        _mainViewStateLiveData.value = newMainViewState
    }

    private fun setInitialState() {
        var initialValue = ""

        if (INITIAL_IS_VALUE_PROVIDED) {
            initialValue = INITIAL_VALUE.toString()
            updateCurrentValue(INITIAL_VALUE)
        }

        updateViewState(
                MainViewState(
                        initialValue,
                        INITIAL_CONVERT_FROM,
                        INITIAL_APP_TAB,
                        INITIAL_IS_VALUE_PROVIDED)
        )
    }
}