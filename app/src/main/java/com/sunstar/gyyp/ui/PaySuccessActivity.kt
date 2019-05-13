package com.sunstar.gyyp.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseActivity

class PaySuccessActivity : BaseActivity() {
    override fun appViewInitComplete() {

    }

    override fun initHeadModel(): HeadVm = HeadVm("支付成功",true,R.mipmap.back)

    override fun initView(): View = View.inflate(mContext,R.layout.activity_pay_success,null)
}
