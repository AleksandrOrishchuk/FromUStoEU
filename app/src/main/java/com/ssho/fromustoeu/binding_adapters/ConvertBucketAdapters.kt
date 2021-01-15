package com.ssho.fromustoeu.binding_adapters

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ssho.fromustoeu.R
import com.ssho.fromustoeu.copyTextToSystemClipboard
import com.ssho.fromustoeu.showLongToast
import java.util.*

@BindingAdapter("copyToClipboardOnLongClick")
fun bindCopyToClipboardOnLongClick(view: View, text: String?) {
    view.setOnLongClickListener {
        copyTextToSystemClipboard(it.context, text.orEmpty())
        showLongToast(it.context, R.string.copied_to_clipboard)

        true
    }
}

@BindingAdapter("setTextFromStringResourceName")
fun bindResourceIdFromResourceName(textView: TextView, resourceName: String?) {
    if (resourceName == null || resourceName.isEmpty()) {
        textView.text = ""
        return
    }

    val context = textView.context
    val resourceId = context.resources.getIdentifier(resourceName.toLowerCase(Locale.ROOT),
            "string",
            context.applicationInfo.packageName)

    if (resourceId == 0)
        textView.text = ""
    else
        textView.setText(resourceId)
}