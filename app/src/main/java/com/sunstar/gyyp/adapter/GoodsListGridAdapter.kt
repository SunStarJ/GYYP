package com.sunstar.gyyp.adapter

import android.content.Context
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.databinding.AdapterGoodsGridListBinding

class GoodsListGridAdapter(mConext:Context):SSBaseDataBindingAdapter<PreferenceItem,AdapterGoodsGridListBinding>(mConext) {
    override fun initLayoutId(): Int = R.layout.adapter_goods_grid_list
}