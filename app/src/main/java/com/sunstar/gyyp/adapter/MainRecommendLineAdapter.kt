package com.sunstar.gyyp.adapter

import android.content.Context
import android.databinding.ViewDataBinding
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseMuiltAdapter
import com.sunstar.gyyp.data.RecommendsItem
import com.sunstar.gyyp.databinding.AdapterRecommendLineBinding
import com.sunstar.gyyp.ui.GoodsInfoActivity
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class MainRecommendLineAdapter(var data : RecommendsItem):  BaseMuiltAdapter.MuiltAdapterBaseData<ViewDataBinding>(){

    override fun initBindingView(): BaseMuiltAdapter.BindView<ViewDataBinding> = object: BaseMuiltAdapter.BindView<ViewDataBinding>{
        override fun onBindViewHolder(b: ViewDataBinding, position: Int) {
            (b as AdapterRecommendLineBinding).data = data
            b.root.onClick {
                b.root.context.startActivity<GoodsInfoActivity>("id" to data.id)
            }
        }
    }

    override fun getViewId(): Int = R.layout.adapter_recommend_line
}