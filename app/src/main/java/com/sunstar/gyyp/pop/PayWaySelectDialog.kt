package com.sunstar.gyyp.pop

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.data.RootBean
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class PayWaySelectDialog(var mContext: Context, var pointNum: String) {

    var bottomDialog: BottomSheetDialog? = null
    var listener: PayComplete? = null
    var countText: TextView? = null

    fun hidePoint(): PayWaySelectDialog {
        v?.run {
            find<LinearLayout>(R.id.pay_way_count).visibility = View.GONE
            find<ImageView>(R.id.pay_way_ali_select).isSelected = true
        }
        return this
    }

    init {
        bottomDialog = BottomSheetDialog(mContext)
        bottomDialog?.setContentView(initDialogView())
    }

    fun initListener(listener: PayComplete): PayWaySelectDialog {
        this.listener = listener
        return this
    }

    private var v: View? = null

    private fun initDialogView(): View {
        v = View.inflate(mContext, R.layout.pay_way_dialog, null)
        countText = v?.find(R.id.count_text)
        countText?.text = "积　分\\n剩余积分：核算中.."
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
        for (layout in clickList) {
            layout.onClick {
                when (layout.id) {
                    R.id.pay_way_count -> {
                        checkData(0, selectList)
                    }
                    R.id.pay_way_ali -> {
                        checkData(1, selectList)
                    }
                    R.id.pay_way_wechat -> {
                        checkData(2, selectList)
                    }
                }
            }
        }
        v?.find<View>(R.id.pay_now)?.onClick {
            var payWay = 0
            for (i in 0 until selectList.size) {
                if (selectList[i].isSelected) {
                    payWay = i
                }
            }
            bottomDialog?.dismiss()
            if (payWay == 0 && leftpoint == "") {
                mContext.toast("积分余额获取失败，请重新点击支付重试")
                return@onClick
            } else if (payWay == 0 && leftpoint != "") {
//                if(leftpoint.toDouble()<pointNum.toDouble()){
//                    mContext.toast("积分余额不足，请充值或使用其他支付方式")
//                    return@onClick
//                }
            }
            listener?.payNow(payWay)
        }
        initData()
        return v!!
    }

    var leftpoint: String = ""

    private fun initData() {
        OkGo.post<RootBean>(Url.getmyinfo).execute(object : BaseCallBack() {
            override fun dataError(data: RootBean) {
                mContext.toast("获取数据失败，请重新点击付款打开获取积分")
                countText?.text = "积　分\n剩余积分：核算失败，请重新打开获取"
            }

            override fun success(it: Response<RootBean>) {
                leftpoint = it.body().leftpoint
                countText?.text = "积　分\n剩余积分：$leftpoint"
            }

            override fun dataNull() {

            }
        })
    }

    private fun checkData(i: Int, selectList: MutableList<ImageView>) {
        for (data in selectList) {
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

    interface PayComplete {
        fun payNow(payWay: Int)
    }

}