package com.sunstar.gyyp.ui

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
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
import com.sunstar.gyyp.databinding.GoodsInfoBottomDialogBinding
import com.sunstar.gyyp.vm.ShopCartVm
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class GoodsInfoActivity : BaseActivity(), GoodsInfoView {
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
            startActivity<ShoopCartActivity>()
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        vm?.onDestory()
    }

}
