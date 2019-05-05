package com.sunstar.gyyp.adapter

import android.content.Context
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.databinding.AdapterOrderInfoGoodsBinding

class OrderInfoGoodsAdapter(mContext:Context):SSBaseDataBindingAdapter<PreferenceItem,AdapterOrderInfoGoodsBinding>(mContext) {
    override fun initLayoutId(): Int = R.layout.adapter_order_info_goods
}