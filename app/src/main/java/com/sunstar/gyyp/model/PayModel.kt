package com.sunstar.gyyp.model

import android.annotation.SuppressLint
import android.app.Activity
import com.alipay.sdk.app.PayTask
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.gyyp.ProjectApplication
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.DataListener
import com.sunstar.gyyp.data.RootBean
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

object PayModel :AnkoLogger{

    fun destroy(){
        EventBus.getDefault().unregister(this)
    }

    fun initE(){
        EventBus.getDefault().register(this)
    }
    var listener:PayResult?=null
    @Subscribe(threadMode =  ThreadMode.MAIN)
    fun onMessageEvent(msg:String){
        if(msg == "paySuccess"){
            info { "paySuccess" }
            listener?.payResult("",0)
            listener = null
        }
    }

    @SuppressLint("CheckResult")
    fun aliPay(activity: Activity, orderInfo: String, resultListener: PayResult) {
        Flowable.just(1).map {
            var aliPay = PayTask(activity)
            aliPay.payV2(orderInfo, true)
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            when (it["resultStatus"]!!) {
                "9000" -> {
                    resultListener.payResult("支付成功", 9000)
                }
                "4000" -> {
                    resultListener.payResult("支付失败，订单已生成，请去我的订单中继续支付", 4000)
                }
            }
        }
    }

    fun aliPayPrepare(orderNo: String, type: Int, dataListener: DataListener.NetDataListener<String>) {
        OkGo.post<RootBean>(Url.alipay)
                .params("datano", orderNo)
                .params("datatype", type)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        dataListener.error(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        dataListener.success(it.body().sign)
                    }

                    override fun dataNull() {
                    }
                })
    }


    fun weChatPay(appid: String, partnerId: String, prepayId: String, nonceStr: String, timeStamp: String, sign: String) {
        var request = PayReq()
        request.appId = appid
        request.partnerId = partnerId
        request.prepayId = prepayId
        request.packageValue = "Sign=WXPay"
        request.nonceStr = nonceStr
        request.timeStamp = timeStamp
        request.sign = sign
        ProjectApplication.wxApi.sendReq(request)
    }

    fun wxPreparePay(orderNo: String, type: Int, dataListener: DataListener.NetDataListener<String>,listener:PayResult) {
        OkGo.post<RootBean>(Url.wxpay)
                .params("datano", orderNo)
                .params("datatype", type)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        dataListener.error(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        this@PayModel.listener = listener
                        it.body().result.run {
                            weChatPay(appid,partnerid,prepayid,noncestr,timestamp,sign)
                        }
                    }

                    override fun dataNull() {
                    }
                })
    }

    fun pointPay(orderNo: String, payPassword: String, payResult: PayResult) {
        OkGo.post<RootBean>(Url.paybypoint)
                .params("orderno", orderNo)
                .params("paypwd", payPassword)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        payResult.payResult(data.msg, -1)
                    }

                    override fun success(it: Response<RootBean>) {
                        payResult.payResult("支付成功", 0)
                    }

                    override fun dataNull() {
                    }
                })
    }

    interface PayResult {
        fun payResult(msg: String, type: Int)
    }

}