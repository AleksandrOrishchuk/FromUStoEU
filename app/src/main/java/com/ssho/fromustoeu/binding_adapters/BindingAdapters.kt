package com.ssho.fromustoeu.binding_adapters

import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.AbsListViewBindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("textChangedListener")
fun bindTextWatcher(editText: EditText, textWatcher: TextWatcher) {
    editText.addTextChangedListener(textWatcher)
}

@BindingAdapter("spinnerListener")
fun bindOnItemSelectedListener(spinner: Spinner, onItemSelectedListener: AdapterView.OnItemSelectedListener) {
    spinner.onItemSelectedListener = onItemSelectedListener
}

@BindingAdapter("onFocusChangedListener")
fun bindOnFocusChangedListener(editText: EditText, onFocusChangeListener: View.OnFocusChangeListener) {
    editText.onFocusChangeListener = onFocusChangeListener
}

@BindingAdapter("onScrollListener")
fun bindOnScrollChangeListener(recyclerView: RecyclerView, onScrollListener: RecyclerView.OnScrollListener) {
    recyclerView.addOnScrollListener(onScrollListener)
}

@BindingAdapter("setTextFromStringResourceName")
fun bindResourceIdFromResourceName(textView: TextView, resourceName: String?) {
    if (resourceName == null || resourceName.isEmpty()) {
        textView.text = ""
        return
    }

    val context = textView.context
    val resourceId = context.resources.getIdentifier(resourceName,
            "string",
            context.applicationInfo.packageName)

    if (resourceId == 0)
        textView.text = ""
    else
        textView.setText(resourceId)
}
