package com.android.ql.lf.article.ui.fragments.login

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.View
import android.view.ViewGroup
import com.android.ql.lf.article.R
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseFragment
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import com.android.ql.lf.baselibaray.ui.fragment.BaseViewPagerFragment
import kotlinx.android.synthetic.main.fragment_login_layout.*

class LoginFragment : BaseViewPagerFragment() {

    companion object {
        fun startLoginFragment(context: Context) {
            FragmentContainerActivity.from(context).setTitle("").setNeedNetWorking(false).setHiddenToolBar(true).setClazz(LoginFragment::class.java).start()
        }
    }

    override fun getViewPagerAdapter() = object : FragmentPagerAdapter(childFragmentManager) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    LoginFirstStepFragment()
                }
                1 -> {
                    LoginSecondStepFragment()
                }
                else -> {
                    LoginFirstStepFragment()
                }
            }
        }

        override fun getCount() = 2
    }

    override fun positionFailed(position: Int) {
        finish()
    }
}