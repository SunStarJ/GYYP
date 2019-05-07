package com.sunstar.gyyp.adapter

import android.content.Context
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.DetailBean
import com.sunstar.gyyp.databinding.AdapterOrderChildItemBinding

class OrderListChildAdapter(mContext: Context) : SSBaseDataBindingAdapter<DetailBean, AdapterOrderChildItemBinding>(mContext) {
    override fun initLayoutId(): Int = R.layout.adapter_order_child_item
}