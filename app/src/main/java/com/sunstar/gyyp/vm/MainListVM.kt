package com.sunstar.gyyp.vm

import android.databinding.ViewDataBinding
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseMuiltAdapter
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.databinding.AdapterMainListLayoutBinding
class MainListVM(var preferenceData : PreferenceItem) : BaseMuiltAdapter.MuiltAdapterBaseData<ViewDataBinding>() {
    override fun initBindingView(): BaseMuiltAdapter.BindView<ViewDataBinding> = object :BaseMuiltAdapter.BindView<ViewDataBinding>{
        override fun onBindViewHolder(b: ViewDataBinding, position: Int) {
            (b as AdapterMainListLayoutBinding).data = this@MainListVM
        }
    }

    override fun getViewId(): Int = R.layout.adapter_main_list_layout



}