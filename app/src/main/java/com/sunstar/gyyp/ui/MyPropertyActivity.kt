package com.sunstar.gyyp.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.adapter.MainControlAdapter
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.ControlData
import com.sunstar.gyyp.databinding.AdapterMainControlBinding
import com.sunstar.gyyp.databinding.AdapterMainControlInnerAdapterBinding
import kotlinx.android.synthetic.main.activity_my_property.*
import kotlinx.android.synthetic.main.activity_order_info.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class MyPropertyActivity : BaseActivity() {
    override fun appViewInitComplete() {
        count_show.text = "${intent.getStringExtra("accumulatepoint")}\n\n总资产（积分）"
        var dataList = mutableListOf<ControlData>()
        dataList.add(ControlData("资产记录",R.mipmap.zzichanjilu))
        dataList.add(ControlData("申请提现",R.mipmap.shenqingtixian))
        dataList.add(ControlData("账户充值",R.mipmap.zhanghuchongzhi))
        dataList.add(ControlData("帮助中心",R.mipmap.bangzhuzhongxin))
        var adapter = MainControlAdapter(mContext).initDataList(dataList).initBindView(object : SSBaseDataBindingAdapter.BindView<AdapterMainControlInnerAdapterBinding>{
            override fun onBindViewHolder(b: AdapterMainControlInnerAdapterBinding, position: Int) {
                b.data = dataList[position]
                b.root.onClick {
                    when(position){
                        0->{
                            startActivity<PointsHistoryActivity>()
                        }
                        1->{
                            startActivity<GetCashWayActivity>("leftpoint" to intent.getStringExtra("leftpoints"))
                        }
                        2->{
                            startActivity<InvestPointsActivity>()
                        }
                        3->{}
                    }
                }
            }
        }) as MainControlAdapter
        control_view.adapter = adapter
        control_view.layoutManager = GridLayoutManager(mContext,3)

    }

    override fun initHeadModel(): HeadVm = HeadVm("我的资产",true,R.mipmap.back)

    override fun initView(): View = View.inflate(mContext,R.layout.activity_my_property,null)

}
