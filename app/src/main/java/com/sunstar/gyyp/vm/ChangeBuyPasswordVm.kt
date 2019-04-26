package com.sunstar.gyyp.vm

import android.provider.DocumentsContract
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.gyyp.IdentifyingCode
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.BaseView
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.view.ChangeBuyPasswordView

class ChangeBuyPasswordVm(var phonenum:String,mv:ChangeBuyPasswordView):UserVm<ChangeBuyPasswordView>(mv) {
    var showPhoneNum = phonenum.replace(
            phonenum.substring(3,7),"****"
    )
    var newpwd =""
    var againpassword=""
    var smscode =""
    var imgcheckCode = ""
    fun changeBuyPassword(){
        mView?.run {
            var message = ""
            if(imgcheckCode == ""){
                message = "请输入图片验证码"
            }
            if(newpwd == ""){
                message = "请输入新密码"
            }
            if(againpassword == ""){
                message = "请输入确认密码"
            }
            if(smscode == ""){
                message = "请输入短信验证码"
            }
            if(imgcheckCode!= IdentifyingCode.instance.code){
                message = "请检查图片验证码"
            }
            if(newpwd!=againpassword){
                message = "两次密码输入不一致"
            }
            if(message!=""){
                showMsg(message)
            }else{
                showLoading("提交数据中，请稍后")
                OkGo.post<RootBean>(Url.changepaypwd)
                        .params("phonenum",phonenum)
                        .params("smscode",smscode)
                        .params("newpwd",newpwd)
                        .execute(object :BaseCallBack(){
                            override fun dataError(data: RootBean) {
                                hiddenLoading()
                                showMsg(data.msg)
                            }

                            override fun success(it: Response<RootBean>) {
                                hiddenLoading()
                                changeComplete()
                            }

                            override fun dataNull() {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }
                        })
            }
        }

    }
    fun getNetCode(){
        getCodeByString(phonenum,3)
    }
}