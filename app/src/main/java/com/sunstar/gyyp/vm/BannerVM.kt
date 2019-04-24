package com.sunstar.gyyp.vm

import android.databinding.ViewDataBinding
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseMuiltAdapter
import com.sunstar.gyyp.data.BannerItem
import com.sunstar.gyyp.databinding.AdapterMainBannerLayoutBinding

class BannerVM<T:ViewDataBinding>: BaseMuiltAdapter.MuiltAdapterBaseData<T>() {
    override fun initBindingView(): BaseMuiltAdapter.BindView<T> =
            object :  BaseMuiltAdapter.BindView<T> {
                override fun onBindViewHolder(b: T, position: Int) {
                    (b as AdapterMainBannerLayoutBinding).data = this@BannerVM
                }
            }

    var banners = mutableListOf<BannerItem>()

    fun initBanner(banners:MutableList<BannerItem>): BannerVM<T> {
        this.banners.clear()
        this.banners.addAll(banners)
        return this
    }

    override fun getViewId(): Int = R.layout.adapter_main_banner_layout
}