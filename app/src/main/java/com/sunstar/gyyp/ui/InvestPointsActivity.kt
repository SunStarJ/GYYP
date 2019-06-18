package com.sunstar.gyyp.ui

import android.app.Activity
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
import com.sunstar.gyyp.base.DataListener
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.model.PayModel
import com.sunstar.gyyp.pop.PayWaySelectDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_invest_points.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.info
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

class InvestPointsActivity : BaseActivity() {
    override fun appViewInitComplete() {
        left_cont.text = intent.getStringExtra("leftpoint")
        charge_now.onClick {
            if(charge_num.text.toString() == ""){
                toast("请输入充值金额")
                return@onClick
            }
            var dialog = PayWaySelectDialog(mContext,"").hidePoint()
            dialog.initListener(object :PayWaySelectDialog.PayComplete{
                override fun payNow(payWay: Int) {
                    info { payWay }
                    if(payWay == 1){
                        payNow(0)
                    }else if(payWay == 2){
                        payNow(1)
                    }
                }
            })
            dialog.show()

        }
    }

    private fun payNow(type:Int) {
        OkGo.post<RootBean>(Url.recharge)
                .params("money", charge_num.text.toString())
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        if(type == 2){
                            PayModel.wxPreparePay(it.body().rechargeno, 2, object : DataListener.NetDataListener<String> {
                                override fun success(data: String) {
                                    startActivity<PaySuccessActivity>()
                                    EventBus.getDefault().post("getmoney_complete")
                                    toast("支付成功")
                                    finish()
                                }

                                override fun error(msg: String) {
                                    showMsg(msg)
                                }
                            },object : PayModel.PayResult {
                                override fun payResult(msg: String, type: Int) {
                                    startActivity<PaySuccessActivity>()
                                    EventBus.getDefault().post("getmoney_complete")
                                    toast("支付成功")
                                    finish()
                                }
                            })
                        }else if(type == 1){
                            PayModel.aliPayPrepare(it.body().rechargeno, 2, object : DataListener.NetDataListener<String> {
                                override fun success(data: String) {
                                    PayModel.aliPay(mContext as Activity, data, object : PayModel.PayResult {
                                        override fun payResult(msg: String, type: Int) {
                                            startActivity<PaySuccessActivity>()
                                            EventBus.getDefault().post("getmoney_complete")
                                            toast("支付成功")
                                            finish()
                                        }
                                    })
                                }

                                override fun error(msg: String) {
                                    showMsg(msg)
                                }
                            })
                        }

                    }

                    override fun dataNull() {
                    }
                })
    }

    override fun initHeadModel(): HeadVm = HeadVm("积分充值",true,R.mipmap.back)

    override fun initView(): View = View.inflate(mContext,R.layout.activity_invest_points,null)

}
