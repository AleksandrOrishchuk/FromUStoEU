package com.ssho.fromustoeu

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"
private const val INITIAL_VALUE = 55.5
private const val INITIAL_SYSTEM_FROM = FROM_IMPERIAL_US
private const val INITIAL_APP_TAB = TAB_HOME
private const val INITIAL_IS_VALUE_PROVIDED = false

class MainViewModel(
    private val currencyBucketsProvider: CurrencyBucketsProvider,
    private val measureBucketsProvider: MeasureBucketsProvider) : ViewModel() {

    val mainViewStateLiveData: LiveData<MainViewState> get() = _mainViewStateLiveData
    val calculatorIntentLiveData: LiveData<Intent> get() = _calculatorIntentLiveData
    private val _mainViewStateLiveData = MutableLiveData<MainViewState>()
    private val _calculatorIntentLiveData = MutableLiveData<Intent>()

    val valueWatcher = object : TextWatcher {
        override fun onTextChanged(
            charSequence: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
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

                Log.d(TAG, "Value received from TextEdit: $newValue")

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

    val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            if (item.itemId == R.id.tab_calculator) {
                val calcIntent = Intent.makeMainSelectorActivity(
                    Intent.ACTION_MAIN,
                    Intent.CATEGORY_APP_CALCULATOR
                ).apply {
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
                val currentMeasureSystem = mainViewStateLiveData.value?.measureSystemFrom ?: 1

                updateBucketListViewState(newAppTab, currentMeasureSystem)
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
        val currentMeasureSystem = mainViewStateLiveData.value?.measureSystemFrom
        val newMeasureFrom =
                if (currentMeasureSystem == FROM_IMPERIAL_US)
                    FROM_METRIC_EU
                else
                    FROM_IMPERIAL_US

        val currentTab = mainViewStateLiveData.value?.appTab.orEmpty()
        updateBucketListViewState(currentTab, newMeasureFrom)
    }

    private fun updateBucketListViewState(appTab: String, measureSystemFrom: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val convertBucketList = getConvertBucketList(appTab, measureSystemFrom)
            updateViewState(
                    _mainViewStateLiveData.value?.copy(
                            appTab = appTab,
                            measureSystemFrom = measureSystemFrom,
                            convertBucketList = convertBucketList
                    )
            )
        }
    }

    private suspend fun getConvertBucketList(appTab: String?, measureSystemFrom: Int): List<ConvertBucket> {
        val convertBucketsProvider = if (appTab == TAB_HOME) measureBucketsProvider
        else currencyBucketsProvider

        return convertBucketsProvider.getBucketList(measureSystemFrom).also {
            Log.d(TAG, "Bucket list received. Size: ${it.size}")
        }
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

        updateBucketListViewState(INITIAL_APP_TAB, INITIAL_SYSTEM_FROM)
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val currencyBucketsProvider: CurrencyBucketsProvider,
    private val measureBucketsProvider: MeasureBucketsProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(
            currencyBucketsProvider,
            measureBucketsProvider
        ) as T
    }
}