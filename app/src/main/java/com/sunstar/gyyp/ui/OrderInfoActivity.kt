package com.sunstar.gyyp.ui

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.google.gson.Gson
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.adapter.OrderInfoGoodsAdapter
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.BaseMuiltAdapter
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.databinding.ActivityOrderInfoBinding
import com.sunstar.gyyp.databinding.AdapterOrderInfoGoodsBinding
import com.sunstar.gyyp.vm.OrderVm
import kotlinx.android.synthetic.main.activity_order_info.*

class OrderInfoActivity : BaseActivity() {
    var vm = OrderVm()
    var adapter:OrderInfoGoodsAdapter?=null
    override fun appViewInitComplete() {
        vm.initList(Gson().fromJson<Array<PreferenceItem>>(intent.getStringExtra("dataList"), Array<PreferenceItem>::class.java).toMutableList())
        adapter = OrderInfoGoodsAdapter(mContext).initDataList(vm?.dataList).initBindView(object:SSBaseDataBindingAdapter.BindView<AdapterOrderInfoGoodsBinding>{
            override fun onBindViewHolder(b: AdapterOrderInfoGoodsBinding, position: Int) {
                b.data = vm.dataList[position]
            }
        }) as OrderInfoGoodsAdapter
        data_view.adapter = adapter
        data_view.layoutManager = LinearLayoutManager(mContext)
        data_view.addItemDecoration(DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL))

    }

    override fun initHeadModel(): HeadVm = HeadVm("订单", true, R.mipmap.back)
    override fun initView(): View {
        var binding = DataBindingUtil.inflate<ActivityOrderInfoBinding>(LayoutInflater.from(mContext), R.layout.activity_order_info, null, false)
        binding.data = vm
        return binding.root
    }
}