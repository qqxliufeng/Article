package com.android.ql.lf.article.ui.fragments.other

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.SwitchCompat
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.ArticleItem
import com.android.ql.lf.article.data.ArticleType
import com.android.ql.lf.article.data.UserInfo
import com.android.ql.lf.article.ui.activity.ArticleEditActivity
import com.android.ql.lf.article.ui.fragments.article.ArticleInfoDisplayFragment
import com.android.ql.lf.article.ui.fragments.article.ArticleInfoForNormalFragment
import com.android.ql.lf.article.ui.fragments.article.ArticleInfoForTrashFragment
import com.android.ql.lf.article.ui.fragments.article.Classify
import com.android.ql.lf.article.utils.JS_BRIDGE_INTERFACE_NAME
import com.android.ql.lf.article.utils.loadLocalHtml
import com.android.ql.lf.article.utils.setNormalSetting
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import com.android.ql.lf.baselibaray.utils.RxBus
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

    private val updateArticleListSubscription by lazy {
        RxBus.getDefault().toObservable(ArticleItem::class.java).subscribe {
           mWVArticleWebViewContainer.reload()
        }
    }

    override fun getLayoutId() = R.layout.fragment_article_web_view_layout

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    override fun initView(view: View?) {
        if (mContext is FragmentContainerActivity) {
            (mContext as FragmentContainerActivity).setOnBackPressListener(this)
        }
        updateArticleListSubscription
        mWVArticleWebViewContainer.setNormalSetting()
        mWVArticleWebViewContainer.addJavascriptInterface(WebViewInterface(), JS_BRIDGE_INTERFACE_NAME)

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

    override fun onDestroyView() {
        mWVArticleWebViewContainer.destroy()
        unsubscribe(updateArticleListSubscription)
        super.onDestroyView()
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
            mPbArticleProgress?.visibility = View.VISIBLE
            mPbArticleProgress?.progress = 0
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view?.loadUrl(url)
            return true
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            view?.loadUrl(request?.url?.path)
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            mPbArticleProgress?.visibility = View.GONE
//            mTvArticleWebViewTitle.text = view?.title ?: ""
        }
    }

    inner class MyChromeWebViewClient : WebChromeClient() {

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            mPbArticleProgress?.progress = newProgress
        }
    }

    /**
     * 用于Java/Kotlin和JavaScript相互调用的接口类
     */
    inner class WebViewInterface {

        /**
         * 获取用户ID
         */
        @JavascriptInterface
        fun getUserId(): String {
            return UserInfo.user_id.toString()
        }

        /**
         * 获取文章类型
         */
        @JavascriptInterface
        fun getArticleType():Int{
            return arguments?.getInt("type") ?: 0
        }

        /**
         * 根据不同的类型，跳转到不同的页面
         */
        @JavascriptInterface
        fun jump(type: Int,aid:String,title: String,content:String) {
            when (ArticleType.getTypeNameById(type)) {
                ArticleType.PRIVATE_ARTICLE -> {
                    if (switch.isChecked){
                        //进入编辑模式
                        ArticleEditActivity.startArticleEditActivity(mContext,
                            title,
                            content,
                            Classify(0,"",false,""),
                            1,
                            aid.toInt())
                    }else{
                        //进入预览模式
                        ArticleInfoDisplayFragment.startArticleInfoDisplayFragment(mContext,aid.toInt(),UserInfo.user_id)
                    }
                }
                ArticleType.PUBLIC_ARTICLE -> {
                    ArticleInfoForNormalFragment.startArticleInfoForNormal(mContext,aid.toInt(),UserInfo.user_id)
                }
                ArticleType.COLLECTION_ARTICLE -> {
                }
                ArticleType.POST_ARTICLE -> {
                }
                ArticleType.TRASH_ARTICLE->{
                    ArticleInfoForTrashFragment.startArticleInfoForTrashFragment(mContext,aid.toInt(),UserInfo.user_id)
                }
                else -> {
                }
            }
        }

        /**
         * 通过文章ID 获取文章内容
         */
        @JavascriptInterface
        fun getContentById(id:Int){
        }
    }

}