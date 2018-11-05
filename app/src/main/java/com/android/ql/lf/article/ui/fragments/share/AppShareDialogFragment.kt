package com.android.ql.lf.article.ui.fragments.share

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.ql.lf.article.R
import kotlinx.android.synthetic.main.dialog_app_share_layout.*
import org.jetbrains.anko.support.v4.toast

class AppShareDialogFragment : BottomSheetDialogFragment() {


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