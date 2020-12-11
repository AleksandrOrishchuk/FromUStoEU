package com.ssho.fromustoeu

import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.io.Serializable

private const val TAG = "CBListFragment"

class CBListFragmentViewModel(private val parentViewState: MainViewState) : ViewModel() {
    val fragmentViewState : LiveData<FragmentViewState> get() = _fragmentViewState
    val isRecyclerViewScrolling: LiveData<Boolean> get() = _isRecyclerViewScrolling
    private val _fragmentViewState: MutableLiveData<FragmentViewState> = MutableLiveData()
    private val _isRecyclerViewScrolling: MutableLiveData<Boolean> = MutableLiveData(false)

    private val convertBucketRepository = ConvertBucketRepository.get()

    val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                _isRecyclerViewScrolling.value = true
            if (newState == RecyclerView.SCROLL_STATE_IDLE)
                _isRecyclerViewScrolling.value = false
        }
    }

    init {
        setInitialViewState()
    }

    private fun setInitialViewState() {
        viewModelScope.launch {
            val convertBuckets = selectConvertBucketsFromDatabase()

            _fragmentViewState.postValue(
                    FragmentViewState(convertBucketsForRecycler = convertBuckets)
            )
        }
    }

    private suspend fun selectConvertBucketsFromDatabase(): List<ConvertBucket> {
        val appTab = parentViewState.appTab
        val measureSystemFrom = parentViewState.measureSystemFrom

        return convertBucketRepository.getBuckets(appTab, measureSystemFrom).onEach { bucket ->
            bucket.sourceValueText = parentViewState.currentValueText
        }
    }
}

@Suppress("UNCHECKED_CAST")
class CBListFragmentViewModelFactory(private val parentViewState: MainViewState) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CBListFragmentViewModel(parentViewState) as T
    }

}

data class FragmentViewState (var convertBucketsForRecycler: List<ConvertBucket> = emptyList()) : Serializable