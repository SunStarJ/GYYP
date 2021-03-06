package com.sunstar.gyyp.vm

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseMuiltAdapter
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.data.RecommendsItem
import com.sunstar.gyyp.databinding.AdapterMainListFatherBinding
import com.sunstar.gyyp.databinding.AdapterMainListLayoutBinding
import com.sunstar.gyyp.ui.GoodsInfoActivity
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class MainRecomendVm (var data : ObservableArrayList<PreferenceItem> ,var isFirst :Boolean) : BaseMuiltAdapter.MuiltAdapterBaseData<ViewDataBinding>() {
    override fun initBindingView(): BaseMuiltAdapter.BindView<ViewDataBinding> = object : BaseMuiltAdapter.BindView<ViewDataBinding>{
        override fun onBindViewHolder(b: ViewDataBinding, position: Int) {
            (b as AdapterMainListFatherBinding).data = this@MainRecomendVm
        }
    }

    override fun getViewId(): Int = R.layout.adapter_main_list_father
}