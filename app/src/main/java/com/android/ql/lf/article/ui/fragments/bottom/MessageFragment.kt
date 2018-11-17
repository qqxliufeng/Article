package com.android.ql.lf.article.ui.fragments.bottom

import android.arch.lifecycle.Observer
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.*
import com.android.ql.lf.article.ui.fragments.community.LeaveMessageInfoFragment
import com.android.ql.lf.article.ui.fragments.other.ArticleWebViewFragment
import com.android.ql.lf.article.utils.*
import com.android.ql.lf.baselibaray.ui.fragment.BaseRecyclerViewFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MessageFragment : BaseRecyclerViewFragment<LeaveMessage>() {

    private val headerView by lazy {
        View.inflate(mContext, R.layout.top_message_bottom_layout, null)
    }

    private val mCollection by lazy {
        headerView.findViewById<TextView>(R.id.mTvBottomMessageCollection)
    }

    private val mComment by lazy {
        headerView.findViewById<TextView>(R.id.mTvBottomMessageComment)
    }

    private val mFocus by lazy {
        headerView.findViewById<TextView>(R.id.mTvBottomMessageFocus)
    }

    override fun getLayoutId() = R.layout.fragment_message_layout

    override fun createAdapter() =
        object : BaseQuickAdapter<LeaveMessage, BaseViewHolder>(R.layout.adapter_message_bottom_item_layout, mArrayList) {
            override fun convert(helper: BaseViewHolder?, item: LeaveMessage?) {
            }
        }

    override fun initView(view: View?) {
        super.initView(view)
        UserInfoLiveData.observe(this, Observer {
            if (UserInfo.isLogin()) {
                onPostRefresh()
            }
        })
        mBaseAdapter.setHeaderAndEmpty(true)
        mBaseAdapter.addHeaderView(headerView)
        mFocus.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "关注", "attention.html", ArticleType.OTHER.type)
        }
        mCollection.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "收藏", "collect.html",ArticleType.OTHER.type)
        }
        mComment.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "评论", "comment.html",ArticleType.OTHER.type)
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        if (UserInfo.isLogin()) {
            mPresent.getDataByPost(0x0, getBaseParamsWithPage(MESSAGE_MODULE, MESSAGE_ACT, currentPage))
        }
    }

    override fun onLoadMore() {
        super.onLoadMore()
        if (UserInfo.isLogin()) {
            mPresent.getDataByPost(0x0, getBaseParamsWithPage(MESSAGE_MODULE, MESSAGE_ACT, currentPage))
        }
    }

    override fun getEmptyMessage(): String {
        return "暂无留言哦~"
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ColorDrawable(Color.WHITE))
        return itemDecoration
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        if (requestID == 0x0){
            processList(result as String,LeaveMessage::class.java)
        }
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
//        startActivity(Intent(mContext,ChatActivity::class.java))
//        ArticleWebViewFragment.startArticleWebViewFragment(mContext,"留言","message.html")
        LeaveMessageInfoFragment.startLeaveMessageInfoFragment(mContext)
    }
}