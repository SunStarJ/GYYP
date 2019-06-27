package com.sunstar.gyyp.ui

import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.MainActivity
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.databinding.ActivityRegisterBinding
import com.sunstar.gyyp.view.RegisterView
import com.sunstar.gyyp.vm.UserVm
import org.jetbrains.anko.toast

class RegisterActivity : BaseActivity() ,RegisterView{
    override fun registerComplete() {
        toast("注册成功")
        var intent = Intent(mContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    var binding : ActivityRegisterBinding?=null
    var vm : UserVm<RegisterView>?=null
    override fun appViewInitComplete() {
        vm?.createRegistCodeBitmap()
    }

    override fun initHeadModel(): HeadVm = HeadVm("注册",true,R.mipmap.back)

    override fun initView(): View {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.activity_register,null,false)
        vm = UserVm(this)
        binding!!.data = vm
        return binding!!.root
    }

    override fun back() {
        finish()
    }
    override fun onDestroy() {
        super.onDestroy()
        vm?.onDestory()
    }
}
