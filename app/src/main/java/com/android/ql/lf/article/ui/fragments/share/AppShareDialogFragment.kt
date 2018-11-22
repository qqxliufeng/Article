package com.android.ql.lf.article.ui.fragments.share

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.ql.lf.article.R
import com.android.ql.lf.article.utils.ThirdShareManager
import com.sina.weibo.sdk.share.WbShareHandler
import kotlinx.android.synthetic.main.dialog_app_share_layout.*
import org.jetbrains.anko.support.v4.toast

class AppShareDialogFragment : BottomSheetDialogFragment() {

    private var shareHandler:WbShareHandler? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_app_share_layout, container, false)
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
        mTvAppShareWeiBo.setOnClickListener {
            dismiss()
            webiboShare()
        }
    }

    fun setWeiBoShareHandler(shareHandler: WbShareHandler){
        this.shareHandler = shareHandler
    }

    fun webiboShare(){
        shareHandler?.registerApp()
        shareHandler?.setProgressColor(ContextCompat.getColor(context!!,R.color.colorAccent))
        shareHandler?.shareMessage(ThirdShareManager.getWebpageObj(context!!,resources.getString(R.string.app_name),"这是一款好用的文章app","http://article.581vv.com"),false)
    }

    fun shareMore() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.type = "text/plain"
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Share")
        sendIntent.putExtra(Intent.EXTRA_TEXT, "I have successfully share my message through my app")
        startActivity(Intent.createChooser(sendIntent, "share"))
    }

    fun openWithBrowser() {
        val browserIntent = Intent()
        browserIntent.data = Uri.parse("http://www.baidu.com")
        browserIntent.action = Intent.ACTION_VIEW
        startActivity(browserIntent)
    }

    fun copyBroad() {
        val copyBroadManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        copyBroadManager.text = "http://www.baidu.com"
        toast("复制成功")
    }

}