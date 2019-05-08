package com.sunstar.gyyp.adapter

import android.content.Context
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.Product
import com.sunstar.gyyp.databinding.AdapterCheckOrderInfoBinding

class CheckOrderInfoAdapter(mContext: Context) : SSBaseDataBindingAdapter<Product, AdapterCheckOrderInfoBinding>(mContext) {
    override fun initLayoutId(): Int = R.layout.adapter_check_order_info
}