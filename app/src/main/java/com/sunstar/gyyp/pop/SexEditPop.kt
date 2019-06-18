package com.sunstar.gyyp.pop

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import com.sunstar.gyyp.R
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.wrapContent
import android.view.WindowManager
import kotlinx.android.synthetic.main.adapter_main_control.view.*
import org.jetbrains.anko.textColor


class SexEditPop (var context:Context,var data:String,var fatherView:View){
    var window: PopupWindow? = null
    var windowView: View? = null
    var listener:SexSelect?=null
    init {
        window = PopupWindow(context)
        initView()
        window?.run {
            contentView = windowView!!
            setOnDismissListener {
                backgroundAlpha(1f)
            }
            isOutsideTouchable = true
            width = fatherView.measuredWidth
            height = wrapContent
        }
    }

    private fun initView() {
        var man:TextView? = null
        var woman:TextView? = null
        var sercate:TextView? = null
        windowView = View.inflate(context, R.layout.dialog_sex_edit,null)
        windowView?.run {
            man = find(R.id.man)
            woman = find(R.id.woman)
            sercate = find(R.id.sercate)
            if(data == man!!.text.toString()){
                man!!.textColor = context.resources.getColor(R.color.color_red)
            }else{
                man!!.textColor = context.resources.getColor(R.color.color_text_black)
            }
            if(data == woman!!.text.toString()){
                woman!!.textColor = context.resources.getColor(R.color.color_red)
            }else{
                woman!!.textColor = context.resources.getColor(R.color.color_text_black)
            }
            if(data == sercate!!.text.toString()){
                sercate!!.textColor = context.resources.getColor(R.color.color_red)
            }else{
                sercate!!.textColor = context.resources.getColor(R.color.color_text_black)
            }
            man?.onClick {
                listener?.selectSuccess(1)
                window?.dismiss()
            }
            woman?.onClick {
                listener?.selectSuccess(0)
                window?.dismiss()
            }
            sercate?.onClick {
                listener?.selectSuccess(2)
                window?.dismiss()
            }
        }
    }

    fun show(listener:SexSelect){
        this.listener = listener
        window!!.showAsDropDown(fatherView)
        backgroundAlpha(0.7f)
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    fun backgroundAlpha(bgAlpha: Float) {
        val lp = (context as Activity).window.attributes
        lp.alpha = bgAlpha //0.0-1.0
        (context as Activity).window.attributes = lp
    }

    interface SexSelect{
        fun selectSuccess(int: Int)
    }
}