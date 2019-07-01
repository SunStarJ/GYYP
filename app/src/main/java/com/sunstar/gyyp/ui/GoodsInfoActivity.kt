package com.sunstar.gyyp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.widget.NestedScrollView
import android.view.*
import android.webkit.*
import android.widget.TextView
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.GlideImageLoader
import com.sunstar.gyyp.base.GlideImageStringPathLoader
import com.sunstar.gyyp.data.PublicStaticData
import com.sunstar.gyyp.databinding.ActivityGoodsInfoBinding
import com.sunstar.gyyp.view.GoodsInfoView
import com.sunstar.gyyp.vm.GoodsInfoVm
import kotlinx.android.synthetic.main.activity_goods_info.*
import com.sunstar.gyyp.databinding.GoodsInfoBottomDialogBinding
import com.sunstar.gyyp.vm.ShopCartVm
import org.jetbrains.anko.sdk27.coroutines.onClick
import android.webkit.WebView
import android.view.MotionEvent
import android.view.View.OnTouchListener
import com.zhy.view.flowlayout.TagFlowLayout.dip2px
import android.widget.LinearLayout
import org.jetbrains.anko.*


class GoodsInfoActivity : BaseActivity(), GoodsInfoView {
    override fun initSelect(isCollect: Boolean) {
        var imgR = if(isCollect)  R.mipmap.yishoucang else R.mipmap.shoucang
        var drawable = mContext.resources.getDrawable(imgR)
        drawable.setBounds(0,0,drawable.intrinsicWidth,drawable.intrinsicHeight)
        select.setCompoundDrawables(null,drawable,null,null)
        select.text = if(isCollect) "已收藏" else "收藏"
        select.textColor = mContext.resources.getColor(if(isCollect) R.color.color_red else R.color.color_text_black )
    }

    override fun buyNow() {
        startActivity<ShoopCartActivity>()
    }

    override fun hideBottom() {
        dialog?.dismiss()
    }

    override fun showPrice() {
        binding?.showNum = vm?.showNum
    }

    override fun loadInfo(url: String) {
        web.loadUrl(Url.baseUrl+url)
    }

    override fun showBanner(banners: MutableList<String>) {
        banner.setImages(banners)
        banner.start()
    }


    var vm: GoodsInfoVm? = null
    @SuppressLint("JavascriptInterface")
    override fun appViewInitComplete() {
        vm?.initGoodsInfo(intent.getStringExtra("id"))
        banner.setImageLoader(GlideImageStringPathLoader())
        banner.setOnBannerListener {

        }
        web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置此属性，可任意比例缩放
        web.getSettings().setUseWideViewPort(false);
        // 设置不出现缩放工具
        web.getSettings().setBuiltInZoomControls(false);
        // 设置不可以缩放
        web.getSettings().setSupportZoom(false);
        web.getSettings().setDisplayZoomControls(false);
        web.settings.setDomStorageEnabled(true);//设置适应Html5的一些方法
        // 设置的WebView是否支持变焦
        web.getSettings().setSupportZoom(false);
        web.getSettings().setBuiltInZoomControls(false);
        web.getSettings().setUseWideViewPort(false);
        //自适应屏幕
        web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        // 自适应 屏幕大小界面
        web.getSettings().setLoadWithOverviewMode(true);
        web.clearCache(true);
        web.clearHistory();
        web.addJavascriptInterface(this,"App")

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            web.addJavascriptInterface(this, "App");
        }

        web.webViewClient = object:WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view!!.loadUrl(url)
                return true
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                view!!.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)")
                view.measure(0, 0)
//                val measuredHeight = view.getMeasuredHeight()
//                info { "measuredHeight1:$measuredHeight" }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                view!!.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)")
//                view.measure(0, 0)
//                val measuredHeight = view.getMeasuredHeight()
//                info { "measuredHeight2:$measuredHeight" }

            }
        }
    }

    @JavascriptInterface
    fun resize(height:Int){
        info { "height:$height" }
        (this as Context).runOnUiThread {
            //Toast.makeText(getActivity(), height + "", Toast.LENGTH_LONG).show();
            //此处的 layoutParmas 需要根据父控件类型进行区分，这里为了简单就不这么做了
            var  params = web.getLayoutParams() as LinearLayout.LayoutParams
//            params.height = (int)(height * getResources().getDisplayMetrics().density);
//            m_webView.requestLayout();
        }
    }


    var binding: GoodsInfoBottomDialogBinding? = null
    var dialog: BottomSheetDialog? = null
    override fun showBuyDialog(type: Int) {
        dialog = dialog ?: let {
            var dialog = BottomSheetDialog(mContext)
            var b = DataBindingUtil.inflate<GoodsInfoBottomDialogBinding>(LayoutInflater.from(mContext), R.layout.goods_info_bottom_dialog, null, false)
            b.data = vm
            b.showNum = vm?.showNum
            dialog.setContentView(b.root)
            binding = b
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent)
            dialog
        }
        binding?.root?.find<View>(R.id.buy_now)?.onClick {
            if (type == 0) {
                vm?.addCart()
            } else {
                vm?.buyNow()
            }
        }
        binding?.root?.find<TextView>(R.id.buy_now)?.text = if (type == 0) "加入购物车" else "立即购买"
        dialog?.show()
    }

    override fun initHeadModel(): HeadVm = HeadVm("商品详情", true, R.mipmap.back)

    override fun initView(): View {
        var b = DataBindingUtil.inflate<ActivityGoodsInfoBinding>(LayoutInflater.from(mContext),
                R.layout.activity_goods_info, null, false)
        vm = GoodsInfoVm(this)
        b.data = vm
        return b.root
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflate = menuInflater
        inflate.inflate(R.menu.shopping_cart_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.cart){
         if(PublicStaticData.tooken == "") startActivity<LoginActivity>()  else  startActivity<ShoopCartActivity>()
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        vm?.onDestory()
    }

}
