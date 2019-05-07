package com.sunstar.gyyp.ui

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.databinding.ActivityCheckOrderInfoBinding
import com.sunstar.gyyp.databinding.ActivityOrderInfoBinding
import com.sunstar.gyyp.vm.CheckOrderVm

class CheckOrderInfoActivity : BaseActivity() {
    override fun appViewInitComplete() {
        vm.getOrderInfoData(intent.getStringExtra("orderId"))
    }

    override fun initHeadModel(): HeadVm = HeadVm("订单详情", true, R.mipmap.back)

    var vm = CheckOrderVm(this)

    override fun initView(): View {
        var binding = DataBindingUtil.inflate<ActivityCheckOrderInfoBinding>(LayoutInflater.from(mContext)
                , R.layout.activity_check_order_info, null, false)
        binding.data  = vm
        return binding.root
    }
}
