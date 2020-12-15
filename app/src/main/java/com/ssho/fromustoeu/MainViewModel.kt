package com.ssho.fromustoeu

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val TAG = "MainViewModel"
private const val INITIAL_VALUE = 55.5
private const val INITIAL_SYSTEM_FROM = FROM_IMPERIAL_US
private const val INITIAL_APP_TAB = TAB_HOME
private const val INITIAL_IS_VALUE_PROVIDED = false

class MainViewModel : ViewModel() {

    val mainViewStateLiveData: LiveData<MainViewState> get() = _mainViewStateLiveData
    val isSoftKeyboardFocused: LiveData<Boolean> get() = _isSoftKeyboardFocused
    val calculatorIntentLiveData: LiveData<Intent> get() = _calculatorIntentLiveData
    private val _mainViewStateLiveData: MutableLiveData<MainViewState> = MutableLiveData()
    private val _isSoftKeyboardFocused: MutableLiveData<Boolean> = MutableLiveData(true)
    private val _calculatorIntentLiveData: MutableLiveData<Intent> = MutableLiveData()

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

            if (charSequence.length == 1 && charSequence[0] == '-')
                return

            try {
                val newValueText = charSequence.toString()
                val newValue = newValueText.toDouble()

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

    val onFocusChangedListener = View.OnFocusChangeListener { _, hasFocus -> _isSoftKeyboardFocused.value = hasFocus }

    val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (item.itemId == R.id.tab_calculator) {
            val calcIntent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_CALCULATOR).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            _calculatorIntentLiveData.value = calcIntent

            return@OnNavigationItemSelectedListener false
        }

        val newAppTab = when (item.itemId) {
            R.id.tab_currency -> TAB_CURRENCY
            else -> TAB_HOME
        }
        val currentAppTab = mainViewStateLiveData.value?.appTab

        if (newAppTab != currentAppTab) {
            updateViewState(_mainViewStateLiveData.value?.copy(appTab = newAppTab))
            Log.i(TAG, "Menu item selected: $newAppTab")
            true
        } else
            false
    }

    init {
        setInitialState()
        Log.d(TAG, "MainViewModel Initial load complete.")
    }

    fun onRegionClicked() {
        val regionFrom =
                if (mainViewStateLiveData.value?.measureSystemFrom == FROM_IMPERIAL_US)
                    FROM_METRIC_EU
                else
                    FROM_IMPERIAL_US

        updateViewState(
                _mainViewStateLiveData.value?.copy(
                        measureSystemFrom = regionFrom
                )
        )
    }


    private fun updateViewState(newMainViewState: MainViewState?) {
        _mainViewStateLiveData.value = newMainViewState
    }


    private fun setInitialState() {
        var initialValueText = ""

        if (INITIAL_IS_VALUE_PROVIDED)
            initialValueText = INITIAL_VALUE.toString()

        updateViewState(
                MainViewState(
                        initialValueText,
                        INITIAL_SYSTEM_FROM,
                        INITIAL_APP_TAB,
                        INITIAL_IS_VALUE_PROVIDED)
        )
    }
}