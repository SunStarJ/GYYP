package com.sunstar.gyyp.adapter

import android.content.Context
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.RecommendsItem
import com.sunstar.gyyp.vm.MainRecomendVm
import com.sunstar.gyyp.databinding.AdapterMainListFatherBinding
import com.sunstar.gyyp.databinding.AdapterMainListLayoutBinding

class MainGridAdapter(mContext: Context) : SSBaseDataBindingAdapter<RecommendsItem, AdapterMainListLayoutBinding>(mContext){
    override fun initLayoutId(): Int = R.layout.adapter_main_list_layout
}