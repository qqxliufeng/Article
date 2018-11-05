package com.android.ql.lf.article.utils

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView


@SuppressLint("SetJavaScriptEnabled")
fun WebView.setNormalSetting() {
    settings.javaScriptEnabled = true
    settings.domStorageEnabled = true
    settings.useWideViewPort = true
    settings.loadWithOverviewMode = true
    settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
}


fun WebView.urlHanler(url: String?) {
    if (!url.isNullOrEmpty()) {
        //根据url 查找处理的方法 执行方法
    }
}

fun WebView.loadLocalHtml(url: String = "") {
    loadUrl("file:///android_asset/$url")
}