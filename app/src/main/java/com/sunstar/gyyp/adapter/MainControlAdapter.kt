package com.sunstar.gyyp.adapter

import android.content.Context
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.ControlData
import com.sunstar.gyyp.databinding.AdapterMainControlInnerAdapterBinding

class MainControlAdapter(mContext:Context):SSBaseDataBindingAdapter<ControlData,AdapterMainControlInnerAdapterBinding>(mContext) {
    override fun initLayoutId(): Int = R.layout.adapter_main_control_inner_adapter
}