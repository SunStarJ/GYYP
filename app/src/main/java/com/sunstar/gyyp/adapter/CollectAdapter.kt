package com.sunstar.gyyp.adapter

import android.content.Context
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.databinding.AdapterMyCollectLatoutBinding

class CollectAdapter(mContext:Context) :SSBaseDataBindingAdapter<PreferenceItem,AdapterMyCollectLatoutBinding>(mContext){
    override fun initLayoutId(): Int = R.layout.adapter_my_collect_latout
}