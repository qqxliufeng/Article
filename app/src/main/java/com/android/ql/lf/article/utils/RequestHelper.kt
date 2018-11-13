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
