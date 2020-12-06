package com.ssho.fromustoeu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.io.Serializable

class CBListFragmentViewModel(private val parentViewState: MainViewState) : ViewModel() {
    val fragmentViewState : LiveData<FragmentViewState> get() = _fragmentViewState
    private val _fragmentViewState: MutableLiveData<FragmentViewState> = MutableLiveData()

    init {
        getInitialViewState()
    }

    private fun getInitialViewState() {
        _fragmentViewState.value = FragmentViewState(getRecyclerListResID())
    }

    private fun getRecyclerListResID() : Int {
        val convertFrom: Int = parentViewState.convertFrom

        return when (parentViewState.appTab) {
            TAB_HOME -> {
                if (convertFrom == CONVERT_FROM_US)
                    R.array.home_tab_conversions_from_us
                else
                    R.array.home_tab_conversions_from_eu
            }
            else -> 0
        }
    }
}

@Suppress("UNCHECKED_CAST")
class CBListFragmentViewModelFactory(private val parentViewState: MainViewState) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CBListFragmentViewModel(parentViewState) as T
    }

}

data class FragmentViewState (var recyclerListResID: Int) : Serializable