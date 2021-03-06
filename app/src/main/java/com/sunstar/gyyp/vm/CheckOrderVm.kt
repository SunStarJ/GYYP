package com.sunstar.gyyp.vm

import android.databinding.BaseObservable
import android.databinding.ObservableArrayList
import android.text.SpannableStringBuilder
import android.view.View
import android.webkit.WebView
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.gyyp.ProjectApplication
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.DataListener
import com.sunstar.gyyp.base.Util
import com.sunstar.gyyp.data.Product
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.data.RootBeanX
import com.sunstar.gyyp.model.OrderControlModel
import com.sunstar.gyyp.model.PayModel
import com.sunstar.gyyp.pop.PayPasswordDialog
import com.sunstar.gyyp.pop.PayWaySelectDialog
import com.sunstar.gyyp.ui.PaySuccessActivity
import com.sunstar.gyyp.view.OrderView
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity

class CheckOrderVm(var mv: OrderView):BaseObservable() {
    var showSatate: SpannableStringBuilder? = null
    var showShipNo = "订单编号："
    var createTime = "创建时间："
    var shipWay = "货运方式："
    var remark = "备注："
    var fp = ""
    var model = OrderControlModel()
    var orderData:RootBeanX?=null
    var isTrace = false
    var dataList = ObservableArrayList<Product>()
    fun getOrderInfoData(orderId: String,carState:Int,view:WebView) {
//        mv.showLoading("提交数据中，请稍后")
        model.getOrderInfo(orderId, object : DataListener.NetDataListener<RootBeanX> {
            override fun success(data: RootBeanX) {
                orderData = data
                orderData?.state = carState
                mv.hiddenLoading()
                var state = when (data.state) {
                    0 -> "未付款"
                    1 -> "待发货"
                    2 -> "待收货"
                    3 -> "已完成"
                    5 -> "已取消"
                    6 -> "已签收"
                    else -> ""
                }
                for(data in orderData?.products!!){
                    dataList.add(data)
                }
                view.loadDataWithBaseURL(null,orderData?.trace,"text/html", "utf-8", null);
                showSatate = Util.changeStringColor(ProjectApplication.instance.applicationContext, "订单状态：${state}", state, R.color.color_red)
                showShipNo = "订单编号：${data.orderno}"
                createTime = "创建时间：${data.addtime}"
                shipWay = "货运方式：${data.sendway}"
                remark = "备注：${data.usercontent}"
                isTrace = !data.trace.isEmpty()
                notifyChange()
            }

            override fun error(msg: String) {
                mv.hiddenLoading()
                mv.showMsg(msg)
            }
        })
    }

    fun secondClick(){

        orderData?.run {
            if(cancancel == 1){
                mv.showLoading("提交数据中，请稍后")
                OkGo.post<RootBean>(Url.cancelorder)
                        .params("orderid",orderid)
                        .execute(object :BaseCallBack(){
                            override fun dataError(data: RootBean) {
                                mv.hiddenLoading()
                                mv.showMsg(data.msg)
                            }

                            override fun success(it: Response<RootBean>) {
                                mv.back()
                                EventBus.getDefault().post("order_state_change")
                            }

                            override fun dataNull() {
                            }
                        })
            }else if(candelete == 1){
                mv.showLoading("提交数据中，请稍后")
                OkGo.post<RootBean>(Url.deleteorder)
                        .params("orderid",orderid)
                        .execute(object :BaseCallBack(){
                            override fun dataError(data: RootBean) {
                                mv.hiddenLoading()
                                mv.showMsg(data.msg)
                            }

                            override fun success(it: Response<RootBean>) {
                                mv.back()
                                EventBus.getDefault().post("order_state_change")
                            }

                            override fun dataNull() {
                            }
                        })
            }
        }
    }
    fun mainClick(view: View){
        orderData?.run {
            if(state == 0){
                var dialog = PayWaySelectDialog(view.context,orderData?.orderno.toString())
                dialog.initListener(object:PayWaySelectDialog.PayComplete{
                    override fun payNow(payWay: Int) {
                        if(payWay == 1){
                           PayModel.aliPayPrepare(orderData!!.orderno,1,object:DataListener.NetDataListener<String>{
                               override fun success(data: String) {
                                   mv.payOrder(data)
                               }

                               override fun error(msg: String) {
                                   mv.showMsg(msg)
                               }
                           })
                        }else if(payWay == 0){
                            var context = view.context
                            var dialog = PayPasswordDialog(context,orderData!!.orderno,object: PayModel.PayResult{
                                override fun payResult(msg: String, type: Int) {
                                    mv.hiddenLoading()
                                    if(type == 0){
                                        EventBus.getDefault().post("order_state_change")
                                        context.startActivity<PaySuccessActivity>()
                                        mv.commitComplete()
                                    }
                                    mv.showMsg(msg)
                                }
                            })
                            dialog.show()
                        }else if(payWay == 2){
                            PayModel.wxPreparePay(orderData!!.orderno,1,object:DataListener.NetDataListener<String>{
                                override fun success(data: String) {
                                }

                                override fun error(msg: String) {
                                    mv.showMsg(msg)
                                }
                            },object:PayModel.PayResult{
                                override fun payResult(msg: String, type: Int) {
                                    EventBus.getDefault().post("order_state_change")
                                    view.context.startActivity<PaySuccessActivity>()
                                    mv.commitComplete()
                                }
                            })
                        }
                    }
                }).show()
            }else if(state == 2){
                view.context.alert ("请确定收到货物后再进行确定收货哦，否则就会人财两空啦！"){
                    negativeButton("确定") {
                        mv.showLoading("提交数据中，请稍后")
                        OkGo.post<RootBean>(Url.confirmorder)
                                .params("orderid",orderid)
                                .execute(object :BaseCallBack(){
                                    override fun dataError(data: RootBean) {
                                        mv.hiddenLoading()
                                        mv.showMsg(data.msg)
                                    }

                                    override fun success(it: Response<RootBean>) {
                                        mv.back()
                                        EventBus.getDefault().post("order_state_change")
                                    }

                                    override fun dataNull() {

                                    }
                                })
                    }
                    positiveButton("取消"){}
                }.show()
            }
        }

    }
}