package com.android.ql.lf.article.ui.fragments.article

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.webkit.JavascriptInterface
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.UserInfo
import com.android.ql.lf.article.ui.activity.ArticleEditActivity
import com.android.ql.lf.article.ui.activity.WebViewContainerActivity
import com.android.ql.lf.article.utils.*
import com.android.ql.lf.baselibaray.data.ImageBean
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import com.android.ql.lf.baselibaray.utils.GlideManager
import com.android.ql.lf.baselibaray.utils.ImageUploadHelper
import com.android.ql.lf.baselibaray.utils.PreferenceUtils
import com.android.ql.lf.baselibaray.utils.compressAndSaveCacheFace
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import jp.wasabeef.richeditor.RichEditor
import kotlinx.android.synthetic.main.fragment_article_edit_layout.*
import kotlinx.android.synthetic.main.fragment_login_second_step_layout.*
import okhttp3.MultipartBody
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import org.jsoup.Jsoup
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

class ArticleEditFragment : BaseNetWorkingFragment() {

    companion object {
        const val EDITOR_HTML_FLAG = "editor_html"
    }

    private var isBoldChecked: Boolean = false

    private val selectedImages = arrayListOf<SelectedImageBean>()

    private var selectedImageBean : SelectedImageBean? = null

    private var articleCount:Int = 0

    private val classify by lazy {
        arguments?.classLoader = this.javaClass.classLoader
        arguments?.getParcelable<Classify>("types")
    }

    override fun getLayoutId() = R.layout.fragment_article_edit_layout

