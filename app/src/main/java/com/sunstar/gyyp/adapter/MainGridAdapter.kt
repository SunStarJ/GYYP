package com.sunstar.gyyp.adapter

import android.content.Context
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.data.RecommendsItem
import com.sunstar.gyyp.databinding.AdapterGoodsGridListBinding
import com.sunstar.gyyp.vm.MainRecomendVm
import com.sunstar.gyyp.databinding.AdapterMainListFatherBinding
import com.sunstar.gyyp.databinding.AdapterMainListLayoutBinding

class MainGridAdapter(mContext: Context) : SSBaseDataBindingAdapter<PreferenceItem, AdapterGoodsGridListBinding>(mContext){
    override fun initLayoutId(): Int = R.layout.adapter_goods_grid_list
}