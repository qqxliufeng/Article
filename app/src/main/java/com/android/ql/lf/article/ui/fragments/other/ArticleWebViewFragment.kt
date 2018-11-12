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

class ArticleWebViewFragment : BaseNetWorkingFragment(), FragmentContainerActivity.OnBackPressListener {

    companion object {
        fun startArticleWebViewFragment(context: Context, title: String, url: String, type: Int) {
            FragmentContainerActivity.from(context).setNeedNetWorking(false).setTitle(title)
                .setClazz(ArticleWebViewFragment::class.java)
                .setExtraBundle(bundleOf(Pair("url", url), Pair("title", title),Pair("type", type))).start()
        }
    }

    private val switch by lazy { SwitchCompat(mContext) }

    override fun getLayoutId() = R.layout.fragment_article_web_view_layout

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    override fun initView(view: View?) {
        if (mContext is FragmentContainerActivity) {
            (mContext as FragmentContainerActivity).setOnBackPressListener(this)
        }
        mWVArticleWebViewContainer.setNormalSetting()
        mWVArticleWebViewContainer.addJavascriptInterface(WebViewInterface(), "article")
        mWVArticleWebViewContainer.settings.javaScriptCanOpenWindowsAutomatically = true

        mWVArticleWebViewContainer.webViewClient = MyWebViewClient()
        mWVArticleWebViewContainer.webChromeClient = MyChromeWebViewClient()
        val url = arguments?.getString("url") ?: ""
        mWVArticleWebViewContainer.loadLocalHtml(url)
        when (ArticleType.getTypeNameById(arguments?.getInt("type") ?: 0)) {
            ArticleType.PRIVATE_ARTICLE -> {
                switch.setOnCheckedChangeListener { compoundButton, b ->
                    if (b) {
                        toast("已经开启编辑模式\n点击文章进入编辑器")
                    } else {
                        toast("已经开启预览模式\n点击文章进入预览")
                    }
                }
                val layoutParams =
                    Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams.gravity = Gravity.RIGHT
                (mContext as FragmentContainerActivity).toolbar.addView(switch, layoutParams)
            }
            ArticleType.PUBLIC_ARTICLE -> {
            }
            ArticleType.COLLECTION_ARTICLE -> {
            }
            ArticleType.POST_ARTICLE -> {
            }
            else -> {
            }
        }
    }

    override fun onBackPress(): Boolean {
        onBack()
        return true
    }

    private fun onBack() {
        if (mWVArticleWebViewContainer.canGoBack()) {
            mWVArticleWebViewContainer.goBack()
        } else {
            finish()
        }
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

    inner class WebViewInterface {

        @JavascriptInterface
        fun getUserId(): String {
            return UserInfo.user_id.toString()
        }

        @JavascriptInterface
        fun getArticleType() = arguments?.getInt("type") ?: 0

        @JavascriptInterface
        fun jump(type: Int) {
            when (ArticleType.getTypeNameById(type)) {
                ArticleType.PRIVATE_ARTICLE -> {
                    ArticleEditActivity.startArticleEditActivity(mContext,"title","content",switch.isChecked)
                }
                ArticleType.PUBLIC_ARTICLE -> {
                }
                ArticleType.COLLECTION_ARTICLE -> {
                }
                ArticleType.POST_ARTICLE -> {
                }
                else -> {
                }
            }
//            FragmentContainerActivity.from(mContext).setClazz(ArticleInfoForTrashFragment::class.java).setTitle("")
//                .setNeedNetWorking(true).start()
        }
    }

}