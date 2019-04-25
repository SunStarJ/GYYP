package com.sunstar.gyyp.vm

import android.databinding.ViewDataBinding
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseMuiltAdapter
import  com.sunstar.gyyp.databinding.AdapterMainImageLayoutBinding

class MainImageAdapterVM(var imagePath:String): BaseMuiltAdapter.MuiltAdapterBaseData<ViewDataBinding>(){

    override fun initBindingView(): BaseMuiltAdapter.BindView<ViewDataBinding> = object:BaseMuiltAdapter.BindView<ViewDataBinding>{
        override fun onBindViewHolder(b: ViewDataBinding, position: Int) {
            (b as AdapterMainImageLayoutBinding).data = this@MainImageAdapterVM
        }
    }

    override fun getViewId(): Int = R.layout.adapter_main_image_layout
}