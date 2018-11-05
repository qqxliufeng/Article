package com.android.ql.lf.article.ui.fragments.start

import android.app.DatePickerDialog
import android.content.Intent
import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.article.ui.activity.MainActivity
import com.android.ql.lf.article.utils.alert
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import kotlinx.android.synthetic.main.fragment_select_user_info_layout.*
import java.util.*

class SelectUserInfoFragment : BaseNetWorkingFragment() {

    override fun getLayoutId() = R.layout.fragment_select_user_info_layout

    override fun initView(view: View?) {
        mTvSelectUserInfoJump.setOnClickListener {
            alert("是否要跳过？","跳过","不跳过"){_,_->
                startActivity(Intent(mContext,MainActivity::class.java))
                finish()
            }
        }
        mTvSelectUserInfoAge.text = "${Calendar.getInstance().get(Calendar.YEAR)}-${Calendar.getInstance().get(Calendar.MONTH)}-${Calendar.getInstance().get(Calendar.DAY_OF_MONTH)}"
        mTvSelectUserInfoAge.setOnClickListener {
            val datePickDialog = DatePickerDialog(mContext, { _, year, month, day ->
                mTvSelectUserInfoAge.text = "$year-$month-$day"
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
            datePickDialog.show()
        }
        mBtSelectUserInfoNextStep.setOnClickListener {
            (parentFragment as StartCustomTypeFragment).positionFragment(1)
        }
    }
}