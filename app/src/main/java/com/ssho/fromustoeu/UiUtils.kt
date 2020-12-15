package com.ssho.fromustoeu

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.util.*


fun closeSoftKeyboard(context: Context, itemView: View) {
    val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(itemView.windowToken, 0)
}

fun showLongToast(context: Context, messageResId: Int) {
    Toast.makeText(context, messageResId, Toast.LENGTH_LONG).show()
}

fun copyTextToSystemClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("simple text", text)
    clipboard.setPrimaryClip(clip)
}

fun resolveActivityFor(context: Context, intent: Intent): ResolveInfo? {
    val packageManager = context.packageManager
    return packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
}

fun getIntentBasedOnAppName(context: Context, nameContains: String): Intent? {
    val packageManager = context.packageManager
    val packages = packageManager.getInstalledPackages(0)
    packages.forEach {
        if (it.packageName.toString().toLowerCase(Locale.ROOT).contains(nameContains))
            return packageManager.getLaunchIntentForPackage(it.packageName)
    }
    return null
}