package com.sunstar.gyyp.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.data.RootBean
import kotlinx.android.synthetic.main.activity_point_charge.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class PointChargeActivity : BaseActivity() {
    override fun appViewInitComplete() {
        sure.onClick {
            commitData()
        }
    }

    private fun commitData() {
        showLoading("提交数据中，请稍后")
        OkGo.post<RootBean>(Url.exchangepoints)
                .params("cardno",key.text.toString())
                .params("pwd",password.text.toString())
                .execute(object:BaseCallBack(){
                    override fun dataError(data: RootBean) {
                        hiddenLoading()
                        toast(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        hiddenLoading()
                        toast(it.body().msg)
                    }

                    override fun dataNull() {
                    }
                })
    }

    override fun initHeadModel(): HeadVm = HeadVm("积分兑换",true,R.mipmap.back)

    override fun initView(): View = View.inflate(mContext,R.layout.activity_point_charge,null)
}
