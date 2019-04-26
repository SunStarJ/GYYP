package com.sunstar.gyyp.vm

import android.databinding.BaseObservable
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.BaseView
import com.sunstar.gyyp.data.RootBean

class LocationVm(var mv:BaseView):BaseObservable() {
    var receiverName:String = ""
    var receiverPhone:String = ""
    var province:String = ""
    var city:String = ""
    var area:String = ""
    var addressdetail:String = ""
    var cityShow = ""
    var locationId=""

    fun initCityShow(){
        cityShow = "$province  $city  $area"
        notifyChange()
    }

    fun getLocationLis(){
        mv.showLoading("获取数据中，请稍后")
        OkGo.post<RootBean>(Url.getdeliveryaddress)
                .execute(object :BaseCallBack(){
                    override fun dataError(data: RootBean) {
                        mv.hiddenLoading()
                        mv.showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        mv.hiddenLoading()
                        notifyChange()
                    }

                    override fun dataNull() {

                    }
                })
    }

}