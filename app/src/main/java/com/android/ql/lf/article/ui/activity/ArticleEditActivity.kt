package com.android.ql.lf.article.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.view.Menu
import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.article.ui.fragments.article.ArticleEditFragment
import com.android.ql.lf.baselibaray.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_article_edit_layout.*
import org.jetbrains.anko.bundleOf

class ArticleEditActivity : BaseActivity() {

    companion object {
        fun startArticleEditActivity(context: Context,title:String,content: String, isEdit: Boolean) {
            val intent = Intent(context, ArticleEditActivity::class.java)
            intent.putExtra("title",title)
            intent.putExtra("content", content)
            intent.putExtra("is_edit", isEdit)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_article_edit_layout

    private val articleEditFragment by lazy {
        val articleEditFragment = ArticleEditFragment()
        articleEditFragment.arguments = bundleOf(
            Pair("content", intent.getStringExtra("content")),
            Pair("is_edit", intent.getBooleanExtra("is_edit", false)),
            Pair("title", intent.getStringExtra("title"))
        )
        articleEditFragment
    }

    override fun initView() {
        statusBarColor = Color.WHITE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setSupportActionBar(mTlArticleEdit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mTlArticleEdit.setNavigationOnClickListener { finish() }
        mTvArticleEditMenu.setOnClickListener {
            articleEditFragment.publicArticle()
        }
        supportFragmentManager.beginTransaction().replace(R.id.mFlArticleEditContainer, articleEditFragment).commit()
    }

    fun setSubTitleText(subText: Int) {
        mTlArticleEdit.subtitle = "${subText}字"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_article_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onBackPressed() {
        articleEditFragment.onBackPress()
    }

}