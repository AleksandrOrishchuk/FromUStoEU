package com.ssho.fromustoeu.binding_adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssho.fromustoeu.ui.ConversionResultUi
import com.ssho.fromustoeu.ui.ConversionResultListFragment
import com.ssho.fromustoeu.ui.closeSoftKeyboard

@BindingAdapter("closeKeyboardOnScroll")
fun bindCloseKeyboardOnScroll(recyclerView: RecyclerView, isActive: Boolean) {
    if (isActive) {
        val onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                    closeSoftKeyboard(recyclerView.context, recyclerView)
            }
        }
        recyclerView.addOnScrollListener(onScrollListener)
    }
}

@BindingAdapter("submitConvertBucketList")
fun bindSubmitList(recyclerView: RecyclerView, conversionResultUiList: List<ConversionResultUi>) {
    (recyclerView.adapter
            as ConversionResultListFragment.ConversionResultAdapter)
            .submitList(conversionResultUiList)
}