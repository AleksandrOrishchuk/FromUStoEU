package com.ssho.fromustoeu.binding_adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssho.fromustoeu.ConvertBucketListFragment
import com.ssho.fromustoeu.ConvertBucket
import com.ssho.fromustoeu.closeSoftKeyboard

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
fun bindSubmitList(recyclerView: RecyclerView, convertBuckets: List<ConvertBucket>) {
    (recyclerView.adapter
            as ConvertBucketListFragment.ConvertBucketAdapter)
            .submitList(convertBuckets)
}