package com.sunstar.gyyp.vm

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.ObservableArrayList
import android.view.View
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.gyyp.BigDecimalUtils
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.data.AddressBean
import com.sunstar.gyyp.data.AddressListItem
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.model.PayModel
import com.sunstar.gyyp.pop.PayPasswordDialog
import com.sunstar.gyyp.pop.PayWaySelectDialog
import com.sunstar.gyyp.ui.LocationListActivity
import com.sunstar.gyyp.ui.PaySuccessActivity
import com.sunstar.gyyp.view.OrderView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

class OrderVm(var mv: OrderView) : BaseObservable() {

    var dataList = ObservableArrayList<PreferenceItem>()
    var addressData: AddressListItem? = null
    var addressShowData: AddressBean? = null
    var showPrice = "￥0.0"
    var showPoints = "积分：0.0"
    var shipFee = "￥0.0"
    var shipPoints = "积分：0.0"
    var remark = ""
    var rootBean: RootBean? = null
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(addressData: AddressListItem) {
        this.addressData = addressData
        rootBean!!.address.addressdetail = addressData.province + addressData.city + addressData.area
        rootBean!!.address.addressid = addressData.id.toInt()
        rootBean!!.address.name = addressData.name
        rootBean!!.address.phone = addressData.phone
        notifyChange()
    }

    fun commitOrderData(view: View) {
        var dialog = PayWaySelectDialog(view.context, rootBean!!.productpoint.toString())
        dialog.initListener(object : PayWaySelectDialog.PayComplete {
            override fun payNow(payWay: Int) {
                caluateOrder(payWay,view.context)
            }
        })
        dialog.show()

    }

    private fun caluateOrder(payway: Int,context:Context) {
        mv.showLoading("提交数据中请稍候")
        var commitList = mutableListOf<ShopCartVm.CommitData>()
        for (data in rootBean!!.products!!) {
            var data = ShopCartVm.CommitData(data.shoppingcartid, data.count)
            commitList.add(data)
        }
        OkGo.post<RootBean>(Url.makeorder)
                .params("orderinfo", Gson().toJson(commitList))
                .params("remark", remark)
                .params("addressid", rootBean!!.address.addressid)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        mv.hiddenLoading()
                        mv.showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        mv.hiddenLoading()
                        when (payway) {
                            0 -> {
                                var dialog = PayPasswordDialog(context,it.body().orderno,object: PayModel.PayResult{
                                    override fun payResult(msg: String, type: Int) {
                                        mv.hiddenLoading()
                                        if(type == 0){
                                            EventBus.getDefault().post("order_commit_complete")
                                            context.startActivity<PaySuccessActivity>()
                                            mv.commitComplete()
                                        }
                                        mv.showMsg(msg)
                                    }
                                })
                                dialog.show()
                            }
                            1 -> aliPay(it.body().orderno)
                            2 -> {
                            }
                        }

                    }

                    override fun dataNull() {

                    }
                })
    }

    private fun aliPay(orderNo: String) {
        OkGo.post<RootBean>(Url.alipay)
                .params("datano", orderNo)
                .params("datatype", 1)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        mv.showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        mv.payOrder(it.body().sign)
                    }

                    override fun dataNull() {

                    }
                })
    }

    init {
        EventBus.getDefault().register(this)
    }

    fun onDestory() {
        EventBus.getDefault().unregister(this)
    }

    fun initList(dataList: MutableList<PreferenceItem>) {
        this.dataList.clear()
        for (data in dataList) {
            this.dataList.add(data)
        }
        var totalPrice = 0.0
        var totalPoints = 0.0
        for (data in dataList) {
            totalPrice = BigDecimalUtils.add(totalPrice.toString(), BigDecimalUtils.mul(data.price.toString(), data.num.toString(), 2), 2).toDouble()
            totalPoints = BigDecimalUtils.add(totalPoints.toString(), BigDecimalUtils.mul(data.points.toString(), data.num.toString(), 2), 2).toDouble()
        }
        showPrice = "￥$totalPrice"
        showPoints = "积分：$totalPoints"
        notifyChange()
    }

    fun selectAddressList(view: View) {
        view.context.startActivity<LocationListActivity>("selectType" to 1)
    }

    fun initData(rootBean: RootBean) {
        this.rootBean = rootBean
        dataList.clear()
        for (innerData in rootBean.products!!) {
            dataList.add(innerData)
        }
        addressShowData = rootBean.address
    }


}