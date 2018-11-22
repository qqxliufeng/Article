package com.android.ql.lf.article.ui.fragments.mine

import android.text.TextUtils
import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.UserInfo
import com.android.ql.lf.article.ui.activity.AuthActivity
import com.android.ql.lf.article.utils.ACCOUNT_SAFE_ACT
import com.android.ql.lf.article.utils.MEMBER_MODULE
import com.android.ql.lf.article.utils.getBaseParamsWithModAndAct
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import kotlinx.android.synthetic.main.fragment_account_safe_layout.*
import org.jetbrains.anko.support.v4.toast


/**
 * Created by lf on 18.11.21.
 * @author lf on 18.11.21
 */
class AccountSafeFragment : BaseNetWorkingFragment() {

    override fun getLayoutId() = R.layout.fragment_account_safe_layout

    private var type: Int = 1

    override fun initView(view: View?) {
        mTvAccountSafePhone.text = UserInfo.user_phone
        if (TextUtils.isEmpty(UserInfo.user_wx)) {
            mIvAccountSafeWX.setImageResource(R.drawable.img_wx_unselect_icon)
            mTvAccountSafeWX.text = "未绑定"
            mLlAccountSafeWX.setOnClickListener {
            }
        } else {
            mIvAccountSafeWX.setImageResource(R.drawable.img_wx_select_icon)
            mTvAccountSafeWX.text = "已绑定"
        }
        if (TextUtils.isEmpty(UserInfo.user_qq)) {
            mIvAccountSafeQQ.setImageResource(R.drawable.img_qq_unselect_icon)
            mTvAccountSafeQQ.text = "未绑定"
            mLlAccountSafeQQ.setOnClickListener {}
        } else {
            mIvAccountSafeQQ.setImageResource(R.drawable.img_qq_select_icon)
            mTvAccountSafeQQ.text = "已绑定"
        }
        if (TextUtils.isEmpty(UserInfo.user_weibo)) {
            mIvAccountSafeWB.setImageResource(R.drawable.img_wb_unselect_icon)
            mTvAccountSafeWB.text = "未绑定"
            mLlAccountSafeWB.setOnClickListener {
                (mContext as AuthActivity).weiboAuth()
            }
        } else {
            mIvAccountSafeWB.setImageResource(R.drawable.img_wb_select_icon)
            mTvAccountSafeWB.text = "已绑定"
        }
    }

    fun onWeiBoAuthSuccess(token: Oauth2AccessToken) {
        type = 3
        mPresent.getDataByPost(
            0x0, getBaseParamsWithModAndAct(MEMBER_MODULE, ACCOUNT_SAFE_ACT)
                .addParam("type", "3")
                .addParam("weibo", token.token)
        )
    }

    fun onWeiXinAuthSuccess(token: String) {
        type = 2
        mPresent.getDataByPost(
            0x0, getBaseParamsWithModAndAct(MEMBER_MODULE, ACCOUNT_SAFE_ACT)
                .addParam("type", "2")
                .addParam("wx", token)
        )
    }

    fun onQQAuthSuccess(token: String) {
        type = 1
        mPresent.getDataByPost(
            0x0, getBaseParamsWithModAndAct(MEMBER_MODULE, ACCOUNT_SAFE_ACT)
                .addParam("type", "1")
                .addParam("qq", token)
        )
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x0) {
            getFastProgressDialog("正在授权……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        if (requestID == 0x0) {
            val check = checkResultCode(result)
            if (check != null) {
                if (check.code == SUCCESS_CODE) {
                    toast("授权成功")
                    when (type) {
                        1 -> {
                            mIvAccountSafeQQ.setImageResource(R.drawable.img_qq_select_icon)
                            mTvAccountSafeQQ.text = "已绑定"
                        }
                        2 -> {
                            mIvAccountSafeWX.setImageResource(R.drawable.img_wx_select_icon)
                            mTvAccountSafeWX.text = "已绑定"
                        }
                        3 -> {
                            mIvAccountSafeWB.setImageResource(R.drawable.img_wb_select_icon)
                            mTvAccountSafeWB.text = "已绑定"
                        }
                    }
                } else {
                    authFail()
                }
            } else {
                authFail()
            }
        }
    }

    private fun authFail() {
        toast("绑定失败")
        when (type) {
            1 -> {
                mIvAccountSafeQQ.setImageResource(R.drawable.img_qq_unselect_icon)
                mTvAccountSafeQQ.text = "未绑定"
            }
            2 -> {
                mIvAccountSafeWX.setImageResource(R.drawable.img_wx_unselect_icon)
                mTvAccountSafeWX.text = "未绑定"
            }
            3 -> {
                mIvAccountSafeWB.setImageResource(R.drawable.img_wb_unselect_icon)
                mTvAccountSafeWB.text = "未绑定"
            }
        }
    }

}