package com.android.ql.lf.article.data

import com.chad.library.adapter.base.entity.MultiItemEntity

class ArticleItem : MultiItemEntity {

    var mType: Int = 0

    override fun getItemType() = mType
}