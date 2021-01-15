package com.ssho.fromustoeu

import android.util.Log
import androidx.lifecycle.*

private const val TAG = "CBListFragmentVM"

class CBListFragmentViewModel(private val parentViewState: MainViewState) : ViewModel() {
    val fragmentViewState: LiveData<FragmentViewState> get() = _fragmentViewState
    private val _fragmentViewState = MutableLiveData(FragmentViewState())

    init {
        if (parentViewState.convertBucketList.isNotEmpty())
            fillBucketsWithValue()
        Log.d(TAG, "Initialized")
    }

    @Suppress("UNCHECKED_CAST")
    private fun fillBucketsWithValue() {
        val filledBuckets = mutableListOf<ConvertBucket>()
        if (parentViewState.appTab == TAB_HOME) {
            parentViewState.convertBucketList as List<MeasureBucket>
            parentViewState.convertBucketList.forEach {
                filledBuckets.add(it.copy(sourceValueText = parentViewState.currentValueText))
            }
        }
        else {
            parentViewState.convertBucketList as List<CurrencyBucket>
            parentViewState.convertBucketList.forEach {
                filledBuckets.add(it.copy(sourceValueText = parentViewState.currentValueText))
            }
        }

        _fragmentViewState.postValue(
                FragmentViewState(convertBucketsForRecycler = filledBuckets)
        )
    }
}

@Suppress("UNCHECKED_CAST")
class CBListFragmentViewModelFactory(
    private val parentViewState: MainViewState
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CBListFragmentViewModel(parentViewState) as T
    }
}

data class FragmentViewState(
    val convertBucketsForRecycler: List<ConvertBucket> = emptyList()
)