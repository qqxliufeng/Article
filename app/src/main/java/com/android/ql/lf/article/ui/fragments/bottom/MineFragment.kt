package com.android.ql.lf.article.ui.fragments.bottom

import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.ArticleType
import com.android.ql.lf.article.ui.fragments.mine.PersonalEditFragment
import com.android.ql.lf.article.ui.fragments.mine.PersonalIndexFragment
import com.android.ql.lf.article.ui.fragments.share.AppShareDialogFragment
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import kotlinx.android.synthetic.main.fragment_mine_layout.*
import com.android.ql.lf.article.ui.activity.WebViewContainerActivity
import com.android.ql.lf.article.ui.fragments.login.LoginFragment
import com.android.ql.lf.article.ui.fragments.other.ArticleWebViewFragment
import com.android.ql.lf.article.utils.alert


class MineFragment : BaseNetWorkingFragment() {

    private val shareDialogFragment by lazy {
        AppShareDialogFragment()
    }

    override fun getLayoutId() = R.layout.fragment_mine_layout

    override fun initView(view: View?) {
        mRlMineUserInfoContainer.setOnClickListener {
            LoginFragment.startLoginFragment(mContext)
        }
        mTvMineEditPersonalInfo.setOnClickListener {
            PersonalEditFragment.startPersonalEditFragment(mContext)
        }
        mTvMineShare.setOnClickListener {
            shareDialogFragment.show(childFragmentManager, "app_share_dialog")
        }
        mLlMineFocusCount.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "关注", "attention.html", ArticleType.OTHER.type)
        }
        mLlMineFansCount.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "粉丝", "attention.html",ArticleType.OTHER.type)
        }
        mLlMineLikeCount.setOnClickListener {
            //            val url = "mqqwpa://im/chat?chat_type=wpa&uin=2585346539"
//            startActivity(Intent(ACTION_VIEW, Uri.parse(url)))
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "喜欢", "like.html",ArticleType.OTHER.type)
        }
        mLlMineCollectionCount.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "收藏文章", "like.html",ArticleType.COLLECTION_ARTICLE.type)
        }
        mTvMineWallet.setOnClickListener {
            WebViewContainerActivity.startWebViewContainerActivity(mContext, "wallet.html")
        }
        mTvMineTrash.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "回收站", "contribute.html",ArticleType.OTHER.type)
        }
        mTvMineAuth.setOnClickListener {
            WebViewContainerActivity.startWebViewContainerActivity(mContext, "authent.html")
        }
        mTvMineCollection.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "收录文章", "contribute.html",ArticleType.COLLECTION_ARTICLE.type)
        }
        mTvMinePost.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "投稿文章", "contribute.html",ArticleType.POST_ARTICLE.type)
        }
        mTvMinePublic.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "公开文章", "contribute.html",ArticleType.PUBLIC_ARTICLE.type)
        }
        mTvMinePrivate.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "私密文章", "contribute.html",ArticleType.PRIVATE_ARTICLE.type)
        }
        mTvMineAccountSafe.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "帐号与安全", "safety.html",ArticleType.OTHER.type)
        }
        mTvMineHistory.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "浏览历史", "history.html",ArticleType.OTHER.type)
        }
        mTvMineLogout.setOnClickListener {
            alert("提示", "是否要退出当前帐号？", "退出", "取消", { _, _ ->

            }, null)
        }
        mTvMineFeedback.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "意见反馈", "feedback.html",ArticleType.OTHER.type)
        }
        mTvMineProtocol.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "用户协议", "protocol.html",ArticleType.OTHER.type)
        }
    }
}