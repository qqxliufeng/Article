package com.android.ql.lf.article.data

import com.chad.library.adapter.base.entity.MultiItemEntity

class ArticleItem : MultiItemEntity {

    var mType: Int = 0

    override fun getItemType() = mType
}

enum class ArticleType(val type: Int) {
    PRIVATE_ARTICLE(1),
    PUBLIC_ARTICLE(2),
    COLLECTION_ARTICLE(3),
    POST_ARTICLE(4),
    OTHER(-1);

    companion object {
        fun getTypeNameById(type: Int): ArticleType {
            return when (type) {
                PRIVATE_ARTICLE.type -> {
                    PRIVATE_ARTICLE
                }
                PUBLIC_ARTICLE.type -> {
                    PUBLIC_ARTICLE
                }
                COLLECTION_ARTICLE.type -> {
                    COLLECTION_ARTICLE
                }
                POST_ARTICLE.type -> {
                    POST_ARTICLE
                }
                else -> {
                    POST_ARTICLE
                }
            }
        }
    }
}