package com.sunstar.gyyp.model

import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.DataListener
import com.sunstar.gyyp.data.RootBean

class UserModel {
    fun onDestory() {
        OkGo.getInstance().cancelTag(this)
    }

    fun userLogin(userName: String, password: String, netDataListener: DataListener.NetDataListener<Boolean>) {
        OkGo.post<RootBean>(Url.login)
                .params("account", userName)
                .params("password", password).execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        netDataListener.error(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        netDataListener.success(true)
                    }

                    override fun dataNull() {

                    }
                })
    }

    fun register(commentNum: String, userName: String, password: String, checkCode: String, netDataListener: DataListener.NetDataListener<Boolean>) {
        OkGo.post<RootBean>(Url.register)
                .params("invitecode", commentNum)
                .params("phonenum", userName)
                .params("password", password)
                .params("smsvalidcode", checkCode)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        netDataListener.error(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        netDataListener.success(true)
                    }

                    override fun dataNull() {

                    }
                })
    }

    fun getPhoneCode(userName: String, netDataListener: DataListener.NetDataListener<Boolean>) {
        OkGo.post<RootBean>(Url.getsmscode).params("phonenum",userName)
                .execute(object:BaseCallBack(){
                    override fun dataError(data: RootBean) {
                        netDataListener.error(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                    }

                    override fun dataNull() {
                    }
                })
    }
}