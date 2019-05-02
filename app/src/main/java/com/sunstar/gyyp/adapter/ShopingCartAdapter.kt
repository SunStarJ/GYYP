package com.sunstar.gyyp.adapter

import android.content.Context
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.databinding.AdapterShopCartBinding

class ShopingCartAdapter(mContext:Context):SSBaseDataBindingAdapter<PreferenceItem,AdapterShopCartBinding> (mContext){
    override fun initLayoutId(): Int  = R.layout.adapter_shop_cart

}