package com.android.ql.lf.article.ui.fragments.other

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.content.ContextCompat
import android.support.v7.widget.SwitchCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.ArticleType
import com.android.ql.lf.article.data.UserInfo
import com.android.ql.lf.article.ui.activity.ArticleEditActivity
import com.android.ql.lf.article.ui.fragments.article.ArticleInfoForTrashFragment
import com.android.ql.lf.article.utils.loadLocalHtml
import com.android.ql.lf.article.utils.setNormalSetting
import com.android.ql.lf.article.utils.toDip
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import kotlinx.android.synthetic.main.fragment_article_web_view_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast

class NteWebViewFragment : BaseNetWorkingFragment() {

    companion object {
        fun startNetWebViewFragment(context: Context, url: String) {
            FragmentContainerActivity.from(context).setNeedNetWorking(false).setTitle("")
                .setClazz(NteWebViewFragment::class.java)
                .setExtraBundle(bundleOf(Pair("url", url))).start()
        }
    }

    private val switch by lazy { SwitchCompat(mContext) }

    override fun getLayoutId() = R.layout.fragment_article_web_view_layout

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    override fun initView(view: View?) {
        mWVArticleWebViewContainer.setNormalSetting()
        mWVArticleWebViewContainer.settings.javaScriptCanOpenWindowsAutomatically = true
        mWVArticleWebViewContainer.webViewClient = MyWebViewClient()
        mWVArticleWebViewContainer.webChromeClient = MyChromeWebViewClient()
        val url = arguments?.getString("url") ?: ""
        mWVArticleWebViewContainer.loadUrl(url)
    }

    inner class MyWebViewClient : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            mPbArticleProgress.visibility = View.VISIBLE
            mPbArticleProgress.progress = 0
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view?.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            mPbArticleProgress.visibility = View.GONE
//            mTvArticleWebViewTitle.text = view?.title ?: ""
        }
    }

    inner class MyChromeWebViewClient : WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            mPbArticleProgress.progress = newProgress
        }
    }
}