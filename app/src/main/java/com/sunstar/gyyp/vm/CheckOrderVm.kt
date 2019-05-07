package com.sunstar.gyyp.vm

import android.databinding.BaseObservable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.gyyp.ProjectApplication
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.BaseView
import com.sunstar.gyyp.base.DataListener
import com.sunstar.gyyp.base.Util
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.data.RootBeanX
import com.sunstar.gyyp.model.OrderControlModel

class CheckOrderVm(var mv: BaseView):BaseObservable() {
    var showSatate: SpannableStringBuilder? = null
    var showShipNo = "订单编号："
    var createTime = "创建时间："
    var shipWay = "货运方式："
    var remark = "备注："
    var fp = ""
    var model = OrderControlModel()
    var orderData:RootBeanX?=null
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

}