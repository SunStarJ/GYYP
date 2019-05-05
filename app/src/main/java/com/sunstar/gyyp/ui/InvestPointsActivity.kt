package com.sunstar.gyyp.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lzy.okgo.OkGo
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_invest_points.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class InvestPointsActivity : BaseActivity() {
    override fun appViewInitComplete() {
        charge_now.onClick {

        }
    }

    override fun initHeadModel(): HeadVm = HeadVm("",true,R.mipmap.back)

    override fun initView(): View = View.inflate(mContext,R.layout.activity_invest_points,null)

}
