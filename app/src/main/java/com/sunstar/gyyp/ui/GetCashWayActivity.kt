package com.sunstar.gyyp.ui

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.util.GetBank
import kotlinx.android.synthetic.main.activity_get_cash_way.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class GetCashWayActivity : BaseActivity() {
    override fun appViewInitComplete() {
        initListener()
    }

    @SuppressLint("CheckResult")
    private fun initListener() {
        RxTextView.textChanges(card_no).map {
            if(it.length<6) "" else if(GetBank.getname(it.toString()) == null) "" else GetBank.getname(it.toString())
        }.subscribe {
            bank_name.setText(it)
        }
        sure_click.onClick {
            if(!GetBank.checkBankCard(card_no.text.toString())){
                toast("银行卡号错误，请检查")
                return@onClick
            }
            startActivity<GetMoneyActivity>(
                    "leftPoints" to intent.getStringExtra("leftpoint"),
                    "bankNo" to  card_no.text.toString(),
                    "bankUser" to user_name.text.toString(),
                    "bankName" to bank_name.text.toString()
            )
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(msg:String){
        if(msg == "getmoney_complete"){
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }

    override fun initHeadModel(): HeadVm = HeadVm("提现方式",true,R.mipmap.back)

    override fun initView(): View = View.inflate(mContext,R.layout.activity_get_cash_way,null)
}
