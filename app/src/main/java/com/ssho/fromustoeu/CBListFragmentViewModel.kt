package com.ssho.fromustoeu

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.io.Serializable

private const val TAG = "CBListFragment"

class CBListFragmentViewModel(private val parentViewState: MainViewState) : ViewModel() {
    val fragmentViewState : LiveData<FragmentViewState> get() = _fragmentViewState
    private val _fragmentViewState: MutableLiveData<FragmentViewState> = MutableLiveData(FragmentViewState())

    private val convertBucketRepository = ConvertBucketRepository.get()
    val convertBucketsLiveData = selectConvertBucketsFromDatabase()



    private fun selectConvertBucketsFromDatabase(): LiveData<List<ConvertBucket>> {
        val appTab = parentViewState.appTab
        val measureSystemFrom = parentViewState.measureSystemFrom

        return convertBucketRepository.getBuckets(appTab, measureSystemFrom)
    }

}

@Suppress("UNCHECKED_CAST")
class CBListFragmentViewModelFactory(private val parentViewState: MainViewState) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CBListFragmentViewModel(parentViewState) as T
    }

}

data class FragmentViewState (var convertBucketsForRecycler: Array<ConvertBucket> = emptyArray()) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FragmentViewState

        if (!convertBucketsForRecycler.contentEquals(other.convertBucketsForRecycler)) return false

        return true
    }

    override fun hashCode(): Int {
        return convertBucketsForRecycler.contentHashCode()
    }
}