package com.android.ql.lf.article.ui.fragments.login

import android.app.Activity
import android.content.Intent
import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.UserInfo
import com.android.ql.lf.article.data.jsonToUserInfo
import com.android.ql.lf.article.data.postUserInfo
import com.android.ql.lf.article.utils.*
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import com.android.ql.lf.baselibaray.utils.BaseConfig
import com.android.ql.lf.baselibaray.utils.PreferenceUtils
import com.android.ql.lf.baselibaray.utils.RxBus
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.fragment_login_first_step_layout.*
import kotlinx.android.synthetic.main.layout_pre_step.*
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import org.json.JSONObject

class LoginForSMSCodeFragment : BaseNetWorkingFragment() {

    private var mCode: Int = 0

    override fun getLayoutId() = R.layout.fragment_login_first_step_layout

    private val iwxapi by lazy {
        WXAPIFactory.createWXAPI(mContext,BaseConfig.WX_APP_ID,true)
    }

    private val mSsoHandler by lazy {
        SsoHandler(mContext as Activity)
    }


    private val wxAuthSubscription by lazy {
        RxBus.getDefault().toObservable(BaseResp::class.java).subscribe {
            onWeiXinAuthSuccess((it as SendAuth.Resp).code)
        }
    }

    override fun initView(view: View?) {
        wxAuthSubscription
        mTvPreFirstStep.setOnClickListener {
            (parentFragment as LoginFragment).positionFragment(-1)
        }
        mTvLoginUserVerCode.setOnClickListener {
            if (mEtLoginUserPhone.isEmpty()) {
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (mEtLoginUserPhone.isNotPhone()) {
                toast("请输入合法的手机号")
                return@setOnClickListener
            }
            mPresent.getDataByPost(
                0x0,
                getBaseParamsWithModAndAct(LOGIN_MODULE, SMSCODE_ACT).addParam(
                    "phone",
                    mEtLoginUserPhone.getTextString()
                )
            )
            mTvLoginUserVerCode.start()
        }
        mBtFirstStepLogin.setOnClickListener {
            if (mEtLoginUserPhone.isEmpty()) {
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (mEtLoginUserPhone.isNotPhone()) {
                toast("请输入合法的手机号")
                return@setOnClickListener
            }
            if (mEtLoginUserVerCode.isEmpty()) {
                toast("请输入验证码")
                return@setOnClickListener
            }
            if ("$mCode" != mEtLoginUserVerCode.getTextString()) {
                toast("请输入正确的验证码")
                return@setOnClickListener
            }
            mPresent.getDataByPost(
                0x1,
                getBaseParamsWithModAndAct(LOGIN_MODULE, LOGINDO_ACT).addParam(
                    "phone",
                    mEtLoginUserPhone.getTextString()
                ).addParam("code", mEtLoginUserVerCode.getTextString())
            )
        }
        mLlLoginFirstStepForPassword.setOnClickListener {
            (parentFragment as LoginFragment).positionFragment(2)
        }
        mLlLoginFirstStepForQQ.setOnClickListener {

        }
        mLlLoginFirstStepForWX.setOnClickListener {
            iwxapi.registerApp(BaseConfig.WX_APP_ID)
            val req = SendAuth.Req()
            req.scope = "snsapi_userinfo"
            req.state = "wechat_sdk_ql_bs"
            iwxapi.sendReq(req)
        }
        mLlLoginFirstStepForWB.setOnClickListener {
            mSsoHandler.authorize(object : WbAuthListener {
                override fun onSuccess(p0: Oauth2AccessToken?) {
                    toast("绑定成功")
                }

                override fun onFailure(p0: WbConnectErrorMessage?) {
                    toast("绑定失败")
                }

                override fun cancel() {
                    toast("绑定取消")
                }
            })
        }
    }

    override fun onMyActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onMyActivityResult(requestCode, resultCode, data)
        mSsoHandler.authorizeCallBack(requestCode, resultCode, data)
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x0) {
            getFastProgressDialog("正在获取验证码……")
        } else if (requestID == 0x1 || requestID == 0x2) {
            getFastProgressDialog("正在登录……")
        }
    }

    private fun onWeiXinAuthSuccess(code:String){
        mPresent.getDataByPost(0x2, getBaseParamsWithModAndAct(T_MODULE,WX_LOGIN_ACT)
            .addParam("code",code))
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        when (requestID) {
            0x0 -> {
                val json = JSONObject(result as String)
                json.optInt("status").let {
                    if (it == 200) {
                        toast("验证码发送成功，请注意查收")
                        mCode = json.optInt("code")
                    }
                }
            }
            0x1 -> try {
                val check = checkResultCode(result)
                if (check != null) {
                    if (check.code == SUCCESS_CODE) {
                        val jsonObject = (check.obj as JSONObject).optJSONObject(RESULT_OBJECT)
                        if (UserInfo.jsonToUserInfo(jsonObject)) {
                            UserInfo.postUserInfo()
                            finish()
                        }else{
                            toast("登录失败")
                        }
                    } else if (check.code == "400") { // 第一次登录，需完善头像和昵称
                        PreferenceUtils.setPrefString(mContext,"user_phone",mEtLoginUserPhone.getTextString())
                        PreferenceUtils.setPrefString(mContext,"user_code",mEtLoginUserVerCode.getTextString())
                        (parentFragment as LoginFragment).positionFragment(1)
                    }
                } else {
                    toast("登录失败")
                }
            } catch (e: Exception) {
                toast("登录失败")
            }
            0x2 -> {
                val check = checkResultCode(result)
                if (check!=null){
                    when {
                        check.code == SUCCESS_CODE -> {

                        }
                        check.code == "202" -> { //绑定手机号
                            toast("请完善手机号")
                        }
                        else -> {
                            toast("登录失败……")
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        mTvLoginUserVerCode.cancel()
        unsubscribe(wxAuthSubscription)
        super.onDestroyView()
    }
}