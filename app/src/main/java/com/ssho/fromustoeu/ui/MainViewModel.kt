package com.ssho.fromustoeu.ui

import android.util.Log
import androidx.annotation.IdRes
import androidx.lifecycle.*
import com.ssho.fromustoeu.data.ConversionDataInteractor
import com.ssho.fromustoeu.data.ResultWrapper
import com.ssho.fromustoeu.data.model.ConversionData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"
private const val INITIAL_VALUE = 55.5
private const val INITIAL_MEASURE_SYSTEM = FROM_IMPERIAL_US
private const val INITIAL_APP_TAB = TAB_HOME
private const val INITIAL_IS_VALUE_PROVIDED = false

class MainViewModel(
        private val conversionDataInteractor: ConversionDataInteractor,
        private val conversionResultUiMapperFactory: ConversionResultUiMapperFactory,
        private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    ) : ViewModel() {

    val mainViewState: LiveData<MainViewState> get() = _mainViewState
    val appTab: Int get() = _appTap
    val sourceValueEditText: String get() = _sourceValueEditText
    private val _mainViewState: MutableLiveData<MainViewState> = MutableLiveData()
    private var _appTap = INITIAL_APP_TAB
    private var _sourceValueEditText = if (INITIAL_IS_VALUE_PROVIDED) INITIAL_VALUE.toString() else ""
    private var currentMeasureSystem = INITIAL_MEASURE_SYSTEM
    private lateinit var currentConversionData: ConversionData

    init {
        refreshConversionDataAndUI()
        Log.d(TAG, "MainViewModel Initial load complete.")
    }

    fun onChangeMeasureSystemClick() {
        currentMeasureSystem =
                if (currentMeasureSystem == FROM_IMPERIAL_US)
                    FROM_METRIC_EU
                else
                    FROM_IMPERIAL_US

        currentConversionData = currentConversionData.swapConversionPairs()
        refreshUI()
    }

    fun onChangeAppTab(newAppTab: Int): Boolean {
        if (newAppTab == appTab)
            return false

        _appTap = newAppTab
        refreshConversionDataAndUI()

        return true
    }

    fun onSourceValueChanged(charSequence: CharSequence?) {
        if (charSequence == null || charSequence.isEmpty()) {
            _sourceValueEditText = ""
            return refreshUI()
        }

        if (charSequence.length == 1 && charSequence[0] == '-')
            return

        try {
            val newValueText = charSequence.toString()
            val newValueDouble = newValueText.toDouble()
            _sourceValueEditText = newValueText

            refreshUI()

            Log.d(TAG, "Value received from TextEdit: $newValueDouble")

        } catch (e: NumberFormatException) {
            Log.e(TAG, "Number Format Exception in TextEdit")
        }
    }

    fun onForceRefreshUI() {
        viewModelScope.launch(dispatcher) {
            updateViewStateLiveData(
                    MainViewState.Loading)
            val response = conversionDataInteractor.getLatestConversionData(appTab)
            unwrapResponseAndUpdateUI(response)
        }
    }

    private fun refreshUI() {
        if (mainViewState.value is MainViewState.Error || mainViewState.value is MainViewState.Loading)
            return
        if (sourceValueEditText.isBlank())
            return updateViewStateLiveData(MainViewState.NoSourceValue)

        updateViewStateLiveData(
                MainViewState.Result(
                        conversionResultUiList = mapCurrentResultUiList()
                )
        )
    }

    private fun refreshConversionDataAndUI() {
        viewModelScope.launch(dispatcher) {
            updateViewStateLiveData(
                    MainViewState.Loading
            )
            val response = conversionDataInteractor.getConversionData(appTab)
            unwrapResponseAndUpdateUI(response)
        }
    }

    private fun unwrapResponseAndUpdateUI(response: ResultWrapper<ConversionData>) {
        when (response) {
            is ResultWrapper.Success -> {
                currentConversionData = response.value

                if (currentMeasureSystem != FROM_IMPERIAL_US)
                    currentConversionData = currentConversionData.swapConversionPairs()

                updateViewStateLiveData(
                        if (sourceValueEditText.isBlank())
                            MainViewState.NoSourceValue
                        else
                            MainViewState.Result(conversionResultUiList = mapCurrentResultUiList())
                )
            }
            else ->
                updateViewStateLiveData(MainViewState.Error)
        }
    }

    private fun mapCurrentResultUiList(): List<ConversionResultUi> {
        return conversionResultUiMapperFactory
                .initializeMapper(
                        appTab = appTab,
                        conversionData = currentConversionData
                ).map(sourceValueEditText.toDoubleOrNull())
    }

    private fun updateViewStateLiveData(newMainViewState: MainViewState?) {
        _mainViewState.postValue(newMainViewState)
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

//class OneTimeConsumingValue<T>(private val value: T) {
//    private var isConsumed = false
//
//    fun get(): T? =
//        if (isConsumed)
//            null
//        else {
//            isConsumed = true
//            value
//        }
//}