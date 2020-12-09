package com.ssho.fromustoeu.binding_adapters

import android.text.TextWatcher
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import androidx.databinding.BindingAdapter

@BindingAdapter("textChangedListener")
fun bindTextWatcher(editText: EditText, textWatcher: TextWatcher) {
    editText.addTextChangedListener(textWatcher)
}

@BindingAdapter("spinnerListener")
fun bindOnItemSelectedListener(spinner: Spinner, onItemSelectedListener: AdapterView.OnItemSelectedListener) {
    spinner.onItemSelectedListener = onItemSelectedListener
}