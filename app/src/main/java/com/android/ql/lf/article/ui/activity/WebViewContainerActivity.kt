package com.android.ql.lf.article.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.Toast
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.UserInfo
import com.android.ql.lf.article.ui.fragments.mine.FeedBackFragment
import com.android.ql.lf.article.utils.JS_BRIDGE_INTERFACE_NAME
import com.android.ql.lf.article.utils.loadLocalHtml
import com.android.ql.lf.article.utils.setNormalSetting
import com.android.ql.lf.baselibaray.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_web_view_container_layout.*
import org.jetbrains.anko.toast

class WebViewContainerActivity : BaseActivity() {

    companion object {
        fun startWebViewContainerActivity(context: Context, url: String? = "") {
            val intent = Intent(context, WebViewContainerActivity::class.java)
            intent.putExtra("title", "")
            intent.putExtra("url", url)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_web_view_container_layout

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        mIvArticleWebViewClose.setOnClickListener { finish() }
        mIvArticleWebViewBack.setImageResource(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material)
        mIvArticleWebViewBack.setOnClickListener { onBack() }
        mIvArticleWebViewClose.setOnClickListener { finish() }

        mWVArticleWebViewContainer.setNormalSetting()
        mWVArticleWebViewContainer.addJavascriptInterface(WebViewInterface(), JS_BRIDGE_INTERFACE_NAME)

        mWVArticleWebViewContainer.webViewClient = MyWebViewClient()
        mWVArticleWebViewContainer.webChromeClient = MyChromeWebViewClient()
        val url = intent.getStringExtra("url") ?: ""
        mWVArticleWebViewContainer.loadLocalHtml(url)
        when (url) {
            "wallet.html" ->{
                mTvArticleWebViewAction.visibility = View.VISIBLE
                mTvArticleWebViewAction.text = "充值问题"
                mTvArticleWebViewAction.setTextColor(ContextCompat.getColor(this,R.color.colorAccent))
                mTvArticleWebViewAction.setOnClickListener {
                    FeedBackFragment.startFeedBackFragment(this,2)
                }
                mMLlWebViewContainer.registFragmentSizeObserver { wdiff, hdiff ->
                    if (hdiff < -200) { // 处理输入法弹出事件
                        mWVArticleWebViewContainer.loadUrl("""
                   javascript:(function(){
                        $(".withdraw-main").css({'top':'1%'})
                   }())
                """)
                    } else if (hdiff == 0) {  // 处理输入法隐藏事件
                        mWVArticleWebViewContainer.loadUrl("""
                   javascript:(function(){
                        $(".withdraw-main").css({'top':'10%'})
                   }())
                """)
                    }
                }
            }
            else -> {
                mTvArticleWebViewAction.visibility = View.GONE
            }
        }
    }

    override fun onBackPressed() {
        onBack()
    }

    private fun onBack() {
        if (mWVArticleWebViewContainer.canGoBack()) {
            mWVArticleWebViewContainer.goBack()
        } else {
            mWVArticleWebViewContainer.destroy()
            super.onBackPressed()
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
            if (url != null && url.endsWith("wallet.html")) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            } else {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            }
            mPbArticleProgress.visibility = View.GONE
            mTvArticleWebViewTitle.text = view?.title ?: ""
        }
    }

    inner class MyChromeWebViewClient : WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            mPbArticleProgress.progress = newProgress
        }

        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            toast("asdafd")
            return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
        }
    }

    inner class WebViewInterface {

        @JavascriptInterface
        fun getUserId(): String {
            return UserInfo.user_id.toString()
        }
    }

}