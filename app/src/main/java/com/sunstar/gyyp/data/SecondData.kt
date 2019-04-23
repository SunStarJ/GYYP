package com.sunstar.gyyp.data

import android.databinding.ViewDataBinding
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseMuiltAdapter
import com.sunstar.gyyp.base.BaseView
import com.sunstar.gyyp.databinding.AdapterMainBannerLayoutBinding
import com.sunstar.gyyp.databinding.AdapterMainSecondLayoutBinding

class SecondData<T:ViewDataBinding>: BaseMuiltAdapter.MuiltAdapterBaseData<T>() {
    override fun initBindingView(): BaseMuiltAdapter.BindView<T> =
            object :  BaseMuiltAdapter.BindView<T> {
                override fun onBindViewHolder(b: T, position: Int) {
                    (b as AdapterMainSecondLayoutBinding).data = this@SecondData
                }
            }
    var name = "2"
    override fun getViewId(): Int = R.layout.adapter_main_second_layout
}