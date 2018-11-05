package com.android.ql.lf.article.ui.fragments.article

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.baselibaray.ui.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_article_list_layout.*

class ArticleListFragment : BaseFragment(){

    private val titles = arrayListOf("推荐","推荐")

    override fun getLayoutId() = R.layout.fragment_article_list_layout

    override fun initView(view: View?) {
        mVpArticleList.adapter = object : FragmentStatePagerAdapter(childFragmentManager) {

            override fun getItem(position: Int): Fragment {
                return ArticleListItemFragment()
            }

            override fun getCount() = titles.size

            override fun getPageTitle(position: Int) = titles[position]
        }
        mTlArticleList.setupWithViewPager(mVpArticleList)
    }
}