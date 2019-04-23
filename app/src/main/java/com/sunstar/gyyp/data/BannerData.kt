package com.sunstar.gyyp.data

import android.databinding.ViewDataBinding
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseMuiltAdapter
import com.sunstar.gyyp.base.BaseView
import com.sunstar.gyyp.databinding.AdapterMainBannerLayoutBinding

class BannerData<T:ViewDataBinding>: BaseMuiltAdapter.MuiltAdapterBaseData<T>() {
    override fun initBindingView(): BaseMuiltAdapter.BindView<T> =
            object :  BaseMuiltAdapter.BindView<T> {
                override fun onBindViewHolder(b: T, position: Int) {
                    (b as AdapterMainBannerLayoutBinding).data = this@BannerData
                }
            }
    var name = "1"
    override fun getViewId(): Int = R.layout.adapter_main_banner_layout
}