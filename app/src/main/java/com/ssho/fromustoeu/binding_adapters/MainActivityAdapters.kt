package com.ssho.fromustoeu.binding_adapters

import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssho.fromustoeu.*

@BindingAdapter("textChangedListener")
fun bindTextWatcher(editText: EditText, textWatcher: TextWatcher) {
    editText.addTextChangedListener(textWatcher)
}

@BindingAdapter("closeKeyboardOnFocusChanged")
fun bindCloseKeyboardOnFocusChanged(editText: EditText, isActive: Boolean) {
    if (isActive)
        editText.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus)
                closeSoftKeyboard(view.context, view)
        }
}

@BindingAdapter("selectItemByCurrentTab")
fun bindItemIdfFromCurrentTab(navigationView: BottomNavigationView, currentAppTab: String) {
    navigationView.selectedItemId = when (currentAppTab) {
        TAB_HOME -> R.id.tab_home
        TAB_CURRENCY -> R.id.tab_currency

        else -> R.id.tab_home
    }
}