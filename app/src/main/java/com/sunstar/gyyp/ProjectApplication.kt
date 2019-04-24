package com.sunstar.gyyp

import android.app.Application

class ProjectApplication:Application() {
    companion object {
        lateinit var instance:ProjectApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}