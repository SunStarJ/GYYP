package com.sunstar.gyyp.vm

import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.view.ChangePasswordView

class ChangePasswordVm (var mView:ChangePasswordView){


    var oldPassword:String = ""
    var newPassword:String = ""
    var againPassword:String = ""
    fun changePassword(){
        if(oldPassword == ""){
            mView.showMsg("请输入旧密码")
            return
        }
        if(newPassword == ""){
            mView.showMsg("请输入新密码")
            return
        }
        if(againPassword == "") {
            mView.showMsg("请输入确认密码")
            return
        }
        if(newPassword!=againPassword){
            mView.showMsg("两次密码输入不一致")
            return
        }
        mView.showLoading("提交数据中，请稍后")
        OkGo.post<RootBean>(Url.changeloginpwd)
                .params("oldpwd",oldPassword)
                .params("newpwd",newPassword).execute(object:BaseCallBack(){
                    override fun dataError(data: RootBean) {
                        mView.hiddenLoading()
                        mView.showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        mView.hiddenLoading()
                        mView.changeComplete()
                    }

                    override fun dataNull() {
                        mView.hiddenLoading()
                    }
                })
    }
}