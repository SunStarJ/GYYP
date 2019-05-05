package com.sunstar.gyyp.view

import com.sunstar.gyyp.base.BaseView

interface GoodsInfoView : BaseView {
    fun showBanner(banners: MutableList<String>)
    fun loadInfo(url: String)
    fun showBuyDialog(type:Int)
    fun showPrice()
    fun hideBottom()
    fun buyNow()
}