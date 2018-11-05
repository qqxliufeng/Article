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
        fun startArticleWebViewFragment(context: Context, title: String, url: String) {
            val articleWebViewFragment = ArticleWebViewFragment()
            articleWebViewFragment.arguments = bundleOf(Pair("url", url), Pair("title", title))
            FragmentContainerActivity.from(context).setNeedNetWorking(false).setTitle(title)
                .setClazz(ArticleWebViewFragment::class.java)
                .setExtraBundle(bundleOf(Pair("url", url), Pair("title", title))).start()
        }
    }

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
        when (arguments?.getString("title")) {
            "私密文章" -> {
                val switch = SwitchCompat(mContext)
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
            "公开文章" -> {
                val textView = TextView(mContext)
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent))
                textView.text = "创作"
                textView.textSize = 16.0f
                textView.setOnClickListener {
                    startActivity(Intent(mContext, ArticleEditActivity::class.java))
                    finish()
                }
                val layoutParams =
                    Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams.gravity = Gravity.RIGHT
                layoutParams.rightMargin = 15.0f.toDip(mContext).toInt()
                (mContext as FragmentContainerActivity).toolbar.addView(textView, layoutParams)
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
            Toast.makeText(mContext, "test", Toast.LENGTH_LONG).show()
            return "122w343w4"
        }

        @JavascriptInterface
        fun jump(type: Int) {
            Log.e("TAG", "$type")
            FragmentContainerActivity.from(mContext).setClazz(ArticleInfoForTrashFragment::class.java).setTitle("")
                .setNeedNetWorking(true).start()
        }

    }

}