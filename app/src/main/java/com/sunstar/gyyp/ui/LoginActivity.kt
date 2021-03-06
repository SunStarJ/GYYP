package com.sunstar.gyyp.ui

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.MainActivity
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.databinding.ActivityLoginBinding
import com.sunstar.gyyp.view.LoginView
import com.sunstar.gyyp.vm.UserVm
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class LoginActivity : BaseActivity(),LoginView {
    override fun loginSuccess() {
        if(intent.getIntExtra("type",0) == 1){
            var intent = Intent(mContext,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }else{
            finish()
        }

    }

    override fun loginError(msg:String) {
        showMsg(msg)
    }

    var bingding:ActivityLoginBinding? = null

    var userVm:UserVm<LoginView>?=null

    override fun appViewInitComplete() {
        userVm?.let {
            it.createRegistCodeBitmap()
        }
        initListener()
    }

    override fun onResume() {
        super.onResume()
        userVm?.let {
            it.createRegistCodeBitmap()
        }
    }

    private fun initListener() {
        find_password.onClick {
            startActivity<FindPasswordActivity>()
        }
        register_password.onClick {
            startActivity<RegisterActivity>()
        }
    }

    override fun initHeadModel(): HeadVm = HeadVm("登录",true,R.mipmap.back)

    override fun initView(): View {
        bingding = DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.activity_login,null,false)
        userVm = UserVm(this)
        userVm?.let {
            bingding!!.data = it
        }
        return bingding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        userVm?.onDestroy()
    }

}
