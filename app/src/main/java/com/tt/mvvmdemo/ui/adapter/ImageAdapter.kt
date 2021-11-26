package com.tt.mvvmdemo.ui.adapter

import android.content.Context
import com.tt.mvvmdemo.httpUtils.Banner
import com.tt.mvvmdemo.utils.ImageLoader
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder

/**
 * Banner适配器
 */
class ImageAdapter(private val context: Context, imgList: List<Banner>) :
    BannerImageAdapter<Banner>(imgList) {

    override fun onBindView(holder: BannerImageHolder?, data: Banner?, position: Int, size: Int) {
        ImageLoader.loadBanner(context, data?.imagePath, holder?.imageView)
    }
}