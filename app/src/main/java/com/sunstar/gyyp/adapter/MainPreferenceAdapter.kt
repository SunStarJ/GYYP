package com.sunstar.gyyp.adapter

import android.content.Context
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.databinding.AdapterPerferenceInnerAdapterBinding

class MainPreferenceAdapter(mContext:Context):SSBaseDataBindingAdapter<PreferenceItem,AdapterPerferenceInnerAdapterBinding>(mContext) {
    override fun initLayoutId(): Int = R.layout.adapter_perference_inner_adapter
}