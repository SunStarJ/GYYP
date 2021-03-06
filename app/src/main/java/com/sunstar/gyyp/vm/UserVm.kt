package com.sunstar.gyyp.vm

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.databinding.BaseObservable
import android.databinding.ObservableField
import android.graphics.Bitmap
import android.view.View
import com.sunstar.gyyp.IdentifyingCode
import com.sunstar.gyyp.ProjectApplication
import com.sunstar.gyyp.base.BaseView
import com.sunstar.gyyp.base.DataListener
import com.sunstar.gyyp.base.Util
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.data.UserChangeData
import com.sunstar.gyyp.model.UserModel
import com.sunstar.gyyp.pop.SexEditPop
import com.sunstar.gyyp.view.ChangeUserInfoView
import com.sunstar.gyyp.view.FindPasswordView
import com.sunstar.gyyp.view.LoginView
import com.sunstar.gyyp.view.RegisterView
import com.yanzhenjie.permission.AndPermission
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import com.yanzhenjie.permission.runtime.Permission
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.concurrent.TimeUnit

open class UserVm<T : BaseView> : BaseObservable, AnkoLogger {

    var userM = UserModel()
    var user: RootBean? = null
    var mView: T? = null

    var bitMap: Bitmap? = null
    var commentNum: String = ""
    var phoneNum: String = ""
    var password: String = ""
    var againPassword: String = ""
    var regisCode: String = ""
    var checkCode: String = ""
    var getCodeString = ObservableField<String>("获取验证码")
    var genderName = ObservableField<String>("保密")

    var nickName: String = ""
    var realName: String = ""
    var gender: Int = 0
    var birthDay: String = ""
    var headerImg: String = ""

    init {
        EventBus.getDefault().register(this)
    }

    fun changeSex(view: View) {
        SexEditPop(view.context, genderName.get()!!, view).show(object : SexEditPop.SexSelect {
            override fun selectSuccess(int: Int) {
                user!!.gender = int
                gender = int
                if (gender == 0) genderName.set("女士") else if (gender == 1) genderName.set("先生") else genderName.set("保密")
            }
        })
    }

    fun changeBirthday(view: View) {
        DatePickerDialog(view.context, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            var date = ""
            date += "$year-"
            date += if (month + 1 >= 10) "${month + 1}-" else "0${month + 1}-"
            date += if (dayOfMonth >= 10) dayOfMonth else "0$dayOfMonth"
            user!!.birth = date
            notifyChange()
        }, user!!.birth.substring(0, 4).toInt(), user!!.birth.substring(5, 7).toInt() - 1, user!!.birth.substring(8, 10).toInt()).show()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(changeData: UserChangeData) {
        user?.run {
            if (changeData.nickname != "") nickname = changeData.nickname
            if (changeData.headImg != "") headpic = changeData.headImg
        }
        nickName = changeData.nickname
        notifyChange()
    }

    fun onDestory() {
        EventBus.getDefault().unregister(this)
    }

    fun initUser() {
        user?.run {
            nickName = nickname
            realName = realname
            this@UserVm.gender = gender
            birthDay = birth
            headerImg = headpic
        }
        if (gender == 0) genderName.set("女士") else if (gender == 1) genderName.set("先生") else genderName.set("保密")
    }


