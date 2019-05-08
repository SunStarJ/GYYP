package com.sunstar.gyyp.vm

import android.databinding.BaseObservable
import android.databinding.ObservableArrayList
import android.provider.DocumentsContract
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.view.View
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.gyyp.ProjectApplication
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.BaseView
import com.sunstar.gyyp.base.DataListener
import com.sunstar.gyyp.base.Util
import com.sunstar.gyyp.data.Product
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.data.RootBeanX
import com.sunstar.gyyp.model.OrderControlModel
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.alert

class CheckOrderVm(var mv: BaseView):BaseObservable() {
    var showSatate: SpannableStringBuilder? = null
    var showShipNo = "订单编号："
    var createTime = "创建时间："
    var shipWay = "货运方式："
    var remark = "备注："
    var fp = ""
    var model = OrderControlModel()
    var orderData:RootBeanX?=null
    var dataList = ObservableArrayList<Product>()
    fun getOrderInfoData(orderId: String) {
        mv.showLoading("提交数据中，请稍后")
        model.getOrderInfo(orderId, object : DataListener.NetDataListener<RootBeanX> {
            override fun success(data: RootBeanX) {
                orderData = data
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
                showSatate = Util.changeStringColor(ProjectApplication.instance.applicationContext, "订单状态：${state}", state, R.color.color_red)
                showShipNo = "订单编号：${data.orderno}"
                createTime = "创建时间：${data.addtime}"
                shipWay = "货运方式：${data.sendway}"
                remark = "备注：${data.usercontent}"
                notifyChange()
            }

            override fun error(msg: String) {
                mv.hiddenLoading()
                mv.showMsg(msg)
            }
        })
    }

    fun secondClick(){
        mv.showLoading("提交数据中，请稍后")
        orderData?.run {
            if(cancancel == 1){
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
                mv.showMsg("支付")
            }else if(canmakesure == 1){

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
                }
            }
        }

    }
}