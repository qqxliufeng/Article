package com.android.ql.lf.article.ui.fragments.login

import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import kotlinx.android.synthetic.main.fragment_login_second_step_layout.*
import kotlinx.android.synthetic.main.layout_pre_step.*

class LoginSecondStepFragment : BaseNetWorkingFragment(){

    override fun getLayoutId() = R.layout.fragment_login_second_step_layout

    override fun initView(view: View?) {
        mTvPreFirstStep.setOnClickListener {
            (parentFragment as LoginFragment).positionFragment(0)
        }
    }
}