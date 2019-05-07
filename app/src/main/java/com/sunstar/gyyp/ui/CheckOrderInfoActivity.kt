package com.sunstar.gyyp.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseActivity

class CheckOrderInfoActivity : BaseActivity() {
    override fun appViewInitComplete() {

    }

    override fun initHeadModel(): HeadVm = HeadVm("订单详情",true,R.mipmap.back)

    override fun initView(): View {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_order_info)
    }
}
