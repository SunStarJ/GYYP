package com.sunstar.gyyp.adapter

import android.content.Context
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.AddressListItem
import com.sunstar.gyyp.databinding.AdapterLocationListBinding

class AddressAdapter(mContext:Context):SSBaseDataBindingAdapter<AddressListItem,AdapterLocationListBinding>(mContext) {
    override fun initLayoutId(): Int = R.layout.adapter_location_list
}