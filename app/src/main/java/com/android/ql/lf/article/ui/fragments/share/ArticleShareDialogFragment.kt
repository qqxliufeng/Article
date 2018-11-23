package com.android.ql.lf.article.ui.fragments.share

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.ArticleShareItem
import com.android.ql.lf.article.utils.ThirdShareManager
import com.android.ql.lf.baselibaray.data.BaseShareItem
import kotlinx.android.synthetic.main.dialog_article_share_layout.*

class ArticleShareDialogFragment : AppShareDialogFragment() {

    private var onCreateImage:(()->Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_article_share_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTvAppShareCancel.setOnClickListener { dismiss() }
        mTvAppShareOpenWithBrowser.setOnClickListener {
            openWithBrowser()
            dismiss()
        }
        mTvAppShareCopy.setOnClickListener {
            copyBroad()
            dismiss()
        }
        mTvAppShareMore.setOnClickListener {
            dismiss()
            shareMore()
        }
        mTvAppShareCreateImage.setOnClickListener {
            dismiss()
            onCreateImage?.invoke()
        }
        mTvArticleShareWeibo.setOnClickListener {
            dismiss()
            webiboShare()
        }
    }

    fun setCreateImage(onCreateImage:(()->Unit)){
        this.onCreateImage = onCreateImage
    }

    override fun webiboShare(){
        if (baseShareItem!=null) {
            shareHandler?.registerApp()
            shareHandler?.setProgressColor(ContextCompat.getColor(context!!,R.color.colorAccent))
            shareHandler?.shareMessage(ThirdShareManager.getWebpageObj(context!!,baseShareItem?.title ?: "",baseShareItem?.content ?: "",baseShareItem?.url ?: ""),false)
        }
    }
}