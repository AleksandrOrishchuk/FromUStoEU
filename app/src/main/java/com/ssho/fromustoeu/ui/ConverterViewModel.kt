package com.ssho.fromustoeu.ui

import android.util.Log
import androidx.lifecycle.*
import com.ssho.fromustoeu.domain.ConversionResult
import com.ssho.fromustoeu.domain.GetConversionResultUseCase
import com.ssho.fromustoeu.domain.NoValueProvidedException
import com.ssho.fromustoeu.ui.model.ConversionResultUi
import com.ssho.fromustoeu.ui.model.ConverterUiInputData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MainViewModel"

internal class ConverterViewModel(
    private val getConversionResult: GetConversionResultUseCase,
) : ViewModel() {

    companion object {
        private const val INITIAL_VALUE = 55.5
        private val INITIAL_CONVERSION_DIRECTION = ConversionDirection.FROM_IMPERIAL_US
        private val INITIAL_APP_TAB = AppTab.TAB_HOME
        private const val INITIAL_IS_VALUE_PROVIDED = false
    }

    val converterViewState: LiveData<ConverterViewState> get() = _converterViewState
    val converterUiInputData: ConverterUiInputData get() = _converterUiInputData
    private val _converterViewState: MutableLiveData<ConverterViewState> = MutableLiveData()
    private var _converterUiInputData = getInitialConverterUiData()

    init {
        calculateResultAndUpdateViewState()
        Log.d(TAG, "MainViewModel Initial load complete.")
    }

    fun onChangeAppTab(newAppTab: AppTab): Boolean {
        if (newAppTab == converterUiInputData.appTab)
            return false

        _converterUiInputData = converterUiInputData.copy(appTab = newAppTab)

        calculateResultAndUpdateViewState()

        return true
    }

    fun onChangeMeasureSystemClick() {
        _converterUiInputData = converterUiInputData.copy(
            conversionDirection =
                if (converterUiInputData.conversionDirection == ConversionDirection.FROM_IMPERIAL_US)
                    ConversionDirection.FROM_METRIC_EU
                else
                    ConversionDirection.FROM_IMPERIAL_US
        )
        calculateResultAndUpdateViewState()
    }

    fun onSourceValueChanged(charSequence: CharSequence?) {
        if (charSequence == null || charSequence.isEmpty()) {
            _converterUiInputData = converterUiInputData.copy(sourceValue = "")

            return setNoValueProvidedViewState()
        }

        if (charSequence.length == 1 && charSequence[0] == '-' || charSequence[charSequence.length - 1] == '.')
            return

        try {
            val newValueText = charSequence.toString()
            val newValueDouble = newValueText.toDouble()
            _converterUiInputData = converterUiInputData.copy(sourceValue = newValueText)

            calculateResultAndUpdateViewState()

            Log.d(TAG, "Value received from TextEdit: $newValueDouble")

        } catch (e: NumberFormatException) {
            Log.e(TAG, "Number Format Exception in TextEdit")
        }
    }

    fun onForceRefreshUI() {
        calculateResultAndUpdateViewState()
    }

    private fun calculateResultAndUpdateViewState() {
        viewModelScope.launch {
            setLoadingViewState()
            runCatching {
                withContext(Dispatchers.IO) {
                    getConversionResult(converterUiInputData)
                }
            }.onSuccess {
                setResultViewState(it)
            }.onFailure { error ->
                if (error is NoValueProvidedException)
                    setNoValueProvidedViewState()
                else
                    setNetworkErrorViewState()
            }
        }
    }

    private fun setResultViewState(conversionResult: List<ConversionResult>) {
        val conversionResultUiList = conversionResult.map {
            ConversionResultUi(
                sourceName = it.sourceName,
                resultValueText = getValueText(it.result),
                targetName = it.targetName
            )
        }
        _converterViewState.value = ConverterViewState.Result(conversionResultUiList)
    }

    private fun setNoValueProvidedViewState() {
        _converterViewState.value = ConverterViewState.NoValueProvided
    }

    private fun setNetworkErrorViewState() {
        _converterViewState.value = ConverterViewState.Error
    }

    private fun setLoadingViewState() {
        _converterViewState.value = ConverterViewState.Loading
    }

    private fun getInitialConverterUiData(): ConverterUiInputData {
        val sourceValue = if (INITIAL_IS_VALUE_PROVIDED)
            INITIAL_VALUE.toString()
        else
            ""

        return ConverterUiInputData(
            sourceValue,
            INITIAL_CONVERSION_DIRECTION,
            INITIAL_APP_TAB
        )
    }

}

@Suppress("UNCHECKED_CAST")
internal class ConverterViewModelFactory(
    private val getConversionResultUseCase: GetConversionResultUseCase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ConverterViewModel(
            getConversionResultUseCase,
        ) as T
    }
}
