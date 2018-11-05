package com.android.ql.lf.article.ui.activity

import android.graphics.Color
import android.os.Build
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.android.ql.lf.article.R
import com.android.ql.lf.article.ui.fragments.article.ArticleEditFragment
import com.android.ql.lf.baselibaray.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_article_edit_layout.*

class ArticleEditActivity : BaseActivity(){

    override fun getLayoutId() = R.layout.activity_article_edit_layout

    private val articleEditFragment by lazy {
        ArticleEditFragment()
    }

    override fun initView() {
        statusBarColor = Color.WHITE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setSupportActionBar(mTlArticleEdit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mTlArticleEdit.setNavigationOnClickListener { finish() }
        supportFragmentManager.beginTransaction().replace(R.id.mFlArticleEditContainer,articleEditFragment).commit()
    }

    fun setSubTitleText(subText:Int){
        mTlArticleEdit.subtitle = "${subText}字"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_article_edit,menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onBackPressed() {
        articleEditFragment.onBackPress()
    }

}