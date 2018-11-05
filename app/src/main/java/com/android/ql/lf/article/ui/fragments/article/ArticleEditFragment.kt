package com.android.ql.lf.article.ui.fragments.article

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.article.ui.activity.ArticleEditActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import com.android.ql.lf.baselibaray.utils.PreferenceUtils
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import jp.wasabeef.richeditor.RichEditor
import kotlinx.android.synthetic.main.fragment_article_edit_layout.*
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

    override fun getLayoutId() = R.layout.fragment_article_edit_layout

    override fun initView(view: View?) {
        mReArticleEdit.setPlaceholder("请输入内容")
        mReArticleEdit.setPadding(0, 10, 0, 10)
        mReArticleEdit.setTextColor(ContextCompat.getColor(mContext, R.color.normalTextColor))
        mReArticleEdit.focusEditor()
        mReArticleEdit.setOnInitialLoadListener {
            val html = PreferenceUtils.getPrefString(mContext,EDITOR_HTML_FLAG,"")
            if (html != ""){
                mReArticleEdit.html = html
            }
        }
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
            openImageChoose(MimeType.ofImage(),1)
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
            (mContext as ArticleEditActivity).setSubTitleText(length)
            mTvEditCount.text = "${length}字"
        }
    }

    private fun exec(js:String = ""){
        val clazz = RichEditor::class.java
        val method = clazz.getDeclaredMethod("exec", String::class.java)
        method.isAccessible = true
        method.invoke(mReArticleEdit,js)
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
        if (requestCode == 0x0 && resultCode == Activity.RESULT_OK && data != null){
            Matisse.obtainPathResult(data)[0].let {
                Luban.with(mContext)
                        .load(it)
                        .setTargetDir(mContext.cacheDir.absolutePath)
                        .setCompressListener(object : OnCompressListener {
                            override fun onSuccess(file: File?) {
                                val selectedImageBean = SelectedImageBean(file?.absolutePath,null)
                                selectedImages.add(selectedImageBean)
                                mReArticleEdit.insertImage(selectedImageBean.srcPath,"""img "style="width:100% """)
                            }

                            override fun onError(e: Throwable?) {
                            }

                            override fun onStart() {
                            }
                        }).launch()
            }
        }
    }

    fun onBackPress(){
        if (mReArticleEdit.html != "") {
            PreferenceUtils.setPrefString(mContext, EDITOR_HTML_FLAG, mReArticleEdit.html)
        }
        finish()
    }
}

data class LinkBean(val name: String, val address: String)

data class SelectedImageBean(val srcPath:String?,val httpPath:String?)
