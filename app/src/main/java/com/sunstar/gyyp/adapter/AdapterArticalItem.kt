package com.sunstar.gyyp.adapter

import android.content.Context
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.ArticlesItem
import com.sunstar.gyyp.databinding.AdapertAticalItemBinding

class AdapterArticalItem(mContext:Context) :SSBaseDataBindingAdapter<ArticlesItem,AdapertAticalItemBinding>(mContext){
    override fun initLayoutId(): Int = R.layout.adapert_atical_item
}