    init {
        getCodeString.addOnPropertyChangedCallback(object : android.databinding.Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: android.databinding.Observable?, propertyId: Int) {
                notifyPropertyChanged(propertyId)
            }
        })
        genderName.addOnPropertyChangedCallback(object : android.databinding.Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: android.databinding.Observable?, propertyId: Int) {
                notifyChange()
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
            mView!!.showMsg("请输入帐号")
            return true
        }
        if (password == "") {
            mView!!.showMsg("请输入密码")
            return true
        }
        if (regisCode == "") {
            mView!!.showMsg("请输入验证码")
            return true
        }
        if (!Util.isMobileNO(phoneNum)) {
            mView!!.showMsg("手机号错误")
            return true
        }
        if (regisCode .toLowerCase()!= IdentifyingCode.instance.code!!.toLowerCase()) {
            mView!!.showMsg("验证码错误")
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
    fun getPhoneCheckCode(type: Int) {
        var phoneNum = this.phoneNum
        getCodeByString(phoneNum, type)
    }

    open fun getCodeByString(phoneNum: String, type: Int) {
        if (showDownUtil == null) {
            if (!Util.isMobileNO(phoneNum)) {
                mView!!.showMsg("请检查手机号")
                return
            }
            userM.getPhoneCode(phoneNum, type, object : DataListener.NetDataListener<Boolean> {
                override fun success(data: Boolean) {

                }

                override fun error(msg: String) {
                    mView?.showMsg(msg)
                }
            })
            showDownUtil = Observable.interval(1, TimeUnit.SECONDS).take(60).map {
                shoutDown -= 1
                shoutDown
            }.observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (shoutDown == 0) {
                    getCodeString.set("获取验证码")
                    showDownUtil = null
                } else {
                    getCodeString.set("${shoutDown}秒后重新获取")
                }
            }
        }
    }

    //注册
    fun regist() {
        if (checkOriginalInputData()) return
        if (otherCheck()) return
        if (!isReadServer) {
            mView!!.showMsg("请阅读并同意协议")
            return
        }
        mView!!.showLoading("提交数据中，请稍候")
        userM.register(commentNum, phoneNum, password, checkCode, object : DataListener.NetDataListener<Boolean> {
            override fun error(msg: String) {
                mView!!.showMsg(msg)
                mView!!.hiddenLoading()
            }

            override fun success(data: Boolean) {
                mView!!.hiddenLoading()
//                login()
                userM.userLogin(phoneNum, password, object : DataListener.NetDataListener<Boolean> {
                    override fun success(data: Boolean) {
                        mView?.hiddenLoading()
                        (mView as RegisterView).registerComplete()
                    }

                    override fun error(msg: String) {
                        mView?.hiddenLoading()
                        mView?.showMsg(msg)
                    }
                })
            }
        })
    }

    private fun otherCheck(): Boolean {
        if (againPassword == "") {
            mView!!.showMsg("请确认密码")
            return true
        }
        if (againPassword != password) {
            mView!!.showMsg("新密码与确认密码不同请检查")
            return true
        }
        if (checkCode == "" || checkCode.length < 6) {
            mView!!.showMsg("请检查短信验证码")
            return true
        }
        return false
    }

    //找回密码
    fun findPassword() {
        if (checkOriginalInputData()) return
        if (otherCheck()) return
        userM.findPassword(phoneNum, password, checkCode, object : DataListener.NetDataListener<Boolean> {
            override fun error(msg: String) {
                mView!!.showMsg(msg)
                mView!!.hiddenLoading()
            }

            override fun success(data: Boolean) {
                mView!!.hiddenLoading()
                (mView as FindPasswordView).findPasswordComplete()

            }
        })
    }

    //获取用户信息
    fun getUserData() {
        mView?.showLoading("获取数据中，请稍候")
        userM.getUserInfo(object : DataListener.NetDataListener<RootBean> {
            override fun error(msg: String) {
                mView?.showMsg(msg)
                mView?.hiddenLoading()
            }

            override fun success(data: RootBean) {
                user = data
                user?.let {
                    notifyChange()
                }
                initUser()
                mView?.hiddenLoading()
            }
        })
    }

    fun saveData() {
        mView?.showLoading("提交数据中，请稍候")
        userM.saveUserData(nickName, realName, gender, user!!.birth, headerImg, object : DataListener.NetDataListener<Boolean> {
            override fun success(data: Boolean) {
                mView?.run {
                    hiddenLoading()
                    showMsg("修改成功")
                    EventBus.getDefault().post(UserChangeData(nickName))
                    back()
                }
            }

            override fun error(msg: String) {
                mView?.run {
                    hiddenLoading()
                    showMsg(msg)
                }
            }
        })
    }

    fun showSelectImagePop() {
        AndPermission.with(ProjectApplication.instance.applicationContext)
                .runtime()
                .permission(Permission.Group.STORAGE, Permission.Group.CAMERA)
                .onGranted {
                    (mView as ChangeUserInfoView).showPicSelectPop()
                }.start()

    }

    fun back() {
        mView?.back()
    }


    fun isRead(isRead: Boolean) {
        isReadServer = isRead
    }

    fun onDestroy() {
        bitMap?.let {
            it.recycle()
        }
        bitMap = null
    }

    fun changePhoto(imgPath: String) {
        info { imgPath }
        headerImg = imgPath
        notifyChange()
    }
}