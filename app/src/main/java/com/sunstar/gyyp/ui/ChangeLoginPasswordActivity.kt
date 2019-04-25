package com.sunstar.gyyp.ui

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.databinding.ActivityChangeLoginPasswordBinding
import com.sunstar.gyyp.view.ChangePasswordView
import com.sunstar.gyyp.vm.ChangePasswordVm
import org.jetbrains.anko.toast

class ChangeLoginPasswordActivity : BaseActivity() ,ChangePasswordView{
    override fun changeComplete() {
        toast("修改成功")
        finish()
    }

    override fun appViewInitComplete() {
    }

    override fun initHeadModel(): HeadVm  = HeadVm("修改密码",true,R.mipmap.back)

    override fun initView(): View {
        var binding = DataBindingUtil.inflate<ActivityChangeLoginPasswordBinding>(LayoutInflater.from(mContext),R.layout.activity_change_login_password,null,false)
        binding.data = ChangePasswordVm(this)
        return binding.root
    }
}
