package com.android.ql.lf.article.utils

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import org.jetbrains.anko.windowManager

fun Context.getScreen(): Pair<Int, Int> {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
}

fun Fragment.getScreen() = context?.getScreen()


fun Context.showSoftInput(view:View) {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
}

fun Context.hideSoftInput(view:View) {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}