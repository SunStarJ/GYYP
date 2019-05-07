package com.sunstar.gyyp.view

import com.sunstar.gyyp.base.BaseView
import com.sunstar.gyyp.data.RootBean

interface ShopCartView:BaseView {
    fun goToOrederPayPage(data:RootBean)
    fun showAllSelect(select: Boolean)
}