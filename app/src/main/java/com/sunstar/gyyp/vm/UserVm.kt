package com.sunstar.gyyp.vm

import android.annotation.SuppressLint
import android.databinding.BaseObservable
import android.databinding.ObservableField
import android.graphics.Bitmap
import com.sunstar.gyyp.IdentifyingCode
import com.sunstar.gyyp.base.BaseView
import com.sunstar.gyyp.base.DataListener
import com.sunstar.gyyp.base.Util
import com.sunstar.gyyp.model.UserModel
import com.sunstar.gyyp.view.LoginView
import com.sunstar.gyyp.view.RegisterView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class UserVm<T : BaseView> : BaseObservable {

    var userM = UserModel()

    var mView: T? = null

    var bitMap: Bitmap? = null
    var commentNum:String  =""
    var phoneNum: String = ""
    var password: String = ""
    var againPassword: String = ""
    var regisCode: String = ""
    var checkCode: String = ""
    var getCodeString = ObservableField<String>("获取验证码")

    init {
        getCodeString.addOnPropertyChangedCallback(object : android.databinding.Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: android.databinding.Observable?, propertyId: Int) {
                notifyPropertyChanged(propertyId)
            }
        })
    }

    var isReadServer = false

    constructor(mView: T?) : super() {
        this.mView = mView
    }

    fun login() {
        if (mView is LoginView) {
            if (checkOriginalInputData()) return
            mView?.showLoading("登陆中，请稍候")
            userM.userLogin(phoneNum, password, object : DataListener.NetDataListener<Boolean> {
                override fun success(data: Boolean) {
                    mView?.hiddenLoading()
                    (mView as LoginView).loginSuccess()
                }

                override fun error(msg: String) {
                    mView?.hiddenLoading()
                    mView?.showMsg(msg)
                }
            })
        }
    }

    private fun checkOriginalInputData(): Boolean {
        if (phoneNum == "") {
            (mView as LoginView).loginError("请输入帐号")
            return true
        }
        if (password == "") {
            (mView as LoginView).loginError("请输入密码")
            return true
        }
        if (regisCode == "") {
            (mView as LoginView).loginError("请输入验证码")
            return true
        }
        if (!Util.isMobileNO(phoneNum)) {
            (mView as LoginView).loginError("手机号错误")
            return true
        }
        if (regisCode != IdentifyingCode.instance.code) {
            (mView as LoginView).loginError("验证码错误")
            return true
        }
        return false
    }

    //生成验证码
    @SuppressLint("CheckResult")
    fun createRegistCodeBitmap() {
        Observable.just(1).map {
            IdentifyingCode.instance.createBitmap()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    bitMap = it
                    notifyChange()
                }
    }

    var shoutDown = 60

    private var showDownUtil: Disposable? = null

    //获取验证码
    fun getPhoneCheckCode() {
        if (showDownUtil == null) {
            if(!Util.isMobileNO(phoneNum)){
                mView!!.showMsg("请检查手机号")
                return
            }
            userM.getPhoneCode(phoneNum,object:DataListener.NetDataListener<Boolean>{
                override fun success(data: Boolean) {

                }

                override fun error(msg: String) {

                }
            })
            showDownUtil = Observable.interval(1, TimeUnit.SECONDS).take(60).map {
                shoutDown -= 1
                shoutDown
            }.observeOn(AndroidSchedulers.mainThread()).subscribe {
                if(shoutDown == 0){
                    getCodeString.set("获取验证码")
                    showDownUtil = null
                }else{
                    getCodeString.set("${shoutDown}秒后重新获取")
                }
            }
        }
    }

    //注册
    fun regist(){
        if (checkOriginalInputData()) return
        if(againPassword == ""){
            mView!!.showMsg("请确认密码")
            return
        }
        if(againPassword!=password){
            mView!!.showMsg("新密码与确认密码不同请检查")
            return
        }
        if(checkCode == "" || checkCode.length<6){
            mView!!.showMsg("请检查短信验证码")
            return
        }
        if(!isReadServer){
            mView!!.showMsg("请阅读并同意协议")
            return
        }
        mView!!.showLoading("提交数据中，请稍候")
        userM.register(commentNum,phoneNum,password,checkCode,object :DataListener.NetDataListener<Boolean>{
            override fun error(msg: String) {
                mView!!.showMsg(msg)
                mView!!.hiddenLoading()
            }

            override fun success(data: Boolean) {
                mView!!.hiddenLoading()
                (mView as RegisterView).registerComplete()

            }
        })
    }

    fun isRead(isRead:Boolean){
        isReadServer = isRead
    }

    fun onDestroy() {
        bitMap?.let {
            it.recycle()
        }
        bitMap = null
    }
}