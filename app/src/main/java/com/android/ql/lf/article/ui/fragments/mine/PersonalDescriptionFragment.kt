package com.android.ql.lf.article.ui.fragments.mine

import android.content.Context
import android.support.v4.view.MenuItemCompat
import android.view.*
import com.android.ql.lf.article.R
import com.android.ql.lf.article.ui.widgets.SingleTextViewActionProvide
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import kotlinx.android.synthetic.main.fragment_personal_description_layout.*

class PersonalDescriptionFragment : BaseNetWorkingFragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId() = R.layout.fragment_personal_description_layout

    override fun initView(view: View?) {
        mEtPersonalDescriptionContent.isLongClickable = false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.submit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        val submitMenu = menu?.findItem(R.id.mMenuSubmit)
        val provider = MenuItemCompat.getActionProvider(submitMenu) as SingleTextViewActionProvide
        provider.setOnActionClick {
            finish()
        }
        provider.setTitle("确定")
        super.onPrepareOptionsMenu(menu)
    }
}
