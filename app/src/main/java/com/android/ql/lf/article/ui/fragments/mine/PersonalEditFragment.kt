package com.android.ql.lf.article.ui.fragments.mine

import android.app.DatePickerDialog
import android.content.Context
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import kotlinx.android.synthetic.main.fragment_personal_edit_layout.*
import java.util.*

class PersonalEditFragment : BaseNetWorkingFragment() {

    companion object {
        fun startPersonalEditFragment(context: Context) {
            FragmentContainerActivity.from(context).setNeedNetWorking(true).setTitle("编辑个人资料").setClazz(PersonalEditFragment::class.java).start()
        }
    }

    private val sexs = arrayOf("男", "女")

    override fun getLayoutId() = R.layout.fragment_personal_edit_layout

    override fun initView(view: View?) {
        mRlPersonalEditFaceContainer.setOnClickListener { }
        mRlPersonalEditNickNameContainer.setOnClickListener {
            val nickNameDialogFragment = PersonalEditNickNameDialogFragment()
            nickNameDialogFragment.myShow(childFragmentManager, "nick_name_dialog") { nickName ->
                mTvPersonalEditNickName.text = nickName
            }
        }
        mRlPersonalEditSexContainer.setOnClickListener {
            val sexDialog = AlertDialog.Builder(mContext)
            sexDialog.setTitle("请选择性别")
            sexDialog.setSingleChoiceItems(sexs, 0) { _, which ->
                mTvPersonalEditSex.text = sexs[which]
            }
            sexDialog.setNegativeButton("取消", null)
            sexDialog.setPositiveButton("确定", null)
            sexDialog.create().show()
        }
        mRlPersonalEditBirthdayContainer.setOnClickListener {
            val dataDialog = DatePickerDialog(mContext, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                mTvPersonalEditBirthday.text = "$year.$month.$dayOfMonth"
            }, Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
            dataDialog.show()
        }
        mTvPersonalEditDes.setOnClickListener {
            FragmentContainerActivity.from(mContext).setTitle("编辑个性签名").setNeedNetWorking(true).setClazz(PersonalDescriptionFragment::class.java).start()
        }
    }
}