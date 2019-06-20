package com.sunstar.gyyp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_web.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.view.KeyEvent.KEYCODE_BACK
import android.webkit.WebChromeClient
import org.jetbrains.anko.info


class WebActivity : BaseActivity() {
    override fun appViewInitComplete() {
        var url = if(intent.getStringExtra("url").contains(Url.baseUrl)) intent.getStringExtra("url") else Url.baseUrl+intent.getStringExtra("url")
        info { url }

        web.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        web.webChromeClient = object:WebChromeClient(){
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                changeText(title!!)
            }
        }
        web.settings.useWideViewPort = true//将图片调整到适合webView的大小
        web.settings.javaScriptEnabled = true//将图片调整到适合webView的大小
        web.settings.domStorageEnabled = true//将图片调整到适合webView的大小
        web.settings.loadWithOverviewMode = true//缩放至屏幕大小
        web.loadUrl(url)
    }

    override fun initHeadModel(): HeadVm = HeadVm("", true, R.mipmap.back)

    override fun initView(): View = View.inflate(mContext, R.layout.activity_web, null)

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && web.canGoBack()) {  // goBack()表示返回WebView的上一页面
            web.goBack()
            return true
        } else {
            //结束当前页
            return super.onKeyDown(keyCode, event)
        }
    }

}
