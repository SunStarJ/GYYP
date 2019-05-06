package com.sunstar.gyyp.model

import android.annotation.SuppressLint
import android.app.Activity
import com.alipay.sdk.app.PayTask
import com.sunstar.gyyp.ProjectApplication
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PayModel {
    @SuppressLint("CheckResult")
    fun aliPay(activity:Activity,orderInfo:String){
        Flowable.just(1).map {
            var aliPay = PayTask(activity)
            aliPay.payV2(orderInfo,true)
        }.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe {

        }
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

}