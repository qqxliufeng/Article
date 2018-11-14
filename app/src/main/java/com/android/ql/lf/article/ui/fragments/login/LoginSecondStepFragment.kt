package com.android.ql.lf.article.ui.fragments.login

import android.app.Activity
import android.content.Intent
import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.UserInfo
import com.android.ql.lf.article.data.jsonToUserInfo
import com.android.ql.lf.article.data.postUserInfo
import com.android.ql.lf.article.ui.activity.MainActivity
import com.android.ql.lf.article.utils.*
import com.android.ql.lf.baselibaray.data.ImageBean
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import com.android.ql.lf.baselibaray.utils.GlideManager
import com.android.ql.lf.baselibaray.utils.ImageUploadHelper
import com.android.ql.lf.baselibaray.utils.PreferenceUtils
import com.android.ql.lf.baselibaray.utils.compressAndSaveCacheFace
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.fragment_login_first_step_layout.*
import kotlinx.android.synthetic.main.fragment_login_second_step_layout.*
import kotlinx.android.synthetic.main.layout_pre_step.*
import okhttp3.MultipartBody
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import top.zibin.luban.OnCompressListener
import java.io.File

class LoginSecondStepFragment : BaseNetWorkingFragment() {

    private var facePath = ""

    private val sex by lazy {
        PreferenceUtils.getPrefString(mContext, "sex", "")
    }

    private val birthday by lazy {
        PreferenceUtils.getPrefString(mContext, "birthday", "")
    }

    private val phone by lazy {
        PreferenceUtils.getPrefString(mContext, "user_phone", "")
    }

    private val code by lazy {
        PreferenceUtils.getPrefString(mContext, "user_code", "")
    }

    override fun getLayoutId() = R.layout.fragment_login_second_step_layout

    override fun initView(view: View?) {
        mTvPreFirstStep.setOnClickListener {
            (parentFragment as LoginFragment).positionFragment(0)
        }
        mIvLoginUserFace.setOnClickListener {
            openImageChoose(MimeType.ofImage(), 1)
        }
        mBtComplement.setOnClickListener {
            if (facePath == "") {
                toast("请选择头像")
                return@setOnClickListener
            }
            if (mEtLoginUserNickName.isEmpty()) {
                toast("请输入昵称")
                return@setOnClickListener
            }
            mPresent.getDataByPost(
                0x1, getBaseParamsWithModAndAct(LOGIN_MODULE, REGISTERDO_ACT)
                    .addParam("phone", phone)
                    .addParam("code", code)
                    .addParam("sex", sex)
                    .addParam("birthday", birthday)
                    .addParam("nickname", mEtLoginUserNickName.getTextString())
                    .addParam("address", "")
                    .addParam("classify", "")
                    .addParam("pic", facePath)
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            compressAndSaveCacheFace(Matisse.obtainPathResult(data)[0], object : OnCompressListener {
                override fun onSuccess(file: File?) {
                    ImageUploadHelper(object : ImageUploadHelper.OnImageUploadListener {
                        override fun onActionStart() {
                            getFastProgressDialog("正在上传头像……")
                        }

                        override fun onActionEnd(builder: MultipartBody.Builder?) {
                            mPresent.uploadFile(0x0, T_MODULE, UPLOADING_PIC_ACT, builder?.build()?.parts())
                        }

                        override fun onActionFailed() {
                        }

                    }).upload(arrayListOf(ImageBean(null, file?.absolutePath ?: "")))
                }

                override fun onError(e: Throwable?) {
                }

                override fun onStart() {
                }
            })
        }
    }

    override fun onRequestStart(requestID: Int) {
        if (requestID == 0x1){
            getFastProgressDialog("正在注册……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        if (requestID == 0x0) {
            val json = JSONObject(result as String)
            val code = json.optInt("code")
            if (code == 200) {
                facePath = json.optString("result")
                GlideManager.loadFaceCircleImage(mContext, facePath, mIvLoginUserFace)
            } else {
                toast("头像上传失败")
            }
        } else if (requestID == 0x1) {
            val check = checkResultCode(result)
            if (check != null) {
                if (check.code == SUCCESS_CODE) {
                    val jsonObject = (check.obj as JSONObject).optJSONObject(RESULT_OBJECT)
                    if (UserInfo.jsonToUserInfo(jsonObject)) {
                        toast("注册成功")
                        UserInfo.postUserInfo()
                        startActivity(Intent(mContext, MainActivity::class.java))
                        finish()
                    } else {
                        toast("注册失败")
                    }
                } else {
                    toast((check.obj as JSONObject).optString("msg"))
                }
            }
        }
    }
}