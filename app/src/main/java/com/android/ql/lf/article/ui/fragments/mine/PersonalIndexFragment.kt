package com.android.ql.lf.article.ui.fragments.mine

import android.content.Context
import android.graphics.Color
import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.ArticleItem
import com.android.ql.lf.article.ui.adapters.ArticleListAdapter
import com.android.ql.lf.article.ui.fragments.article.ArticleAdmireDialogFragment
import com.android.ql.lf.article.ui.widgets.PopupWindowDialog
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseRecyclerViewFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_personal_index_layout.*

class PersonalIndexFragment : BaseRecyclerViewFragment<ArticleItem>() {

    companion object {
        fun startPersonalIndexFragment(context: Context) {
            FragmentContainerActivity.from(context).setClazz(PersonalIndexFragment::class.java).setNeedNetWorking(true).setHiddenToolBar(true).start()
        }
    }

    override fun createAdapter() = ArticleListAdapter(mArrayList)

    override fun getLayoutId() = R.layout.fragment_personal_index_layout

    override fun initView(view: View?) {
        (mContext as FragmentContainerActivity).statusBarColor = Color.TRANSPARENT
        super.initView(view)
        setLoadEnable(false)
        mIvBack.setImageResource(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material)
        mIvBack.setColorFilter(Color.WHITE)
        mAlPersonalIndex.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val alpha = (Math.abs(verticalOffset / appBarLayout.totalScrollRange.toFloat()) * 255).toInt()
            val bgColor = Color.argb(alpha, 255, 255, 255)
            mTlPersonalIndexBar.setBackgroundColor(bgColor)
            when {
                verticalOffset == 0 -> {
                    mIvBack.setColorFilter(Color.WHITE)
                    mIvShare.setColorFilter(Color.WHITE)
                    mCTLPersonalIndex.title = ""
                }
                Math.abs(verticalOffset) == appBarLayout.totalScrollRange -> {
                    mIvBack.setColorFilter(Color.BLACK)
                    mIvShare.setColorFilter(Color.BLACK)
                    mCTLPersonalIndex.title = "NN~简单"
                }
                else -> {
                    if (!mCTLPersonalIndex.title!!.isEmpty()) {
                        mCTLPersonalIndex.title = ""
                    }
                }
            }
        }
        mIvBack.setOnClickListener { finish() }
        mLlPersonalIndexLeaveMessage.setOnClickListener {
            val contentView = View.inflate(mContext,R.layout.dialog_article_comment_layout,null)
            val popUpWindow = PopupWindowDialog.showReplyDialog(mContext,contentView)
        }
        mTvPersonalIndexPraise.setOnClickListener {
            val admireDialog = ArticleAdmireDialogFragment()
            admireDialog.show(childFragmentManager,"admire_dialog")
        }
        mLlPersonalIndexFocus.setOnClickListener {  }
    }

    override fun onRefresh() {
        (0 .. 10).forEach {
            val articleItem = ArticleItem()
            articleItem.mType = if( it % 2 == 0) ArticleListAdapter.MULTI_IMAGE_TYPE else ArticleListAdapter.SINGLE_IMAGE_TYPE
            mArrayList.add(articleItem)
        }
        mBaseAdapter.notifyDataSetChanged()
        onRequestEnd(-1)
    }


}