package com.android.ql.lf.article.ui.fragments.article

import android.content.Context
import android.support.v7.view.menu.MenuBuilder
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.widget.LinearLayout
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.ArticleType
import com.android.ql.lf.article.ui.fragments.mine.PersonalIndexFragment
import com.android.ql.lf.article.ui.fragments.other.ArticleWebViewFragment
import com.android.ql.lf.article.ui.fragments.share.ArticleShareDialogFragment
import com.android.ql.lf.article.ui.widgets.CommentLinearLayout
import com.android.ql.lf.article.utils.loadLocalHtml
import com.android.ql.lf.article.utils.setNormalSetting
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseRecyclerViewFragment
import com.android.ql.lf.article.ui.widgets.PopupWindowDialog
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_article_info_for_normal_layout.*
import org.jetbrains.anko.support.v4.toast

class ArticleInfoForNormalFragment : BaseRecyclerViewFragment<String>() {

    val tempList = arrayListOf("", "", "","","")

    companion object {
        fun startArticleInfoForNormal(context: Context) {
            FragmentContainerActivity.from(context).setTitle("详情").setNeedNetWorking(true).setClazz(ArticleInfoForNormalFragment::class.java).start()
        }
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
        mHeaderWebView.setNormalSetting()
        mHeaderWebView.loadLocalHtml("article-info.html")
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
    }

    override fun onRefresh() {
        super.onRefresh()
        testAdd("")
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.article_normal_menu, menu)
        if (menu != null) {
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