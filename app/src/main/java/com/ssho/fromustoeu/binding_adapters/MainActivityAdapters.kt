package com.ssho.fromustoeu.binding_adapters

import android.text.TextWatcher
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssho.fromustoeu.*
import com.ssho.fromustoeu.ui.TAB_CURRENCY
import com.ssho.fromustoeu.ui.TAB_HOME
import com.ssho.fromustoeu.ui.closeSoftKeyboard

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