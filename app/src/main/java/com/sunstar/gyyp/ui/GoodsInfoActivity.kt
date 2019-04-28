package com.sunstar.gyyp.ui

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.GlideImageLoader
import com.sunstar.gyyp.base.GlideImageStringPathLoader
import com.sunstar.gyyp.databinding.ActivityGoodsInfoBinding
import com.sunstar.gyyp.view.GoodsInfoView
import com.sunstar.gyyp.vm.GoodsInfoVm
import kotlinx.android.synthetic.main.activity_goods_info.*

class GoodsInfoActivity : BaseActivity(), GoodsInfoView {
    override fun loadInfo(url: String) {
        web.loadUrl(Url.baseUrl + url)
    }

    override fun showBanner(banners: MutableList<String>) {
        banner.setImages(banners)
        banner.start()
    }


    var vm: GoodsInfoVm? = null
    override fun appViewInitComplete() {
        vm?.initGoodsInfo(intent.getStringExtra("id"))
        banner.setImageLoader(GlideImageStringPathLoader())
        banner.setOnBannerListener {

        }
        web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        web.settings.useWideViewPort = true//将图片调整到适合webView的大小
        web.settings.loadWithOverviewMode = true//缩放至屏幕大小
    }



    override fun initHeadModel(): HeadVm = HeadVm("商品详情", true, R.mipmap.back)

    override fun initView(): View {
        var b = DataBindingUtil.inflate<ActivityGoodsInfoBinding>(LayoutInflater.from(mContext),
                R.layout.activity_goods_info, null, false)
        vm = GoodsInfoVm(this)
        b.data = vm
        return b.root
    }

    override fun onDestroy() {
        super.onDestroy()
        vm?.onDestory()
    }

}
