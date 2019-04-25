package com.sunstar.gyyp.model

import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.PostRequest
import com.sunstar.gyyp.ProjectApplication
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.DataListener
import com.sunstar.gyyp.base.Preference
import com.sunstar.gyyp.base.Util
import com.sunstar.gyyp.data.RootBean
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

class UserModel {
    var userToken :String by Preference("token","")
    fun onDestory() {
        OkGo.getInstance().cancelTag(this)
    }

    fun getUserInfo(netDataListener: DataListener.NetDataListener<RootBean>){
        OkGo.post<RootBean>(Url.getmyinfo).execute(object:BaseCallBack(){
            override fun dataError(data: RootBean) {
                netDataListener.error(data.msg)
            }

            override fun success(it: Response<RootBean>) {
                netDataListener.success(it.body())
            }

            override fun dataNull() {

            }
        })
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
                        userToken = it.body().token
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

    fun findPassword( userName: String, password: String, checkCode: String, netDataListener: DataListener.NetDataListener<Boolean>){
        TODO("找回密码逻辑暂无")
    }

    fun saveUserData(nickname:String,realname:String,gender:Int,birth:String,imagePath:String,netDataListener: DataListener.NetDataListener<Boolean>){
        var request =OkGo.post<RootBean>(Url.editmyinfo)
                .params("nickname",nickname)
                .params("realname",realname)
                .params("gender",gender)
                .params("birth",birth)
        if(imagePath.contains("storage")){
            upLoadImage(imagePath,request,netDataListener)
        }else{
            request.params("headpic",Url.baseUrl+imagePath)
        }
        request.execute(object :BaseCallBack(){
            override fun dataError(data: RootBean) {

            }

            override fun success(it: Response<RootBean>) {

            }

            override fun dataNull() {

            }
        })
    }

    private fun upLoadImage(s: String, request: PostRequest<RootBean>?,netDataListener: DataListener.NetDataListener<Boolean>) {
        Luban.with(ProjectApplication.instance.applicationContext)
                .load(s)
                .ignoreBy(100)
                .setTargetDir(Util.getDiskCacheDir(ProjectApplication.instance.applicationContext,"cache_image"))
                .setCompressListener(object :OnCompressListener{
                    override fun onSuccess(file: File?) {
                        OkGo.post<RootBean>(Url.uploadpic)
                                .upFile(file)
                                .execute(object:BaseCallBack(){
                                    override fun dataError(data: RootBean) {

                                    }

                                    override fun success(it: Response<RootBean>) {

                                    }

                                    override fun dataNull() {

                                    }
                                })
                    }

                    override fun onError(e: Throwable?) {

                    }

                    override fun onStart() {

                    }
                })
    }

}