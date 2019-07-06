package com.sunstar.gyyp.adapter

import android.content.Context
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.FinanaceLogData
import com.sunstar.gyyp.databinding.AdapterCostOrReceiveHistoryBinding

class PointHistoryAdapter(var ctx: Context) : SSBaseDataBindingAdapter<FinanaceLogData, AdapterCostOrReceiveHistoryBinding>(ctx) {
    override fun initLayoutId(): Int = R.layout.adapter_cost_or_receive_history
}