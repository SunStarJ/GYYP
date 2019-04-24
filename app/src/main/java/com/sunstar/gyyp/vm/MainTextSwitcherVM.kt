package com.sunstar.gyyp.vm

import android.databinding.ViewDataBinding
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseMuiltAdapter
import com.sunstar.gyyp.data.ArticlesItem
import com.sunstar.gyyp.databinding.AdapterMainTextswitcherLayoutBinding

class MainTextSwitcherVM<T: ViewDataBinding>: BaseMuiltAdapter.MuiltAdapterBaseData<T>(){
    override fun initBindingView(): BaseMuiltAdapter.BindView<T> = object:BaseMuiltAdapter.BindView<T>{
        override fun onBindViewHolder(b: T, position: Int) {
            (b as AdapterMainTextswitcherLayoutBinding).data = this@MainTextSwitcherVM
        }
    }

    var adList = mutableListOf<ArticlesItem>()

    fun initAdList (list:MutableList<ArticlesItem>):MainTextSwitcherVM<T>{
        adList.clear()
        adList.addAll(list)
        return this
    }

    override fun getViewId(): Int = R.layout.adapter_main_textswitcher_layout

}