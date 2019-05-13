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

object PayModel {
    @SuppressLint("CheckResult")
    fun aliPay(activity:Activity,orderInfo:String,resultListener:PayResult){
        Flowable.just(1).map {
            var aliPay = PayTask(activity)
            aliPay.payV2(orderInfo,true)
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            when(it["resultStatus"]!!){
                "9000"->{
                    resultListener.payResult("支付成功",9000)
                }
                "4000"->{
                    resultListener.payResult("支付失败，订单已生成，请去我的订单中继续支付",4000)
                }
            }
        }
    }

    fun aliPayPrepare (orderNo:String,type:Int, dataListener: DataListener.NetDataListener<String>){
        OkGo.post<RootBean>(Url.alipay)
                .params("datano",orderNo)
                .params("datatype",type)
                .execute(object:BaseCallBack(){
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


    fun weChatPay(){
        var wxApi = WXAPIFactory.createWXAPI(ProjectApplication.instance.applicationContext,null)
        wxApi.registerApp("")
        var request = PayReq()
        request.appId = "wxd930ea5d5a258f4f"
        request.partnerId = "1900000109"
        request.prepayId= "1101000000140415649af9fc314aa427"
        request.packageValue = "Sign=WXPay"
        request.nonceStr= "1101000000140429eb40476f8896f4c9"
        request.timeStamp= "1398746574"
        request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B"
        wxApi.sendReq(request)
    }

    interface PayResult{
        fun payResult(msg:String,type:Int)
    }

}