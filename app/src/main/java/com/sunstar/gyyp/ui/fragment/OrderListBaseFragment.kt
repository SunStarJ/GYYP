package com.sunstar.gyyp.ui.fragment

import android.app.Activity
import android.content.Context
import android.databinding.ObservableArrayList
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.sunstar.gyyp.JavaUtil
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.adapter.OrderListAdapter
import com.sunstar.gyyp.adapter.OrderListChildAdapter
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.DataListener
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.OrderBean
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.databinding.AdapterOrderChildItemBinding
import com.sunstar.gyyp.databinding.AdapterOrderFatherItemBinding
import com.sunstar.gyyp.model.PayModel
import com.sunstar.gyyp.pop.PayPasswordDialog
import com.sunstar.gyyp.pop.PayWaySelectDialog
import com.sunstar.gyyp.ui.CheckOrderInfoActivity
import com.sunstar.gyyp.ui.PaySuccessActivity
import kotlinx.android.synthetic.main.refresh_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast

class OrderListBaseFragment : LazyFragment() {
    override fun initLayoutId(): Int = R.layout.refresh_layout
    var dataList = ObservableArrayList<OrderBean>()
    var page = 1
    var type = 99
    fun initType(type: Int): OrderListBaseFragment {
        this.type = type
        return this
    }

    override fun loadPageView() {
        var adapter = OrderListAdapter(activity as Context).initDataList(dataList)
                .initBindView(object : SSBaseDataBindingAdapter.BindView<AdapterOrderFatherItemBinding> {
                    override fun onBindViewHolder(b: AdapterOrderFatherItemBinding, position: Int) {
                        b.orderData = dataList[position]
                        var data = dataList[position]
                        if (data.cancancel == 1 || data.candelete == 1) {
                            if (data.cancancel == 1) {
                                b.secondText.text = "取消订单"
                            } else if (data.candelete == 1) {
                                b.secondText.text = "删除订单"
                            }
                            b.secondText.visibility = View.VISIBLE
                        } else {
                            b.secondText.visibility = View.GONE
                        }
                        if (data.state == 0|| data.state == 2) {
                            if (data.state == 0) {
                                b.mainText.text = "付款"
                            } else if (data.state == 2) {
                                b.mainText.text = "确认收货"
                            }
                            b.mainText.visibility = View.VISIBLE
                        } else {
                            b.mainText.visibility = View.GONE
                        }
                        b.secondText.onClick {
                            if (data.candelete == 1) {
                                deleteOrderList(position)
                            } else if (data.cancancel == 1) {
                                cacleOrderList(position)
                            }
                        }
                        b.root.onClick {
                            startActivity<CheckOrderInfoActivity>("orderId" to dataList[position].id.toString(), "carState" to dataList[position].state)
                        }
                        b.mainText.onClick {
                            if (data.state == 0) {
                                payOrder(position)
                            } else if (data.canmakesure == 1) {
                                receiveOrder(position)
                            }
                        }
                        var adapter = OrderListChildAdapter(mContext = activity as Context).initDataList(dataList[position].details)
                                .initBindView(object : SSBaseDataBindingAdapter.BindView<AdapterOrderChildItemBinding> {
                                    override fun onBindViewHolder(b: AdapterOrderChildItemBinding, p2: Int) {
                                        b.data = dataList[position].details[p2]
                                        b.root.onClick {
                                            startActivity<CheckOrderInfoActivity>("orderId" to dataList[position].id.toString(), "carState" to dataList[position].state)
                                        }
                                    }
                                })
                        b.innerDataView.adapter = adapter
                        b.innerDataView.layoutManager = LinearLayoutManager(activity as Context)
                        var divider = DividerItemDecoration(activity as Context, DividerItemDecoration.VERTICAL)
                        divider.setDrawable(activity!!.resources.getDrawable(R.drawable.divider_bg))
                        b.innerDataView.addItemDecoration(divider)
                    }
                })
        data_view.adapter = adapter
        data_view.layoutManager = LinearLayoutManager(activity as Context)
        dataList.addOnListChangedCallback(JavaUtil.getListChangedCallback(adapter))
        data_view.addItemDecoration(DividerItemDecoration(activity as Context, DividerItemDecoration.VERTICAL))
        refresh_view.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout?) {
                page += 1
                getData()
            }

