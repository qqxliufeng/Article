package com.android.ql.lf.article.ui.activity

import android.content.Intent
import com.android.ql.lf.article.R
import com.android.ql.lf.article.ui.fragments.mine.AccountSafeFragment
import com.android.ql.lf.baselibaray.ui.activity.BaseActivity
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler
import org.jetbrains.anko.toast

/**
 * Created by lf on 18.11.21.
 * @author lf on 18.11.21
 */
class AuthActivity : BaseActivity() {

    private var mSsoHandler: SsoHandler? = null

    override fun getLayoutId() = R.layout.activity_auth_layout

    override fun initView() {
        supportFragmentManager.beginTransaction().replace(R.id.mFlAuthContainer, AccountSafeFragment()).commit()
    }

    fun weiboAuth() {
        mSsoHandler = SsoHandler(this)
        mSsoHandler?.authorize(object : WbAuthListener {
            override fun onSuccess(p0: Oauth2AccessToken?) {
                toast("授权成功")
            }

            override fun onFailure(p0: WbConnectErrorMessage?) {
                toast("授权失败")
            }

            override fun cancel() {
                toast("授权取消")
            }
        })
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity.onActivityResult}
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        mSsoHandler?.authorizeCallBack(requestCode, resultCode, data)
    }
}