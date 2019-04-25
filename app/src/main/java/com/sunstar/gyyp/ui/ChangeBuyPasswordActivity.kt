package com.sunstar.gyyp.ui

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.databinding.ActivityChangeBuyPasswordBinding
import com.sunstar.gyyp.view.ChangeBuyPasswordView
import com.sunstar.gyyp.vm.ChangeBuyPasswordVm
import org.jetbrains.anko.toast

class ChangeBuyPasswordActivity : BaseActivity(), ChangeBuyPasswordView {
    override fun changeComplete() {
        toast("修改成功")
        finish()
    }

    var binding:ActivityChangeBuyPasswordBinding?=null
    var changeBuyPasswordVm:ChangeBuyPasswordVm?=null
    override fun appViewInitComplete() {
        changeBuyPasswordVm?.createRegistCodeBitmap()
    }

    override fun initHeadModel(): HeadVm = HeadVm("修改支付密码",true,R.mipmap.back)

    override fun initView(): View {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.activity_change_buy_password,null,false)
        changeBuyPasswordVm = ChangeBuyPasswordVm(intent.getStringExtra("userPhone"),this)
        binding!!.data = changeBuyPasswordVm
        return binding!!.root
    }
}
