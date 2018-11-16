package com.android.ql.lf.article.utils

import com.android.ql.lf.article.data.UserInfo
import com.android.ql.lf.article.data.isLogin
import com.android.ql.lf.baselibaray.component.ApiParams

/**
 * Created by lf on 18.11.13.
 * @author lf on 18.11.13
 */

fun getBaseParams(): ApiParams = ApiParams().addParam("pt", "android").addParam("uid", if (UserInfo.isLogin()) UserInfo.user_id else "")

fun getBaseParamsWithModAndAct(mod: String, act: String) : ApiParams = getBaseParams().addParam(ApiParams.MOD_NAME, mod).addParam(ApiParams.ACT_NAME, act)

fun getBaseParamsWithPage(mod: String, act: String, page: Int = 0, pageSize: Int = 10) : ApiParams = getBaseParamsWithModAndAct(mod, act).addParam("page", page).addParam("pagesize", pageSize)

/**       公用模块                **/
const val T_MODULE = "t"
const val UPLOADING_PIC_ACT = "uploadingPic"

/**       登录模块                **/
const val LOGIN_MODULE = "login"

const val LOGINDO_ACT = "loginDo"
const val REGISTERDO_ACT = "registerDo"
const val SMSCODE_ACT = "smscode"
const val ADDRESS_ACT = "address"
const val CLASSIFY_ACT = "classify"
const val EDITUSERPIC_ACT = "editUserPic"


/**       文章模块                **/
const val ARTICLE_MODULE = "article"
const val ARTICLENAV_ACT = "articleNav"
const val ARTICLE_LIST_ACT = "articleList"
const val ARTICLE_DETAIL_ACT = "articleDetail"
const val ARTICLE_LIKE_ACT = "articleLike"
const val ARTICLE_ISSUE_ACT = "articleIssue"
const val ARTICLE_COMMENT_ACT = "articleComment"
const val ARTICLE_COMMENT_DO_ACT = "articleCommentDo" //发表评论
const val ARTICLE_COMMENT_REPLY_ACT = "articleCommentReply" //评论回复
const val ARTICLE_COMMENT_LIKE_ACT = "articleCommentLike" //评论喜欢
const val ARTICLE_LOVE_ACT = "articleLove" //文章喜欢
const val ARTICLE_COLLECT_ACT = "articleCollect" //文章收藏

/**       用户模块                **/
const val MEMBER_MODULE = "member"
const val PERSONAL_ACT = "personal"
const val EDIT_USER_PIC_ACT = "editUserPic"
const val PERSONAL_EDIT_ACT = "personalEdit"
