package com.sunstar.gyyp.view

import com.sunstar.gyyp.base.BaseView

interface OrderView :BaseView{
    fun commitComplete()
    fun payOrder(orderNo:String)
}