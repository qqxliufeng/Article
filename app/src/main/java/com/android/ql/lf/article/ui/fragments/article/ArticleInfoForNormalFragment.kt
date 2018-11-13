package com.android.ql.lf.article.ui.fragments.article

import android.content.Context
import android.support.v7.view.menu.MenuBuilder
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.TextView
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.ArticleItem
import com.android.ql.lf.article.data.ArticleType
import com.android.ql.lf.article.ui.fragments.mine.PersonalIndexFragment
import com.android.ql.lf.article.ui.fragments.other.ArticleWebViewFragment
import com.android.ql.lf.article.ui.fragments.share.ArticleShareDialogFragment
import com.android.ql.lf.article.ui.widgets.CommentLinearLayout
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseRecyclerViewFragment
import com.android.ql.lf.article.ui.widgets.PopupWindowDialog
import com.android.ql.lf.article.utils.*
import com.android.ql.lf.baselibaray.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_article_info_for_normal_layout.*
import org.jetbrains.anko.bundleOf
import org.json.JSONObject

class ArticleInfoForNormalFragment : BaseRecyclerViewFragment<String>() {

    val tempList = arrayListOf("", "", "","","")

    companion object {
        fun startArticleInfoForNormal(context: Context,aid:Int) {
            FragmentContainerActivity.from(context).setTitle("详情").setNeedNetWorking(true).setExtraBundle(bundleOf(Pair("aid",aid))).setClazz(ArticleInfoForNormalFragment::class.java).start()
        }
    }

    private var mCurrentArticle : ArticleItem? = null

    private val aid by lazy {
        arguments?.getInt("aid",0)
    }

    private val mHeaderView by lazy {
        View.inflate(mContext, R.layout.layout_article_info_header_view, null)
    }

    private val mHeaderWebView by lazy {
        mHeaderView.findViewById<WebView>(R.id.mWVArticleInfoForNormal)
    }

    override fun getLayoutId() = R.layout.fragment_article_info_for_normal_layout

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun createAdapter() = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_article_comment_info_item_layout, mArrayList) {
        override fun convert(helper: BaseViewHolder?, item: String?) {
            val replyContainer = helper?.getView<CommentLinearLayout>(R.id.mCLLArticleCommentInfoItemReplyContainer)
            replyContainer?.setOnSeeMore {
                ArticleWebViewFragment.startArticleWebViewFragment(mContext,"评论详情","comment-details.html",ArticleType.OTHER.type)
            }
            replyContainer?.setData(tempList)
        }
    }

    override fun initView(view: View?) {
        super.initView(view)
        setRefreshEnable(false)
        mHeaderWebView.setNormalSetting()
        mBaseAdapter.addHeaderView(mHeaderView)
        mHeaderView.findViewById<LinearLayout>(R.id.mLlArticleInfoForAuthInfo).setOnClickListener {
            PersonalIndexFragment.startPersonalIndexFragment(mContext)
        }
        mTvArticleInfoForNormalBottomComment.setOnClickListener {
            val contentView = View.inflate(mContext,R.layout.dialog_article_comment_layout,null)
            val popUpWindow = PopupWindowDialog.showReplyDialog(mContext,contentView)
        }
        mTvArticleInfoForNormalBottomActionComment.setOnClickListener {
            mRecyclerView.smoothScrollToPosition(1)
        }
        mTvArticleInfoForNormalBottomActionAdmire.setOnClickListener {
            val admireDialog = ArticleAdmireDialogFragment()
            admireDialog.show(childFragmentManager,"admire_dialog")
        }
        mTvArticleInfoForNormalBottomActionShare.setOnClickListener {
            val shareDialog = ArticleShareDialogFragment()
            shareDialog.show(childFragmentManager,"share_dialog")
        }
        mPresent.getDataByPost(0x1, getBaseParamsWithModAndAct(ARTICLE_MODULE, ARTICLE_DETAIL_ACT).addParam("aid",aid))
    }

    override fun onRequestStart(requestID: Int) {
        if (requestID == 0x1){
            getFastProgressDialog("正在加载……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        if (requestID == 0x1){
            val check = checkResultCode(result)
            if (check!=null && check.code == SUCCESS_CODE){
                mCurrentArticle = Gson().fromJson((check.obj as JSONObject).optJSONObject(RESULT_OBJECT).toString(),ArticleItem::class.java)
                mHeaderWebView.post {
                    mHeaderWebView.loadData(mCurrentArticle?.articles_content,"text/html;charset=UTF-8",null)
                    mHeaderWebView.webViewClient = object : WebViewClient(){
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            view?.resetImage()
                        }
                    }
                    mHeaderView.findViewById<TextView>(R.id.mTvArticleInfoTitle).text = mCurrentArticle?.articles_title ?: ""
                    mHeaderView.findViewById<TextView>(R.id.mTvArticleInfoForAuthInfoNickName).text = mCurrentArticle?.articles_userData?.member_nickname
                    GlideManager.loadFaceCircleImage(mContext,mCurrentArticle?.articles_userData?.member_pic,mHeaderView.findViewById(R.id.mIvArticleInfoForAuthInfoFace))
                    mTvArticleInfoForNormalBottomActionComment.text = "评论 ${mCurrentArticle?.articles_commentCount}"
                    mTvArticleInfoForNormalBottomActionLove.text = "喜欢 ${mCurrentArticle?.articles_loveCount}"
                    val focusTextView = mHeaderView.findViewById<TextView>(R.id.mCtvArticleInfoForAuthInfoFocus)
                    if (mCurrentArticle?.articles_like == 1){
                        focusTextView.text = "+ 关注"
                    }else{
                        focusTextView.text = "✓ 已关注"
                    }
                    focusTextView.doClickWithUserStatusStart("") {
                        mPresent.getDataByPost(0x1, getBaseParamsWithModAndAct(ARTICLE_MODULE,ARTICLE_LIKE_ACT).addParam("aid",aid))
                    }
                }
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        testAdd("")
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.article_normal_menu, menu)
        if (menu != null) {
            (0 until menu.size()).forEach {
                menu.getItem(it).isVisible = it == 0 || it == 1
            }
            if (menu.javaClass == MenuBuilder::class.java) {
                val method = menu.javaClass.getDeclaredMethod("setOptionalIconsVisible", Boolean::class.java)
                method.isAccessible = true
                method.invoke(menu, true)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.mMenuArticleCollection -> {
                item.setIcon(R.drawable.img_collection_icon2)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}