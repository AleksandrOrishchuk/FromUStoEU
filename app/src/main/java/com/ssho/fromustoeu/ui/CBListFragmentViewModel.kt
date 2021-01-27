package com.ssho.fromustoeu.ui

import androidx.lifecycle.*

private const val TAG = "CBListFragmentVM"

class CBListFragmentViewModel(conversionResultUiList: List<ConversionResultUi>) : ViewModel() {
    val fragmentViewState: LiveData<FragmentViewState> get() = _fragmentViewState
    private val _fragmentViewState = MutableLiveData(FragmentViewState(conversionResultUiList))

}

@Suppress("UNCHECKED_CAST")
class CBListFragmentViewModelFactory(
    private val conversionResultUiList: List<ConversionResultUi>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CBListFragmentViewModel(conversionResultUiList) as T
    }
}

data class FragmentViewState(
    val convertBucketsForRecycler: List<ConversionResultUi> = emptyList()
)