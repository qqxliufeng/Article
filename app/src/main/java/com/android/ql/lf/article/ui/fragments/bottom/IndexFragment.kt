package com.android.ql.lf.article.ui.fragments.bottom

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.ArticleType
import com.android.ql.lf.article.ui.fragments.article.ArticleListFragment
import com.android.ql.lf.article.ui.fragments.other.ArticleWebViewFragment
import com.android.ql.lf.baselibaray.ui.fragment.BaseFragment
import kotlinx.android.synthetic.main.fragment_bottom_index_layout.*

class IndexFragment : BaseFragment() {

    val titles = arrayListOf("类别", "年龄", "地域")

    override fun getLayoutId() = R.layout.fragment_bottom_index_layout

    override fun initView(view: View?) {
        mVpIndexBottom.adapter = object : FragmentStatePagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return ArticleListFragment()
            }

            override fun getCount() = titles.size

            override fun getPageTitle(position: Int) = titles[position]
        }
        mTlIndexBottom.setupWithViewPager(mVpIndexBottom)
        mIvBottomIndexSearch.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "搜索", "search.html", ArticleType.OTHER.type)
        }
    }
}