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
import kotlinx.android.synthetic.main.activity_get_money.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.sdk27.coroutines.onClick

class GetMoneyActivity : BaseActivity() {
    var leftPoints = 0.0
    override fun appViewInitComplete() {
        leftPoints = intent.getStringExtra("leftPoints").toDouble()
        current_points.text = "剩余积分$leftPoints"
        get_money_now.onClick {
            if (point_input.text.toString().isEmpty()) {
                showMsg("请输入兑换积分")
                return@onClick
            }
            if (pay_password.text.toString().isEmpty()) {
                showMsg("请输入密码")
                return@onClick
            }
            commmitData()
        }
        bank_name.text = intent.getStringExtra("bankName")
        var replaceString = ""
        for (i in 4 until intent.getStringExtra("bankNo").length - 4) {
            replaceString += "*"
        }
        bank_no.text = intent.getStringExtra("bankNo").replace(intent.getStringExtra("bankNo").substring(4, intent.getStringExtra("bankNo").length - 4), replaceString)
        input_all.onClick {
            point_input.setText(leftPoints.toString())
        }
    }

    private fun commmitData() {
        OkGo.post<RootBean>(Url.pointwithdraw)
                .params("point", point_input.text.toString())
                .params("bankno", intent.getStringExtra("bankNo"))
                .params("bank", intent.getStringExtra("bankName"))
                .params("bankuser", point_input.text.toString())
                .params("paypwd", pay_password.text.toString())
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        hiddenLoading()
                        showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        hiddenLoading()
                        showMsg(it.body().msg)
                        EventBus.getDefault().post("getmoney_complete")
                        finish()
                    }

                    override fun dataNull() {

                    }
                })
    }

    override fun initHeadModel(): HeadVm = HeadVm("申请提现", true, R.mipmap.back)

    override fun initView(): View = View.inflate(mContext, R.layout.activity_get_money, null)

}
