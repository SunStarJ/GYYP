package com.sunstar.gyyp.model

import android.content.Intent
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.sunstar.gyyp.ProjectApplication
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.DataListener
import com.sunstar.gyyp.data.PublicStaticData
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.data.RootBeanX
import com.sunstar.gyyp.ui.LoginActivity

class OrderControlModel {
    fun getOrderInfo(orderId:String,netDataListener: DataListener.NetDataListener<RootBeanX>){
        OkGo.post<String>(Url.getorderdetail)
                .params("token",PublicStaticData.tooken)
                .params("orderid",orderId)
                .execute(object : StringCallback(){
                    override fun onSuccess(response: Response<String>?) {
                        var data = Gson().fromJson<RootBeanX>(response!!.body(),RootBeanX::class.java)
                        if(data.code == 0){
                            netDataListener.success(data)
                        }else if(data.code == -1){
                            var intent = Intent(ProjectApplication.instance.applicationContext, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            intent.putExtra("type",1)
                            ProjectApplication.instance.applicationContext.startActivity(intent)
                        }else{
                            netDataListener.error(data.msg)
                        }
                    }

                    override fun onError(response: Response<String>?) {
                        super.onError(response)

                    }
                })
    }
}