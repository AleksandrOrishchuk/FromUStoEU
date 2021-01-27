package com.ssho.fromustoeu.ui

import androidx.lifecycle.*

private const val TAG = "CBListFragmentVM"

class CResultListFragmentViewModel(conversionResultUiList: List<ConversionResultUi>) : ViewModel() {
    val fragmentViewState: LiveData<FragmentViewState> get() = _fragmentViewState
    private val _fragmentViewState = MutableLiveData(FragmentViewState(conversionResultUiList))

}

@Suppress("UNCHECKED_CAST")
class CResultListFragmentViewModelFactory(
    private val conversionResultUiList: List<ConversionResultUi>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CResultListFragmentViewModel(conversionResultUiList) as T
    }
}

data class FragmentViewState(
    val convertBucketsForRecycler: List<ConversionResultUi> = emptyList()
)