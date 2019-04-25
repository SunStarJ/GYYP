package com.sunstar.gyyp.adapter

import android.content.Context
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.databinding.AdapterSingletextViewBinding
class UserCenterAdapter(mContext: Context):SSBaseDataBindingAdapter<String,AdapterSingletextViewBinding> (mContext){
    override fun initLayoutId(): Int = R.layout.adapter_singletext_view
}