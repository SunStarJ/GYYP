package com.sunstar.gyyp.vm

import android.databinding.ViewDataBinding
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseMuiltAdapter
import com.sunstar.gyyp.data.ControlData
import com.sunstar.gyyp.databinding.AdapterMainControlBinding
class MainControlVm :BaseMuiltAdapter.MuiltAdapterBaseData<ViewDataBinding>(){
    override fun initBindingView(): BaseMuiltAdapter.BindView<ViewDataBinding> = object : BaseMuiltAdapter.BindView<ViewDataBinding>{
        override fun onBindViewHolder(b: ViewDataBinding, position: Int) {
            (b as AdapterMainControlBinding).data = this@MainControlVm
        }
    }

    var controlList = mutableListOf<ControlData>()

    fun initControlList(dataList:MutableList<ControlData>):MainControlVm{
        controlList.clear()
        controlList.addAll(dataList)
        return this
    }

    override fun getViewId(): Int = R.layout.adapter_main_control

}