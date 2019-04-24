package com.sunstar.gyyp.base

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.data.BannerItem
import com.youth.banner.loader.ImageLoader

class GlideImageLoader:ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        Glide.with(context!!).load(Url.baseUrl+(path as BannerItem).pic).into(imageView!!)
    }

    override fun createImageView(context: Context?): ImageView {
        var image = ImageView(context)
        image.scaleType = ImageView.ScaleType.CENTER_CROP
        return image
}
}