package com.sunstar.gyyp

import android.app.Application
import com.bumptech.glide.Glide
import com.yuyh.library.imgsel.ISNav

class ProjectApplication:Application() {
    companion object {
        lateinit var instance:ProjectApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        ISNav.getInstance().init { context, path, imageView ->
            Glide.with(context).load(path).into(imageView)
        }
    }
}