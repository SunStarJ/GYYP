package com.sunstar.gyyp.adapter

import android.content.Context
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.OrderBean
import com.sunstar.gyyp.databinding.AdapterOrderFatherItemBinding

class OrderListAdapter(mContext:Context):SSBaseDataBindingAdapter<OrderBean,AdapterOrderFatherItemBinding>(mContext) {
    override fun initLayoutId(): Int = R.layout.adapter_order_father_item
}