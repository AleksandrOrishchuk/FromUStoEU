package com.ssho.fromustoeu.ui

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssho.fromustoeu.R
import com.ssho.fromustoeu.data.ConversionDataInteractor
import com.ssho.fromustoeu.data.model.ConversionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"
private const val INITIAL_VALUE = 55.5
private const val INITIAL_SYSTEM_FROM = FROM_IMPERIAL_US
private const val INITIAL_APP_TAB = TAB_HOME
private const val INITIAL_IS_VALUE_PROVIDED = false

class MainViewModel(
    private val conversionDataInteractor: ConversionDataInteractor,
    private val conversionResultUiMapperFactory: ConversionResultUiMapperFactory
    ) : ViewModel() {

    private var conversionData = ConversionData()

    val mainViewState: LiveData<MainViewState> get() = _mainViewStateLiveData
    val calculatorIntent: LiveData<ValueWrapper<Intent>> get() = _calculatorIntentLiveData
    private val _mainViewStateLiveData = MutableLiveData<MainViewState>()
    private val _calculatorIntentLiveData = MutableLiveData<ValueWrapper<Intent>>()

    val valueWatcher = object : TextWatcher {
        override fun onTextChanged(
            charSequence: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            updateCurrentValue(charSequence)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //nothing
        }

        override fun afterTextChanged(s: Editable?) {
            //nothing
        }
    }

    val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.tab_calculator) {
                val calcIntent = Intent.makeMainSelectorActivity(
                    Intent.ACTION_MAIN,
                    Intent.CATEGORY_APP_CALCULATOR
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                _calculatorIntentLiveData.value = ValueWrapper(calcIntent)

                return@OnNavigationItemSelectedListener false
            }

            val newAppTab = when (item.itemId) {
                R.id.tab_currency -> TAB_CURRENCY
                else -> TAB_HOME
            }
            val currentAppTab = mainViewState.value?.appTab

            if (newAppTab != currentAppTab) {
                val currentMeasureSystem = mainViewState.value?.measureSystemFrom ?: 1

                updateConversionDataAndViewState(newAppTab, currentMeasureSystem)
                Log.i(TAG, "Menu item selected: $newAppTab")

                true
            } else
                false
        }

    init {
        setInitialViewState()
        Log.d(TAG, "MainViewModel Initial load complete.")
    }

    fun onRegionClicked() {
        val currentMeasureSystem = mainViewState.value?.measureSystemFrom
        val newMeasureFrom =
                if (currentMeasureSystem == FROM_IMPERIAL_US)
                    FROM_METRIC_EU
                else
                    FROM_IMPERIAL_US

        val currentTab = mainViewState.value?.appTab.orEmpty()
        updateConversionDataAndViewState(currentTab, newMeasureFrom)
    }

    private fun updateCurrentValue(charSequence: CharSequence?) {
        if (charSequence == null || charSequence.isEmpty())
            return updateViewState(
                    _mainViewStateLiveData.value?.copy(
                            currentValueText = "",
                            isValueProvided = false,
                            conversionResultUiList = emptyList()
                    )
            )

        if (charSequence.length == 1 && charSequence[0] == '-')
            return

        try {
            val newValueText = charSequence.toString()
            val newValue = newValueText.toDouble()
            conversionData = conversionData.copy(sourceValue = newValue)
            val appTab = mainViewState.value?.appTab.orEmpty()
            val conversionOutputUiList = getConversionResultUiList(appTab)

            updateViewState(
                    _mainViewStateLiveData.value?.copy(
                            currentValueText = newValueText,
                            isValueProvided = true,
                            conversionResultUiList = conversionOutputUiList
                    )
            )

            Log.d(TAG, "Value received from TextEdit: $newValue")

        } catch (e: NumberFormatException) {
            Log.e(TAG, "Number Format Exception in TextEdit")
        }
    }

    private fun updateConversionDataAndViewState(appTab: String, sourceMeasureSystem: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentSourceValue = conversionData.sourceValue
            conversionData =
                conversionDataInteractor.getConversionData(appTab, sourceMeasureSystem)
                    .copy(sourceValue = currentSourceValue)

            val conversionOutputUiList = getConversionResultUiList(appTab)

            updateViewState(
                    _mainViewStateLiveData.value?.copy(
                            appTab = appTab,
                            measureSystemFrom = sourceMeasureSystem,
                            conversionResultUiList = conversionOutputUiList
                    )
            )
        }
    }

    private fun getConversionResultUiList(appTab: String): List<ConversionResultUi> {
        if (appTab.isBlank())
            return emptyList()

        val conversionOutputUiMapper =
            conversionResultUiMapperFactory.initializeMapper(appTab, conversionData)

        return conversionOutputUiMapper.map()
    }

    private fun updateViewState(newMainViewState: MainViewState?) {
        _mainViewStateLiveData.postValue(newMainViewState)
    }

    private fun setInitialViewState() {
        var initialValueText = ""
        if (INITIAL_IS_VALUE_PROVIDED)
            initialValueText = INITIAL_VALUE.toString()

        _mainViewStateLiveData.value = MainViewState(
                initialValueText,
                INITIAL_SYSTEM_FROM,
                INITIAL_APP_TAB,
                INITIAL_IS_VALUE_PROVIDED
        )

        updateConversionDataAndViewState(INITIAL_APP_TAB, INITIAL_SYSTEM_FROM)
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val conversionDataInteractor: ConversionDataInteractor,
    private val conversionResultUiMapperFactory: ConversionResultUiMapperFactory
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(
            conversionDataInteractor,
            conversionResultUiMapperFactory
        ) as T
    }
}

class ValueWrapper<T>(private val value: T) {
    private var isConsumed = false

    fun get(): T? =
        if (isConsumed)
            null
        else {
            isConsumed = true
            value
        }
}