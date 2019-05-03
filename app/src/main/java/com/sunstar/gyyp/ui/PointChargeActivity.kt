package com.sunstar.gyyp.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseActivity

class PointChargeActivity : BaseActivity() {
    override fun appViewInitComplete() {

    }

    override fun initHeadModel(): HeadVm = HeadVm("积分兑换",true,R.mipmap.back)

    override fun initView(): View = View.inflate(mContext,R.layout.activity_point_charge,null)
}
