package com.sunstar.gyyp.ui

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.MainActivity
import com.sunstar.gyyp.R
import com.sunstar.gyyp.adapter.MainControlAdapter
import com.sunstar.gyyp.adapter.UserCenterAdapter
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.BaseMuiltAdapter
import com.sunstar.gyyp.base.BaseView
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.ControlData
import com.sunstar.gyyp.data.PublicStaticData
import com.sunstar.gyyp.databinding.ActivityVipCenterBinding
import com.sunstar.gyyp.databinding.AdapterMainControlBinding
import com.sunstar.gyyp.databinding.AdapterMainControlInnerAdapterBinding
import com.sunstar.gyyp.databinding.AdapterSingletextViewBinding
import com.sunstar.gyyp.vm.UserVm
import kotlinx.android.synthetic.main.activity_user_center.*
import kotlinx.android.synthetic.main.activity_vip_center.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class VipCenterActivity : BaseActivity() {
    var vm : UserVm<BaseView>?=null
    var controlList = mutableListOf<ControlData>()
    var secondControlList = mutableListOf<String>("我的资产", "我的订单", "我的收藏", "积分兑换", "积分充值")
    override fun appViewInitComplete() {
        vm?.getUserData()
        controlList.add(ControlData("待付款",R.mipmap.daifukuan))
        controlList.add(ControlData("待发货",R.mipmap.daifahuo))
        controlList.add(ControlData("待收获",R.mipmap.daishouhuo))
        controlList.add(ControlData("已完成",R.mipmap.yiwancheng))
        control_view.adapter = MainControlAdapter(mContext).initDataList(controlList)
                .initBindView(object:SSBaseDataBindingAdapter.BindView<AdapterMainControlInnerAdapterBinding> {
                    override fun onBindViewHolder(b: AdapterMainControlInnerAdapterBinding, position: Int) {
                        b.data = controlList[position]
                        b.root.onClick {
                            startActivity<OrderListDataActivity>("pageInde" to position+1)
                        }
                    }
                })
        control_view.layoutManager = GridLayoutManager(mContext,4)
        check_order.onClick {
            startActivity<OrderListDataActivity>()
        }

        var secondAdapter = UserCenterAdapter(mContext).initDataList(secondControlList).initBindView(object : SSBaseDataBindingAdapter.BindView<AdapterSingletextViewBinding> {
            override fun onBindViewHolder(b: AdapterSingletextViewBinding, position: Int) {
                b.name = secondControlList[position]
                b.root.onClick {
                    when (position) {
                        0 -> {
                            startActivity<MyPropertyActivity>("accumulatepoint" to vm?.user?.accumulatepoint,"leftpoints" to vm?.user?.leftpoint)
                        }
                        1 -> {
                            startActivity<OrderListDataActivity>()
                        }
                        2 -> {
                            startActivity<MyCollectActivity>()
                        }
                        3 -> {
                            startActivity<GetCashWayActivity>("leftpoint" to vm?.user?.leftpoint)
                        }
                        4 -> {
                            startActivity<InvestPointsActivity>("leftpoint" to vm?.user?.leftpoint)
                        }
                        5 -> {
                            startActivity<RecommendMainActivity>("userData" to vm!!.user)
                        }
                    }
                }
            }
        }) as UserCenterAdapter
        second_view.adapter = secondAdapter
        second_view.layoutManager = LinearLayoutManager(mContext)
        second_view.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
        top_layout.onClick {
            startActivity<UserCenterActivity>()
        }
    }

    @Subscribe(threadMode =  ThreadMode.MAIN)
    fun onMessageEvent(msg:String){
        if(msg == "getmoney_complete"){
            vm?.getUserData()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }


    override fun initHeadModel(): HeadVm  = HeadVm("会员中心",true,R.mipmap.back)

    override fun initView(): View {
        var b = DataBindingUtil.inflate<ActivityVipCenterBinding>(LayoutInflater.from(mContext)
                ,R.layout.activity_vip_center,null,false)
        vm = UserVm(this)
        b.userData = vm
        return b.root
    }
}
