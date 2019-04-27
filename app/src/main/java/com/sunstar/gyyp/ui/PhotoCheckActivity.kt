package com.sunstar.gyyp.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.yanzhenjie.sofia.Sofia
import kotlinx.android.synthetic.main.activity_photo_check.*

class PhotoCheckActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_check)
        Sofia.with(this).invasionStatusBar().statusBarBackground(this.resources.getColor(R.color.color_transparent))
        var imagePath = if (intent.getStringExtra("photoPath").contains(Url.baseUrl)) intent.getStringExtra("photoPath") else Url.baseUrl + intent.getStringExtra("photoPath")
        Glide.with(this).load(imagePath).into(photo_checkview)
    }
}