    override fun initView(view: View?) {
        mEtArticleEditTitle.setText(arguments?.getString("title","")?:"")
        mReArticleEdit.setPlaceholder("请输入内容")
        mReArticleEdit.setPadding(0, 10, 0, 10)
        mReArticleEdit.setTextColor(ContextCompat.getColor(mContext, R.color.normalTextColor))
        mReArticleEdit.setOnInitialLoadListener {
            if (arguments?.getBoolean("is_edit", true)!!){
                mReArticleEdit.focusEditor()
                mEtArticleEditTitle.isEnabled = true
                mIBArticleEditActionImage.isEnabled = true
                mIBArticleEditActionBold.isEnabled = true
                mIBArticleEditActionLink.isEnabled = true
                mReArticleEdit.setInputEnabled(true)
            }else{
                mEtArticleEditTitle.isEnabled = false
                mIBArticleEditActionImage.isEnabled = false
                mIBArticleEditActionBold.isEnabled = false
                mIBArticleEditActionLink.isEnabled = false
                mReArticleEdit.setInputEnabled(false)
            }
            val html = arguments?.getString("content", "") ?: ""
            if (html != "") {
                mReArticleEdit.html = html
            }
//            exec("""javascript:(function(){
//                                    var images = document.getElementsByTagName("img")
//                                    for (var i = 0; i < images.length; i++) {
//                                        var image = images[i]
//                                        if(image.src.indexOf("file://") == 0){
//                                            var httpPath = aApi.getHttpPathByJSPath(image.src)
//                                            image.src = httpPath
//                                        }
//                                    }
//                                }())
//                        """)
        }
        mReArticleEdit.addJavascriptInterface(MyEditorJavascriptInterface(),"aApi")
        mEtArticleEditTitle.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                mIBArticleEditActionImage.isEnabled = !hasFocus
                mIBArticleEditActionBold.isEnabled = !hasFocus
                mIBArticleEditActionLink.isEnabled = !hasFocus
                mIBArticleEditActionImage.setColorFilter(Color.parseColor("#cdcbcb"))
                mIBArticleEditActionBold.setColorFilter(Color.parseColor("#cdcbcb"))
                mIBArticleEditActionLink.setColorFilter(Color.parseColor("#cdcbcb"))
            }
        }

        mReArticleEdit.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                mIBArticleEditActionImage.isEnabled = hasFocus
                mIBArticleEditActionBold.isEnabled = hasFocus
                mIBArticleEditActionLink.isEnabled = hasFocus
                setBoldImage()
                mIBArticleEditActionImage.setColorFilter(Color.parseColor("#888888"))
                mIBArticleEditActionLink.setColorFilter(Color.parseColor("#888888"))
            }
        }

        mIBArticleEditActionImage.setOnClickListener {
            isBoldChecked = false
            setBoldImage()
            openImageChoose(MimeType.ofImage(), 1)
        }

        mIBArticleEditActionBold.setOnClickListener {
            isBoldChecked = !isBoldChecked
            mReArticleEdit.setBold()
            setBoldImage()
        }

        mIBArticleEditActionLink.setOnClickListener {
            isBoldChecked = false
            setBoldImage()
            val dialog = ArticleEditAddLinkDialogFragment()
            dialog.show(childFragmentManager, "link")
        }

        mReArticleEdit.setOnTextChangeListener {
            val length = Jsoup.parse(it).body().text().length
            articleCount = length
            (mContext as ArticleEditActivity).setSubTitleText(length)
            mTvEditCount.text = "${length}字"
        }
    }

    fun publicArticle(){
        if (!selectedImages.isEmpty()){
            selectedImages.forEach {
                if (it.httpPath == null || it.httpPath == ""){
                    toast("上传失败，请重新编辑")
                    return@forEach
                }
            }
        }
        if (mEtArticleEditTitle.isEmpty()){
            toast("请输入文章标题")
            return
        }
        if (articleCount == 0){
            toast("请输入文章内容")
            return
        }
        mPresent.getDataByPost(0x1, getBaseParamsWithModAndAct(ARTICLE_MODULE,ARTICLE_ISSUE_ACT)
            .addParam("classify",classify?.classify_id)
            .addParam("age",UserInfo.user_age)
            .addParam("address",UserInfo.user_address)
            .addParam("title",mEtArticleEditTitle.getTextString())
            .addParam("content",mReArticleEdit.html)
            .addParam("count",articleCount))
    }

    private fun exec(js: String = "") {
        val clazz = RichEditor::class.java
        val method = clazz.getDeclaredMethod("exec", String::class.java)
        method.isAccessible = true
        method.invoke(mReArticleEdit, js)
    }

    private fun setBoldImage() {
        if (isBoldChecked) {
            mIBArticleEditActionBold.setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccent))
        } else {
            mIBArticleEditActionBold.setColorFilter(Color.parseColor("#888888"))
        }
    }

    fun addLink(linkBean: LinkBean) {
        mReArticleEdit.insertLink(linkBean.address, linkBean.name)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x0 && resultCode == Activity.RESULT_OK && data != null) {
            compressAndSaveCacheFace(Matisse.obtainPathResult(data)[0], object : OnCompressListener {
                override fun onSuccess(file: File?) {
                    selectedImageBean = SelectedImageBean(file?.absolutePath ?: "", null)
                    selectedImages.add(selectedImageBean!!)
                    mReArticleEdit.insertImage(selectedImageBean!!.srcPath, """img "style="width:100% """)
                    ImageUploadHelper(object : ImageUploadHelper.OnImageUploadListener {
                        override fun onActionStart() {
                            getFastProgressDialog("正在上传图片……")
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
        super.onRequestStart(requestID)
        if (requestID == 0x1){
            getFastProgressDialog("正在上传文章……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        if (requestID == 0x0) {
            val json = JSONObject(result as String)
            val code = json.optInt("code")
            if (code == 200) {
                selectedImageBean?.httpPath = json.optString("result")
            } else {
                toast("图片上传失败")
            }
        }else if (requestID == 0x1){
            val check = checkResultCode(result)
            if (check!=null) {
                if (check.code == SUCCESS_CODE) {
                    toast("上传成功")
                } else if (check.code == "400") {
                    toast("请先进行身份认证")
                    WebViewContainerActivity.startWebViewContainerActivity(mContext, "authent.html")
                }
            }else{
                toast("上传失败")
            }
        }
    }

    fun onBackPress() {
        if (mReArticleEdit.html != "") {
            PreferenceUtils.setPrefString(mContext, EDITOR_HTML_FLAG, mReArticleEdit.html)
        }
        finish()
    }

    inner class MyEditorJavascriptInterface{

        /**
         * 根据js传过来的本地image.src查询对应上传网络后得到的图片地址，进行拼装后上传到服务器
         */
        @JavascriptInterface
        public fun getHttpPathByJSPath(jsPath:String) : String{
            for (imageBean in selectedImages){
                if ("file://${imageBean.srcPath}" == jsPath)
                    return imageBean.httpPath ?: jsPath
            }
            return jsPath
        }
    }

}

data class LinkBean(val name: String, val address: String)

data class SelectedImageBean(val srcPath: String? = "", var httpPath: String? = "")
