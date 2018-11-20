package com.android.ql.lf.article.ui.fragments.article

import android.content.Context
import android.support.v7.view.menu.MenuBuilder
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.CheckedTextView
import android.widget.TextView
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.ArticleCommentItem
import com.android.ql.lf.article.data.ArticleItem
import com.android.ql.lf.article.data.UserInfo
import com.android.ql.lf.article.ui.activity.ArticleEditActivity
import com.android.ql.lf.article.ui.fragments.other.NetWebViewFragment
import com.android.ql.lf.article.utils.*
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import com.android.ql.lf.baselibaray.utils.GlideManager
import com.android.ql.lf.baselibaray.utils.RxBus
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_article_info_for_display_layout.*
import kotlinx.android.synthetic.main.layout_article_info_auth_top_view.*
import org.jetbrains.anko.bundleOf
import org.json.JSONObject

class ArticleInfoDisplayFragment : BaseNetWorkingFragment() {

    companion object {
        fun startArticleInfoDisplayFragment(context: Context, aid: Int, auid: Int = 0) {
            FragmentContainerActivity.from(context).setTitle("文章详情").setNeedNetWorking(true)
                .setExtraBundle(
                    bundleOf(
                        Pair("aid", aid),
                        Pair("auid", auid)
                    )
                ).setClazz(ArticleInfoDisplayFragment::class.java).start()
        }
    }

    private var mCurrentArticle: ArticleItem? = null

    private val aid by lazy {
        arguments?.getInt("aid", 0)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    private val updateArticleSubscription by lazy {
        RxBus.getDefault().toObservable(String::class.java).subscribe {
            if (it == ArticleInfoForNormalFragment.UPDATE_ARTICLE_FLAG){
                RxBus.getDefault().post(mCurrentArticle)
                mPresent.getDataByPost(0x1, getBaseParamsWithModAndAct(ARTICLE_MODULE, ARTICLE_DETAIL_ACT).addParam("aid", aid))
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_article_info_for_display_layout

    override fun initView(view: View?) {
        updateArticleSubscription
        mCtvArticleInfoForAuthInfoFocus.visibility = View.GONE
        mPresent.getDataByPost(0x1, getBaseParamsWithModAndAct(ARTICLE_MODULE, ARTICLE_DETAIL_ACT).addParam("aid", aid))
    }

    override fun onRequestStart(requestID: Int) {
        when (requestID) {
            0x1 -> getFastProgressDialog("正在加载……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        when (requestID) {
            0x1 -> {
                val check = checkResultCode(result)
                if (check != null && check.code == SUCCESS_CODE) {
                    mCurrentArticle = Gson().fromJson(
                        (check.obj as JSONObject).optJSONObject(RESULT_OBJECT).toString(),
                        ArticleItem::class.java
                    )
                    mHeaderWebView.post {
                        mHeaderWebView.loadData(mCurrentArticle?.articles_content, "text/html;charset=UTF-8", null)
                        mHeaderWebView.webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                view?.resetImage()
                            }

                            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                                NetWebViewFragment.startNetWebViewFragment(mContext, url ?: "")
                                return true
                            }
                        }
                        mTvArticleInfoTitle.text = mCurrentArticle?.articles_title ?: ""
                        mTvArticleInfoForAuthInfoNickName.text = mCurrentArticle?.articles_userData?.member_nickname
                        mTvArticleInfoType.text = mCurrentArticle?.articles_tags ?: ""
                        mTvArticleInfoDes.text = "${mCurrentArticle?.articles_times} . 字数${mCurrentArticle?.articles_numcount} . 阅读${mCurrentArticle?.articles_read}"
                        GlideManager.loadFaceCircleImage(
                            mContext,
                            mCurrentArticle?.articles_userData?.member_pic,
                            mIvArticleInfoForAuthInfoFace)
                        mLlArticleInfoForDisplayBottomEdit.setOnClickListener {
                            ArticleEditActivity.startArticleEditActivity(mContext,
                                mCurrentArticle?.articles_title?:"",
                                mCurrentArticle?.articles_content?:"",
                                Classify(0,"",false,""),
                                1,
                                mCurrentArticle?.articles_id ?: 0)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.article_display_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.mMenuArticlePublic->{
                alert("是否要公开发布？","是","否"){_,_->

                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        unsubscribe(updateArticleSubscription)
        super.onDestroyView()
    }

}