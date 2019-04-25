package com.sunstar.gyyp.adapter

import android.content.Context
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.HotmarketItem
import com.sunstar.gyyp.databinding.AdapterMainHotmarktInnerBinding
class MainHotmarketItemAdapter(mContext:Context):SSBaseDataBindingAdapter<HotmarketItem,AdapterMainHotmarktInnerBinding>(mContext) {
    override fun initLayoutId(): Int = R.layout.adapter_main_hotmarkt_inner
}