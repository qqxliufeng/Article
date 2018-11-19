package com.android.ql.lf.article.utils

import android.annotation.SuppressLint
import android.util.TypedValue
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

const val JS_BRIDGE_INTERFACE_NAME:String = "article"

@SuppressLint("SetJavaScriptEnabled")
fun WebView.setNormalSetting() {
    settings.javaScriptEnabled = true
    settings.domStorageEnabled = true
    settings.useWideViewPort = true
    settings.defaultFontSize  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,10.0f,this.resources.displayMetrics).toInt()
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

fun WebView.resetImage(){
    loadUrl("javascript:(function(){" +
            "var objs = document.getElementsByTagName('img'); " +
            "for(var i=0;i<objs.length;i++)  " +
            "{"
            + "var img = objs[i];   " +
            "    img.style.width = '100%'; img.style.height = 'auto';  " +
            "}" +
            "})()")
}

fun WebView.setBaseWebViewClient(){
    this.webViewClient = object : WebViewClient(){
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }
    }
}