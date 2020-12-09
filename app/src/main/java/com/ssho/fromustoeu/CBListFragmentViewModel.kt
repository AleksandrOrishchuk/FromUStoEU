package com.ssho.fromustoeu

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.io.Serializable

private const val TAG = "CBListFragment"

class CBListFragmentViewModel(private val parentViewState: MainViewState) : ViewModel() {
    val fragmentViewState : LiveData<FragmentViewState> get() = _fragmentViewState
    private val _fragmentViewState: MutableLiveData<FragmentViewState> = MutableLiveData()

    private val convertBucketRepository = ConvertBucketRepository.get()

    init {
        setInitialViewState()
    }

    private fun setInitialViewState() {
        _fragmentViewState.value = FragmentViewState()

        viewModelScope.launch {
            val convertBuckets = selectConvertBucketsFromDatabase()

            _fragmentViewState.postValue(
                _fragmentViewState.value?.copy(convertBucketsForRecycler = convertBuckets)
            )
        }
    }

    private suspend fun selectConvertBucketsFromDatabase(): List<ConvertBucket> {
        val appTab = parentViewState.appTab
        val measureSystemFrom = parentViewState.measureSystemFrom

        return when (appTab) {
            "home" -> convertBucketRepository.getBuckets(appTab, measureSystemFrom)
            else -> emptyList()
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