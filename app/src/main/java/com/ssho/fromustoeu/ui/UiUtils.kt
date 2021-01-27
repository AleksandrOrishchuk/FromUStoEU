package com.ssho.fromustoeu.ui

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
import kotlin.math.floor
import kotlin.math.round


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
    val startupIntent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
    }

    val activities = packageManager.queryIntentActivities(startupIntent, 0)
    activities.forEach {
        if (it.activityInfo.packageName.toLowerCase(Locale.ROOT).contains(nameContains))
            return packageManager.getLaunchIntentForPackage(it.activityInfo.packageName)
    }
    return null
}

fun getValueText(convertedValue: Double): String {
    return if (floor(convertedValue) == convertedValue)
        convertedValue.toInt().toString()
    else
        String.format("%.2f", round(convertedValue * 100) / 100)
                .replace(',', '.')
}