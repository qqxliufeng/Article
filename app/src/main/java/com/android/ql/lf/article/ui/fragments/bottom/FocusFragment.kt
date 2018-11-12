package com.android.ql.lf.article.ui.fragments.bottom

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.ArticleType
import com.android.ql.lf.article.ui.fragments.article.ArticleListItemFragment
import com.android.ql.lf.article.ui.fragments.other.ArticleWebViewFragment
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import kotlinx.android.synthetic.main.fragment_focus_layout.*

class FocusFragment : BaseNetWorkingFragment() {

    private val titles = arrayListOf("全部", "莫失莫忘")

    override fun getLayoutId() = R.layout.fragment_focus_layout

    override fun initView(view: View?) {
        mVpArticleList.adapter = object : FragmentStatePagerAdapter(childFragmentManager) {

            override fun getItem(position: Int): Fragment {
                return ArticleListItemFragment()
            }

            override fun getCount() = titles.size

            override fun getPageTitle(position: Int) = titles[position]
        }
        mTlArticleList.setupWithViewPager(mVpArticleList)
        mIvBottomFocusSearch.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "搜索", "search.html", ArticleType.OTHER.type)
        }
    }

}