package com.android.ql.lf.article.utils

import android.content.Context
import android.support.v4.app.Fragment
import android.util.DisplayMetrics
import org.jetbrains.anko.windowManager

fun Context.getScreen(): Pair<Int, Int> {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
}

fun Fragment.getScreen() = context?.getScreen()