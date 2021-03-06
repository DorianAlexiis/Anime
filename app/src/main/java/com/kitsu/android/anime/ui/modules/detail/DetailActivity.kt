package com.kitsu.android.anime.ui.modules.detail

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import com.android.dars.base.BaseInjectActivity
import com.bumptech.glide.Glide
import com.kitsu.android.anime.R
import com.kitsu.android.anime.data.response.DataItem
import com.kitsu.android.anime.utils.showImageRound
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import org.jetbrains.anko.toast
import javax.inject.Inject

class DetailActivity: BaseInjectActivity(), DetailView, AppBarLayout.OnOffsetChangedListener {

    @Inject
    @JvmField
    var detailPresenter: DetailPresenter?  = null

    private var dataItem: DataItem? = null
    private var scrollRange = -1
    private var isShow = true

    override fun initialize() {
        super.initialize()
        detailPresenter?.mView =this@DetailActivity
    }

    override fun getActivityLayoutResId() = R.layout.activity_detail


    override fun onViewCreated(savedInstanceState: Bundle?) {
        super.onViewCreated(savedInstanceState)
        setSupportActionBar(toolbar)

        collapsing_toolbar.title = " "

        app_bar_layout.addOnOffsetChangedListener(this)

        intent?.let {
            val slug = it.getStringExtra("slug")

            detailPresenter?.getDetailAnime(slug)
        }
    }


    override fun onShowData(item: DataItem?) {
        item?.let {
            this.dataItem = item

            showImageRound(Glide.with(this), imageView = ivFaceBook, url = item.attributes?.posterImage?.small)

            Glide.with(this)
                    .load(item.attributes?.coverImage?.large)
                    .into(ivCover)

            toolbar.title = item.attributes?.canonicalTitle
            tvName.text = item.attributes?.canonicalTitle

            tvSynopsis.text = item.attributes?.synopsis
        }
    }

    override fun onFailure() {
        toast(R.string.error_connection)
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        dataItem?.let {
            if (scrollRange == -1) {
                scrollRange = appBarLayout?.totalScrollRange?:0
            }
            if (scrollRange + verticalOffset == 0) {
                collapsing_toolbar.title = it.attributes?.canonicalTitle
                isShow = true
            } else if(isShow) {
                collapsing_toolbar.title = " "
                isShow = false
            }
        }
    }



}
