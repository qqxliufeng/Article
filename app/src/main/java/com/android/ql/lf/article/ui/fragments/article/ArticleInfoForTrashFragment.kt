package com.android.ql.lf.article.ui.fragments.article

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.android.ql.lf.article.R
import com.android.ql.lf.article.utils.loadLocalHtml
import com.android.ql.lf.article.utils.setNormalSetting
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import kotlinx.android.synthetic.main.fragment_article_info_for_trash_layout.*

class ArticleInfoForTrashFragment : BaseNetWorkingFragment() {

    override fun getLayoutId() = R.layout.fragment_article_info_for_trash_layout

    @SuppressLint("JavascriptInterface","")
    override fun initView(view: View?) {
        mWVArticleInfoForTrash.setNormalSetting()
        mWVArticleInfoForTrash.loadLocalHtml("article-info.html")
        mWVArticleInfoForTrash.webChromeClient = object : WebChromeClient(){}
        mWVArticleInfoForTrash.webViewClient = object : WebViewClient() {}
    }
}