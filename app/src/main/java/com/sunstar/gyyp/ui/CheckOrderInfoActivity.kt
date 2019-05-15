package com.sunstar.gyyp.ui

import android.app.Activity
import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.JavaUtil
import com.sunstar.gyyp.R
import com.sunstar.gyyp.adapter.CheckOrderInfoAdapter
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.BaseMuiltAdapter
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.databinding.ActivityCheckOrderInfoBinding
import com.sunstar.gyyp.databinding.ActivityOrderInfoBinding
import com.sunstar.gyyp.databinding.AdapterCheckOrderInfoBinding
import com.sunstar.gyyp.model.PayModel
import com.sunstar.gyyp.view.OrderView
import com.sunstar.gyyp.vm.CheckOrderVm
import kotlinx.android.synthetic.main.activity_check_order_info.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class CheckOrderInfoActivity : BaseActivity(), OrderView {
    override fun commitComplete() {
        finish()
    }

    override fun payOrder(orderNo: String) {
        hiddenLoading()
        PayModel.aliPay(mContext as Activity, orderNo, object : PayModel.PayResult {
            override fun payResult(msg: String, type: Int) {
                if (type == 9000) {
                    EventBus.getDefault().post("order_state_change")
                    startActivity<PaySuccessActivity>()
                    finish()
                } else {
                    toast(msg)
                }
            }
        })
    }

    override fun appViewInitComplete() {
//        showLoading("获取数据中，请稍候")
        vm.getOrderInfoData(intent.getStringExtra("orderId"), intent.getIntExtra("carState", 0))
        var adapter = CheckOrderInfoAdapter(mContext).initDataList(vm.dataList).initBindView(object : SSBaseDataBindingAdapter.BindView<AdapterCheckOrderInfoBinding> {
            override fun onBindViewHolder(b: AdapterCheckOrderInfoBinding, position: Int) {
                b.data = vm?.orderData!!.products!![position]
            }
        }) as CheckOrderInfoAdapter
        vm.dataList.addOnListChangedCallback(JavaUtil.getListChangedCallback(adapter))
        data_view.adapter = adapter
        data_view.layoutManager = LinearLayoutManager(mContext)
        var divider = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        divider.setDrawable(mContext.resources.getDrawable(R.drawable.divider_bg))
        data_view.addItemDecoration(divider)
    }

    override fun initHeadModel(): HeadVm = HeadVm("订单详情", true, R.mipmap.back)

    var vm = CheckOrderVm(this)

    override fun initView(): View {
        var binding = DataBindingUtil.inflate<ActivityCheckOrderInfoBinding>(LayoutInflater.from(mContext)
                , R.layout.activity_check_order_info, null, false)
        binding.data = vm
        return binding.root
    }

    override fun back() {
        finish()
    }
}
