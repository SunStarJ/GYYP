package com.sunstar.gyyp.vm

import android.databinding.ViewDataBinding
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseMuiltAdapter
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.databinding.AdapterMainPreferenceLayoutBinding

class MainPreferenceVM(var preferenceDatas : MutableList<PreferenceItem> ) : BaseMuiltAdapter.MuiltAdapterBaseData<ViewDataBinding>() {
    override fun initBindingView(): BaseMuiltAdapter.BindView<ViewDataBinding> = object :BaseMuiltAdapter.BindView<ViewDataBinding>{
        override fun onBindViewHolder(b: ViewDataBinding, position: Int) {
            (b as AdapterMainPreferenceLayoutBinding).data = this@MainPreferenceVM
        }
    }

    override fun getViewId(): Int = R.layout.adapter_main_preference_layout



}