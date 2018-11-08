package com.android.ql.lf.article.ui.fragments.login

import android.app.Activity
import android.content.Intent
import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.article.utils.isEmpty
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import com.android.ql.lf.baselibaray.utils.GlideManager
import com.android.ql.lf.baselibaray.utils.compressAndSaveCacheFace
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.fragment_login_second_step_layout.*
import kotlinx.android.synthetic.main.layout_pre_step.*
import org.jetbrains.anko.support.v4.toast
import top.zibin.luban.OnCompressListener
import java.io.File

class LoginSecondStepFragment : BaseNetWorkingFragment() {

    private var facePath = ""

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
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            compressAndSaveCacheFace(Matisse.obtainPathResult(data)[0], object : OnCompressListener {
                override fun onSuccess(file: File?) {
                    facePath = file?.absolutePath ?: ""
                    GlideManager.loadLocalFaceCircleImage(mContext, file?.absolutePath, mIvLoginUserFace)
                }

                override fun onError(e: Throwable?) {
                }

                override fun onStart() {
                }
            })
        }
    }
}