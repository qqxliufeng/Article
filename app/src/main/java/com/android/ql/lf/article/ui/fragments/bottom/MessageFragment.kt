package com.android.ql.lf.article.ui.fragments.bottom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.android.ql.lf.article.R
import com.android.ql.lf.article.ui.fragments.other.ArticleWebViewFragment
import com.android.ql.lf.baselibaray.ui.fragment.BaseRecyclerViewFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class MessageFragment : BaseRecyclerViewFragment<String>() {

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

    override fun createAdapter() = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_message_bottom_item_layout, mArrayList) {
        override fun convert(helper: BaseViewHolder?, item: String?) {

        }
    }

    override fun initView(view: View?) {
        super.initView(view)
        mBaseAdapter.addHeaderView(headerView)
        mFocus.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext,"关注","attention.html")
        }
        mCollection.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "收藏", "collect.html")
        }
        mComment.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext,"评论","comment.html")
        }
    }


    override fun onRefresh() {
        super.onRefresh()
        testAdd("")
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ColorDrawable(Color.WHITE))
        return itemDecoration
    }


}