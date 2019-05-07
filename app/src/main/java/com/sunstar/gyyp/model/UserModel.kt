package com.sunstar.gyyp.model

import android.annotation.SuppressLint
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.PostRequest
import com.sunstar.gyyp.JavaUtil
import com.sunstar.gyyp.ProjectApplication
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.DataListener
import com.sunstar.gyyp.base.Preference
import com.sunstar.gyyp.base.Util
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.data.UserChangeData
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

class UserModel : AnkoLogger {
    var userToken: String by Preference("token", "")
    fun onDestory() {
        OkGo.getInstance().cancelTag(this)
    }

    fun getUserInfo(netDataListener: DataListener.NetDataListener<RootBean>) {
        OkGo.post<RootBean>(Url.getmyinfo).execute(object : BaseCallBack() {
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

    fun getPhoneCode(userName: String, type: Int, netDataListener: DataListener.NetDataListener<Boolean>) {
        OkGo.post<RootBean>(Url.getsmscode)
                .params("phonenum", userName)
                .params("type", type)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        netDataListener.error(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                    }

                    override fun dataNull() {
                    }
                })
    }

    fun findPassword(userName: String, password: String, checkCode: String, netDataListener: DataListener.NetDataListener<Boolean>) {
        OkGo.post<RootBean>(Url.resetloginpwd)
                .params("phonenum",userName)
                .params("newpwd",password)
                .params("smsvalidcode",checkCode)
                .execute(object:BaseCallBack(){
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

    fun saveUserData(nickname: String, realname: String, gender: Int, birth: String, imagePath: String, netDataListener: DataListener.NetDataListener<Boolean>) {
        var request = OkGo.post<RootBean>(Url.editmyinfo)
                .params("nickname", nickname)
                .params("realname", realname)
                .params("gender", gender)
                .params("birth", birth)
        if (imagePath.contains("storage")) {
            upLoadImage(imagePath, request, netDataListener)
        } else {
            if (imagePath != "") request.params("headpic", imagePath)
            request.execute(object : BaseCallBack() {
                override fun dataError(data: RootBean) {
                    netDataListener.error(data.msg)
                }

                override fun success(it: Response<RootBean>) {
                    var changeData = UserChangeData("")
                    changeData.headImg = imagePath
                    EventBus.getDefault().post(changeData)
                    netDataListener.success(true)
                }

                override fun dataNull() {

                }
            })
        }

    }

    @SuppressLint("CheckResult")
    private fun upLoadImage(s: String, request: PostRequest<RootBean>?, netDataListener: DataListener.NetDataListener<Boolean>) {

        Luban.with(ProjectApplication.instance.applicationContext)
                .load(s)
                .ignoreBy(100)
                .setTargetDir(Util.getDiskCacheDir(ProjectApplication.instance.applicationContext, ""))
                .setCompressListener(object : OnCompressListener {
                    override fun onSuccess(file: File?) {
                        OkGo.post<RootBean>(Url.uploadpic)
                                .params("",file)
                                .execute(object : BaseCallBack() {
                                    override fun dataError(data: RootBean) {
                                        netDataListener.error(data.msg)
                                    }

                                    override fun success(it: Response<RootBean>) {
                                        var imgUrl = it.body().picurl
                                        request!!.params("headpic",imgUrl )
                                        request.execute(object :BaseCallBack(){
                                            override fun dataError(data: RootBean) {
                                                netDataListener.error(data.msg)
                                            }

                                            override fun success(it: Response<RootBean>) {
                                                var changeData = UserChangeData("")
                                                changeData.headImg = imgUrl
                                                EventBus.getDefault().post(changeData)
                                                netDataListener.success(true)
                                            }

                                            override fun dataNull() {

                                            }
                                        })
                                    }

                                    override fun dataNull() {

                                    }
                                })
                    }

                    override fun onError(e: Throwable?) {
                        error { e.toString() }
                    }

                    override fun onStart() {

                    }
                }).launch()
    }

}