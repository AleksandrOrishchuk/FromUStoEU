package com.ssho.fromustoeu.binding_adapters

import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssho.fromustoeu.R
import com.ssho.fromustoeu.TAB_CURRENCY
import com.ssho.fromustoeu.TAB_HOME

@BindingAdapter("textChangedListener")
fun bindTextWatcher(editText: EditText, textWatcher: TextWatcher) {
    editText.addTextChangedListener(textWatcher)
}

@BindingAdapter("onFocusChangedListener")
fun bindOnFocusChangedListener(editText: EditText, onFocusChangeListener: View.OnFocusChangeListener) {
    editText.onFocusChangeListener = onFocusChangeListener
}

@BindingAdapter("onScrollListener")
fun bindOnScrollChangeListener(recyclerView: RecyclerView, onScrollListener: RecyclerView.OnScrollListener) {
    recyclerView.addOnScrollListener(onScrollListener)
}

@BindingAdapter("onLongClickListener")
fun bindOnLongClickListener(view: View, onLongClickListener: View.OnLongClickListener) {
    view.setOnLongClickListener(onLongClickListener)
}

@BindingAdapter("onNavigationItemSelectedListener")
fun bindOnNavigationItemSelectedListener(navigationView: BottomNavigationView, l: BottomNavigationView.OnNavigationItemSelectedListener) {
    navigationView.setOnNavigationItemSelectedListener(l)
}

@BindingAdapter("selectItemByCurrentTab")
fun bindItemIdfFromCurrentTab(navigationView: BottomNavigationView, currentAppTab: String) {
    navigationView.selectedItemId = when (currentAppTab) {
        TAB_HOME -> R.id.tab_home
        TAB_CURRENCY -> R.id.tab_currency

        else -> R.id.tab_home
    }
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
