package com.sunstar.gyyp.pop

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.sunstar.gyyp.R
import kotlinx.android.synthetic.main.adapter_shop_cart.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick

class PayWaySelectDialog(var mContext: Context, var orderNo: String) {

    var bottomDialog: BottomSheetDialog? = null
    var listener : PayComplete? = null
    init {
        bottomDialog = BottomSheetDialog(mContext)
        bottomDialog?.setContentView(initDialogView())
    }

    fun initListener(listener : PayComplete):PayWaySelectDialog{
        this.listener = listener
        return this
    }

    private var v: View? = null

    private fun initDialogView(): View {
        v = View.inflate(mContext, R.layout.pay_way_dialog, null)
        var clickList = mutableListOf<LinearLayout>(
                v!!.find(R.id.pay_way_count),
                v!!.find(R.id.pay_way_ali),
                v!!.find(R.id.pay_way_wechat)
        )
        var selectList = mutableListOf<ImageView>(
                v!!.find(R.id.pay_way_count_select),
                v!!.find(R.id.pay_way_ali_select),
                v!!.find(R.id.pay_way_wechat_select)
        )
        selectList[0].isSelected = true
        for (layout in clickList){
            layout.onClick {
                when(layout.id){
                    R.id.pay_way_count->{
                        checkData(0,selectList)
                    }
                    R.id.pay_way_ali->{
                        checkData(1,selectList)
                    }
                    R.id.pay_way_wechat->{
                        checkData(2,selectList)
                    }
                }
            }
        }
        v?.find<View>(R.id.pay_now)?.onClick {
            var payWay = 0
            for(i in 0 until selectList.size){
                if(selectList[i].isSelected){
                    payWay = i
                }
            }
            bottomDialog?.dismiss()
            listener?.payNow(payWay)
        }
        return v!!
    }

    private fun checkData(i: Int, selectList: MutableList<ImageView>) {
        for (data in selectList){
            data.isSelected = false
        }
        selectList[i].isSelected = true
    }

    fun show() {
        bottomDialog?.show()
    }

    fun hide() {
        bottomDialog?.dismiss()
        bottomDialog = null
    }

    interface PayComplete{
        fun payNow(payWay:Int)
    }

}