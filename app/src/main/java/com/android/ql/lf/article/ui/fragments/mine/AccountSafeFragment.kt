package com.android.ql.lf.article.ui.fragments.mine

import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.UserInfo
import com.android.ql.lf.article.ui.activity.AuthActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import com.sina.weibo.sdk.auth.sso.SsoHandler
import kotlinx.android.synthetic.main.fragment_account_safe_layout.*


/**
 * Created by lf on 18.11.21.
 * @author lf on 18.11.21
 */
class AccountSafeFragment : BaseNetWorkingFragment(){

    override fun getLayoutId() = R.layout.fragment_account_safe_layout

    override fun initView(view: View?) {
        mTvAccountSafePhone.text = UserInfo.user_phone
        mLlAccountSafeWX.setOnClickListener {

        }
        mLlAccountSafeWB.setOnClickListener {
            (mContext as AuthActivity).weiboAuth()
        }
        mLlAccountSafeQQ.setOnClickListener {  }
    }
}