            override fun onRefresh(refreshLayout: RefreshLayout?) {
                page = 1
                getData()
            }
        })
        refresh_view.isEnableLoadMore = false
    }

    private fun payOrder(position: Int) {

        var dialog = PayWaySelectDialog(activity as Context, "")
        dialog.initListener(object : PayWaySelectDialog.PayComplete {
            override fun payNow(payWay: Int) {
                when (payWay) {
                    0 -> {
                        var dialog = PayPasswordDialog((activity as Context),dataList[position].orderno,object:PayModel.PayResult{
                            override fun payResult(msg: String, type: Int) {
                                if(type == 0){
                                    dialog.hide()
                                    EventBus.getDefault().post("order_state_change")
                                    EventBus.getDefault().post("getmoney_complete")
                                    startActivity<PaySuccessActivity>()
                                }
                                toast(msg)
                            }
                        })
                        dialog.show()
                    }
                    1 -> {
                        PayModel.aliPayPrepare(dataList[position].orderno, 1, object : DataListener.NetDataListener<String> {
                            override fun success(data: String) {
                                PayModel.aliPay(activity as Activity, data, object : PayModel.PayResult {
                                    override fun payResult(msg: String, type: Int) {
                                        if (type == 9000) {
                                            EventBus.getDefault().post("order_state_change")
                                            EventBus.getDefault().post("getmoney_complete")
                                            startActivity<PaySuccessActivity>()
                                        } else {
                                            toast(msg)
                                        }
                                    }
                                })
                            }

                            override fun error(msg: String) {
                                toast(msg)
                            }
                        })
                    }
                    2 -> {
                        PayModel.wxPreparePay(dataList[position].orderno,1,object : DataListener.NetDataListener<String> {
                            override fun success(data: String) {

                            }

                            override fun error(msg: String) {
                                toast(msg)
                            }
                        },object : PayModel.PayResult {
                            override fun payResult(msg: String, type: Int) {
                                startActivity<PaySuccessActivity>()
                                EventBus.getDefault().post("getmoney_complete")
                                EventBus.getDefault().post("order_state_change")
                                startActivity<PaySuccessActivity>()
                                toast("支付成功")
                            }
                        })
                    }
                }
            }
        })
        dialog.show()


    }

    private fun receiveOrder(position: Int) {
        var mContext = activity as BaseActivity
        dataList[position]?.run {
            alert("请确定收到货物后再进行确定收货哦，否则就会人财两空啦！") {
                negativeButton("确定") {
                    mContext.showLoading("提交数据中，请稍后")
                    OkGo.post<RootBean>(Url.confirmorder)
                            .params("orderid", id)
                            .execute(object : BaseCallBack() {
                                override fun dataError(data: RootBean) {
                                    mContext.hiddenLoading()
                                    mContext.showMsg(data.msg)
                                }

                                override fun success(it: Response<RootBean>) {
                                    mContext.back()
                                    if (type == 99) {
                                        canmakesure = 0
                                        candelete = 1
                                        dataList[position] = this@run
                                    } else {
                                        dataList.removeAt(position)
                                    }
                                    EventBus.getDefault().post("order_state_change")
                                }

                                override fun dataNull() {

                                }
                            })
                }
                positiveButton("取消") {}
            }.show()
        }
    }

    private fun deleteOrderList(position: Int) {
        (activity as BaseActivity).showLoading("提交数据中，请稍候")
        OkGo.post<RootBean>(Url.deleteorder)
                .params("orderid", dataList[position].id)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        (activity as BaseActivity).hiddenLoading()
                        (activity as BaseActivity).showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        (activity as BaseActivity).showMsg(it.body().msg)
                        (activity as BaseActivity).hiddenLoading()
                        dataList.removeAt(position)
                    }

                    override fun dataNull() {

                    }
                })
    }

    private fun cacleOrderList(position: Int) {
        (activity as BaseActivity).showLoading("提交数据中，请稍候")
        OkGo.post<RootBean>(Url.cancelorder)
                .params("orderid", dataList[position].id)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        (activity as BaseActivity).hiddenLoading()
                        (activity as BaseActivity).showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        (activity as BaseActivity).showMsg(it.body().msg)
                        (activity as BaseActivity).hiddenLoading()
                        if (type == 99) {
                            var data = dataList[position]
                            data.state = 5
                            data.cancancel = 0
                            data.candelete = 1
                            dataList[position] = data
                        } else dataList.removeAt(position)

                    }

                    override fun dataNull() {

                    }
                })
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(msg: String) {
        if (msg == "order_state_change")
            refresh_view.autoRefresh()
    }

    fun getData() {
        OkGo.post<RootBean>(Url.getorders)
                .params("type", type)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        (activity as BaseActivity).hiddenLoading()
                        refresh_view?.finishRefresh()
                        toast(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        refresh_view?.finishRefresh()
                        (activity as BaseActivity).hiddenLoading()
                        dataList.clear()
                        for (data in it.body().orders!!) {
                            dataList.add(data)
                        }
                    }

                    override fun dataNull() {
                    }
                })
    }

    override fun lazyLoad() {
        refresh_view.autoRefresh()
    }

}