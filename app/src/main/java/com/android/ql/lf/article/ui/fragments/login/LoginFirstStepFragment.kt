package com.android.ql.lf.article.ui.fragments.login

import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.article.utils.getTextString
import com.android.ql.lf.article.utils.isEmpty
import com.android.ql.lf.article.utils.isNotPhone
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import kotlinx.android.synthetic.main.fragment_login_first_step_layout.*
import kotlinx.android.synthetic.main.layout_pre_step.*
import org.jetbrains.anko.support.v4.toast

class LoginFirstStepFragment : BaseNetWorkingFragment() {

    private var mCode:String = ""

    override fun getLayoutId() = R.layout.fragment_login_first_step_layout

    override fun initView(view: View?) {
        mTvPreFirstStep.setOnClickListener {
            (parentFragment as LoginFragment).positionFragment(-1)
        }
        mTvLoginUserVerCode.setOnClickListener {
            if (mEtLoginUserPhone.isEmpty()){
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (mEtLoginUserPhone.isNotPhone()){
                toast("请输入合法的手机号")
                return@setOnClickListener
            }
            mTvLoginUserVerCode.start()
        }
        mBtFirstStepLogin.setOnClickListener {
            if (mEtLoginUserPhone.isEmpty()){
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (mEtLoginUserPhone.isNotPhone()){
                toast("请输入合法的手机号")
                return@setOnClickListener
            }
            if (mEtLoginUserVerCode.isEmpty()){
                toast("请输入验证码")
                return@setOnClickListener
            }
            (parentFragment as LoginFragment).positionFragment(1)
//            if (mCode != mEtLoginUserVerCode.getTextString()){
//                toast("请输入正确的验证码")
//                return@setOnClickListener
//            }
        }
    }

    override fun onDestroyView() {
        mTvLoginUserVerCode.cancel()
        super.onDestroyView()
    }
}