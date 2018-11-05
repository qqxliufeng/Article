package com.android.ql.lf.article.ui.adapters

import com.android.ql.lf.article.R
import com.android.ql.lf.article.data.ArticleItem
import com.android.ql.lf.article.ui.widgets.ImageContainerLinearLayout
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

class ArticleListAdapter(list: ArrayList<ArticleItem>) : BaseMultiItemQuickAdapter<ArticleItem, BaseViewHolder>(list) {

    companion object {
        const val MULTI_IMAGE_TYPE = 0
        const val SINGLE_IMAGE_TYPE = 1
    }

    init {
        addItemType(MULTI_IMAGE_TYPE, R.layout.adapter_article_multi_image_type_item_layout)
        addItemType(SINGLE_IMAGE_TYPE, R.layout.adapter_article_single_image_type_item_layout)
    }


    override fun convert(helper: BaseViewHolder?, item: ArticleItem?) {
        when (item!!.mType) {
            MULTI_IMAGE_TYPE -> {
                val imageContainer = helper?.getView<ImageContainerLinearLayout>(R.id.mICllArticleListItemContainer)
                imageContainer?.setImages(arrayListOf(
                        "http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=9b867a04b299a9012f38537575fc600e/4d086e061d950a7b86bee8d400d162d9f2d3c913.jpg",
                        "http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=9b867a04b299a9012f38537575fc600e/4d086e061d950a7b86bee8d400d162d9f2d3c913.jpg",
                        "http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=9b867a04b299a9012f38537575fc600e/4d086e061d950a7b86bee8d400d162d9f2d3c913.jpg"))
            }
            SINGLE_IMAGE_TYPE -> {
            }
        }
    }
}