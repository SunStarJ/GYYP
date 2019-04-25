package com.sunstar.gyyp.vm

import android.databinding.ViewDataBinding
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseMuiltAdapter
import com.sunstar.gyyp.data.HotmarketItem
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.databinding.AdapterMainHotMarketBinding
import com.sunstar.gyyp.databinding.AdapterMainPreferenceLayoutBinding

class MainHotmarketVM(var hotMarketList : MutableList<HotmarketItem> ) : BaseMuiltAdapter.MuiltAdapterBaseData<ViewDataBinding>() {
    override fun initBindingView(): BaseMuiltAdapter.BindView<ViewDataBinding> = object :BaseMuiltAdapter.BindView<ViewDataBinding>{
        override fun onBindViewHolder(b: ViewDataBinding, position: Int) {
            (b as AdapterMainHotMarketBinding).data = this@MainHotmarketVM
        }
    }

    override fun getViewId(): Int = R.layout.adapter_main_hot_market



}