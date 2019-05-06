package com.sunstar.gyyp.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.adapter.VpBodyAdapter
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.ui.fragment.OrderListBaseFragment
import kotlinx.android.synthetic.main.activity_order_list_data.*

class OrderListDataActivity : BaseActivity() {
    var pageIndex = 0
    override fun appViewInitComplete() {
        pageIndex = intent.getIntExtra("pageInde",0)
        var titelList = mutableListOf<String>("全部","待付款","待发货","待收货","已完成")
        var fragmentList = mutableListOf<Fragment>(
                OrderListBaseFragment().initType(99),
                OrderListBaseFragment().initType(0),
                OrderListBaseFragment().initType(1),
                OrderListBaseFragment().initType(2),
                OrderListBaseFragment().initType(3)
        )
        var pagerAdapter = VpBodyAdapter(titelList,fragmentList,supportFragmentManager)
        vp_body.adapter = pagerAdapter
        title_tab.setupWithViewPager(vp_body)
        vp_body.currentItem = pageIndex
    }
    override fun initHeadModel(): HeadVm = HeadVm("我的订单",true,R.mipmap.back)

    override fun initView(): View = View.inflate(mContext,R.layout.activity_order_list_data,null)
}
