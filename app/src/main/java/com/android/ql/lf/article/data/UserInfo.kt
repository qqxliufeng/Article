package com.android.ql.lf.article.data

import android.arch.lifecycle.LiveData
import com.android.ql.lf.article.application.MyApplication
import com.android.ql.lf.baselibaray.utils.PreferenceUtils
import org.json.JSONObject

const val USER_ID_FLAG = "user_id"

/**
 * 广播用户数据
 */
fun UserInfo.postUserInfo() = UserInfoLiveData.postUserInfo()

/**
 * 更新用户数据
 */
fun UserInfo.updateUserInfo(nickName:String, userPic:String){
    user_nickname = nickName
    user_pic = userPic
    this.postUserInfo()
}

/**
 * 清除用户数据
 */
fun UserInfo.clearUserInfo() {
    user_id = -1
    user_phone = null
    user_nickname = null
    user_pic = null
    this.postUserInfo()
}

/**
 * 判断用户是否是登录状态
 */
fun UserInfo.isLogin(): Boolean = user_id != -1 && user_phone != null


/**
 * 解析用户数据
 */
fun UserInfo.jsonToUserInfo(json: JSONObject): Boolean {
    return try {
        user_id = json.optInt("member_id")
        user_nickname = json.optString("member_nickname")
        user_phone = json.optString("member_phone")
        user_pic = json.optString("member_pic")
        user_balance = json.optInt("member_balance")
        user_fans = json.optInt("member_fans")
        user_tags = json.optString("member_tags")
        user_status = json.optInt("member_status")
        user_address = json.optInt("member_address")
        user_classify = json.optString("member_classify")
        user_age = json.optInt("member_age")
        user_birthday = json.optString("member_birthday")
        user_sex = json.optInt("member_sex")
        user_push = json.optInt("member_push")
        PreferenceUtils.setPrefInt(MyApplication.getInstance(), USER_ID_FLAG, user_id)
        true
    } catch (e: Exception) {
        false
    }
}

object UserInfo {
    var user_id: Int = -1
    var user_phone: String? = null
    var user_nickname: String? = null
    var user_pic: String? = null
    var user_balance:Int? = null
    var user_fans:Int? = null
    var user_tags:String? = null
    var user_status:Int? = null
    var user_address:Int? = null
    var user_classify:String? = null
    var user_age:Int? = null
    var user_birthday:String? = null
    var user_sex:Int? = null
    var user_push:Int? = null
}

object UserInfoLiveData : LiveData<UserInfo>() {

    fun postUserInfo() {
        value = UserInfo
    }

}
