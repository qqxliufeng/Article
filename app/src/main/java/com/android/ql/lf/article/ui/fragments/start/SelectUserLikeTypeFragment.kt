package com.android.ql.lf.article.ui.fragments.start

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.article.ui.activity.MainActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseRecyclerViewFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_select_user_like_type_layout.*
import kotlinx.android.synthetic.main.layout_pre_step.*
import org.jetbrains.anko.support.v4.toast

class SelectUserLikeTypeFragment : BaseRecyclerViewFragment<String>() {

    override fun createAdapter(): BaseQuickAdapter<String, BaseViewHolder> =
        object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_select_type_item_layout, mArrayList) {
            override fun convert(helper: BaseViewHolder?, item: String?) {
            }
        }

    override fun getLayoutId() = R.layout.fragment_select_user_like_type_layout


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
        setLoadEnable(false)
        setRefreshEnable(false)
        mTvPreFirstStep.setOnClickListener {
            (parentFragment as StartCustomTypeFragment).positionFragment(0)
        }
        mTvSelectTypeNextStep.setOnClickListener {
            toast("正在为您生成主页")
            mTvSelectTypeNextStep.postDelayed({
                startActivity(Intent(mContext, MainActivity::class.java))
                finish()
            },1500)
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        testAdd("")
    }

}