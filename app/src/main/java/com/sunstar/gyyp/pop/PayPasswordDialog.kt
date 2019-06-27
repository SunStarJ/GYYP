package com.sunstar.gyyp.pop

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.EditText
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.model.PayModel
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class PayPasswordDialog (var mContext:Context,var orderNo:String,var payResult: PayModel.PayResult){
    var dialog:Dialog?=null
    var view: View?=null
    init {
        dialog = Dialog(mContext)
        dialog?.run {
            setContentView(initView())
            setOnDismissListener {
                dialog = null
                view = null
            }
        }
    }

    fun show(){
        dialog?.show()
    }
    fun hide(){
        dialog?.dismiss()
    }

    private fun initView():View {
        view = View.inflate(mContext, R.layout.dialog_pay_password_layout,null)
        view?.run {
            find<View>(R.id.dismiss).onClick {
                dialog?.dismiss()
            }
            find<View>(R.id.sure).onClick {
                PayModel.pointPay(orderNo,find<EditText>(R.id.password_input).text.toString(),payResult)
            }
        }
        return view!!
    }

}