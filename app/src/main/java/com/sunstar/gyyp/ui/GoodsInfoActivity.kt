package com.sunstar.gyyp.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseActivity

class GoodsInfoActivity : BaseActivity() {
    override fun appViewInitComplete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initHeadModel(): HeadVm = HeadVm("商品详情",true,R.mipmap.back)

    override fun initView(): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_info)
    }
}
