package com.android.ql.lf.article.ui.fragments.article

import android.view.View
import com.android.ql.lf.article.data.ArticleItem
import com.android.ql.lf.article.ui.adapters.ArticleListAdapter
import com.android.ql.lf.article.ui.fragments.other.ArticleWebViewFragment
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseRecyclerViewFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ArticleListItemFragment : BaseRecyclerViewFragment<ArticleItem>() {

    override fun createAdapter(): BaseQuickAdapter<ArticleItem, BaseViewHolder> = ArticleListAdapter(mArrayList)

    override fun initView(view: View?) {
        super.initView(view)
//        setLoadEnable(false)
    }

    override fun onRefresh() {
        (0 .. 10).forEach {
            val articleItem = ArticleItem()
            articleItem.mType = if( it % 2 == 0) ArticleListAdapter.MULTI_IMAGE_TYPE else ArticleListAdapter.SINGLE_IMAGE_TYPE
            mArrayList.add(articleItem)
        }
        onRequestEnd(-1)
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
//        FragmentContainerActivity.from(mContext).setClazz(ArticleWebViewFragment::class.java).setTitle("test").setNeedNetWorking(true).start()
        ArticleInfoForNormalFragment.startArticleInfoForNormal(mContext)
    }
}