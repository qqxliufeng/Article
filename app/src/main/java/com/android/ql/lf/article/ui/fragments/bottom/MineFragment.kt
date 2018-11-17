package com.android.ql.lf.article.ui.fragments.bottom

import android.arch.lifecycle.Observer
import android.view.View
import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.*
import com.android.ql.lf.article.ui.fragments.mine.PersonalEditFragment
import com.android.ql.lf.article.ui.fragments.share.AppShareDialogFragment
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import kotlinx.android.synthetic.main.fragment_mine_layout.*
import com.android.ql.lf.article.ui.activity.WebViewContainerActivity
import com.android.ql.lf.article.ui.fragments.login.LoginFragment
import com.android.ql.lf.article.ui.fragments.mine.PersonalIndexFragment
import com.android.ql.lf.article.ui.fragments.other.ArticleWebViewFragment
import com.android.ql.lf.article.utils.*
import com.android.ql.lf.baselibaray.utils.GlideManager
import com.android.ql.lf.baselibaray.utils.PreferenceUtils
import org.json.JSONObject


class MineFragment : BaseNetWorkingFragment() {

    private val shareDialogFragment by lazy {
        AppShareDialogFragment()
    }

    override fun getLayoutId() = R.layout.fragment_mine_layout

    override fun initView(view: View?) {
        UserInfoLiveData.observe(this, Observer<UserInfo> {
            if (it!!.isLogin()) {
                GlideManager.loadFaceCircleImage(mContext, UserInfo.user_pic, mIvMineFace)
                mTvMineNickName.text = UserInfo.user_nickname
                mTvMineFocusCount.text = "${UserInfo.user_loveCount}"
                mTvMinelikeCount.text = "${UserInfo.user_likeCount}"
                mTvMineFansCount.text = "${UserInfo.user_fanCount}"
                mTvMineCollectionCount.text = "${UserInfo.user_colStatus}"
            }else{
                mIvMineFace.setImageResource(R.drawable.img_glide_circle_load_default)
                mTvMineNickName.text = "登录/注册"
                mTvMineFocusCount.text = "0"
                mTvMinelikeCount.text = "0"
                mTvMineFansCount.text = "0"
                mTvMineCollectionCount.text = "0"
            }
        })
        mRlMineUserInfoContainer.doClickWithUserStatusStart("") {
            PersonalIndexFragment.startPersonalIndexFragment(mContext)
        }
        mTvMineEditPersonalInfo.doClickWithUserStatusStart("") {
            PersonalEditFragment.startPersonalEditFragment(mContext)
        }
        mTvMineShare.setOnClickListener {
            shareDialogFragment.show(childFragmentManager, "app_share_dialog")
        }
        mLlMineFocusCount.doClickWithUserStatusStart("") {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "关注", "attention.html", ArticleType.OTHER.type)
        }
        mLlMineFansCount.doClickWithUserStatusStart("") {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "粉丝", "attention.html",ArticleType.OTHER.type)
        }
        mLlMineLikeCount.doClickWithUserStatusStart("") {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "喜欢", "like.html",ArticleType.OTHER.type)
        }
        mLlMineCollectionCount.doClickWithUserStatusStart("") {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "收藏文章", "like.html",ArticleType.COLLECTION_ARTICLE.type)
        }
        mTvMineWallet.doClickWithUserStatusStart("") {
            WebViewContainerActivity.startWebViewContainerActivity(mContext, "wallet.html")
        }
        mTvMineTrash.doClickWithUserStatusStart("") {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "回收站", "contribute.html",ArticleType.OTHER.type)
        }
        mTvMineAuth.doClickWithUserStatusStart("") {
            WebViewContainerActivity.startWebViewContainerActivity(mContext, "authent.html")
        }
        mTvMineCollection.doClickWithUserStatusStart("") {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "收录文章", "contribute.html",ArticleType.COLLECTION_ARTICLE.type)
        }
        mTvMinePost.doClickWithUserStatusStart("") {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "投稿文章", "contribute.html",ArticleType.POST_ARTICLE.type)
        }
        mTvMinePublic.doClickWithUserStatusStart("") {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "公开文章", "contribute.html",ArticleType.PUBLIC_ARTICLE.type)
        }
        mTvMinePrivate.doClickWithUserStatusStart("") {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "私密文章", "contribute.html",ArticleType.PRIVATE_ARTICLE.type)
        }
        mTvMineAccountSafe.doClickWithUserStatusStart("") {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "帐号与安全", "safety.html",ArticleType.OTHER.type)
        }
        mTvMineHistory.doClickWithUserStatusStart("") {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "浏览历史", "history.html",ArticleType.OTHER.type)
        }
        mTvMineLogout.setOnClickListener {
            if (!UserInfo.isLogin()){
                return@setOnClickListener
            }
            alert("提示", "是否要退出当前帐号？", "退出", "取消", { _, _ ->
                UserInfo.loginOut()
            }, null)
        }
        mTvMineFeedback.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "意见反馈", "feedback.html",ArticleType.OTHER.type)
        }
        mTvMineProtocol.setOnClickListener {
            ArticleWebViewFragment.startArticleWebViewFragment(mContext, "用户协议", "protocol.html",ArticleType.OTHER.type)
        }
        if (!UserInfo.isLogin() && PreferenceUtils.getPrefInt(mContext,USER_ID_FLAG,-1) != -1){
            mPresent.getDataByPost(0x0, getBaseParamsWithModAndAct(MEMBER_MODULE, PERSONAL_ACT).addParam("uid",PreferenceUtils.getPrefInt(mContext,USER_ID_FLAG,-1)))
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        if (requestID == 0x0) {
            try {
                val check = checkResultCode(result)
                if (check != null) {
                    if (check.code == SUCCESS_CODE) {
                        val jsonObject = (check.obj as JSONObject).optJSONObject(RESULT_OBJECT)
                        if (UserInfo.jsonToUserInfo(jsonObject)) {
                            UserInfo.postUserInfo()
                        }
                    }
                }
            } catch (e: Exception) {
            }
        }
    }
}