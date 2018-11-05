package com.android.ql.lf.article.application

import com.android.ql.lf.baselibaray.application.BaseApplication

class MyApplication : BaseApplication() {

    companion object {

        private lateinit var instance: MyApplication

        fun getInstance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}