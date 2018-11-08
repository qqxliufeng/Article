package com.android.ql.lf.article.ui.fragments.community

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.article.ui.widgets.PopupWindowDialog
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseRecyclerViewFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 18.11.8.
 * @author lf on 18.11.8
 */
class LeaveMessageInfoFragment : BaseRecyclerViewFragment<String>() {

    companion object {
        fun startLeaveMessageInfoFragment(context: Context){
            FragmentContainerActivity.from(context).setTitle("留言").setNeedNetWorking(true).setClazz(LeaveMessageInfoFragment::class.java).start()
        }
    }

    override fun createAdapter() =
        object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.adapter_leave_message_info_item_layout, mArrayList) {
            override fun convert(helper: BaseViewHolder?, item: String?) {
                helper?.addOnClickListener(R.id.mTvLeaveMessageItemReply)
            }
        }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ColorDrawable(Color.TRANSPARENT))
        return itemDecoration
    }

    override fun onRefresh() {
        super.onRefresh()
        testAdd("")
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemChildClick(adapter, view, position)
        if (view!!.id == R.id.mTvLeaveMessageItemReply){
            val contentView = View.inflate(mContext,R.layout.dialog_article_comment_layout,null)
            PopupWindowDialog.showReplyDialog(mContext,contentView)
        }
    }
}