package com.android.ql.lf.article.ui.fragments.article

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.ql.lf.article.R
import com.android.ql.lf.article.ui.activity.ArticleEditActivity
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseRecyclerViewFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_select_type_layout.*
import org.jetbrains.anko.bundleOf

class SelectTypeFragment : BaseRecyclerViewFragment<String>() {

    companion object {
        fun startSelectTypeFragment(context: Context, title: String) {
            FragmentContainerActivity.from(context).setExtraBundle(bundleOf(Pair("title", title))).setHiddenToolBar(true).setNeedNetWorking(true).setClazz(SelectTypeFragment::class.java).start()
        }
    }

    override fun getLayoutId() = R.layout.fragment_select_type_layout

    override fun createAdapter(): BaseQuickAdapter<String, BaseViewHolder> = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_select_type_item_layout, mArrayList) {
        override fun convert(helper: BaseViewHolder?, item: String?) {
        }
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(mContext, 3)
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ColorDrawable(Color.WHITE))
        return itemDecoration
    }

    override fun initView(view: View?) {
        super.initView(view)
        (mTvSelectTypeFirstStep.parent as ConstraintLayout).setPadding(0,statusBarHeight,0,0)
        mTvSelectTypeFirstStep.setOnClickListener { finish() }
        mTvSelectTypeNextStep.setOnClickListener {
            ArticleEditActivity.startArticleEditActivity(mContext,"","",true)
        }
        mTvSelectTypeTitle.text = arguments?.getString("title", "") ?: ""
        setLoadEnable(false)
        setRefreshEnable(false)
    }

    override fun onRefresh() {
        super.onRefresh()
        testAdd("")
    }

}