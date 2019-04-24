package com.sunstar.gyyp.base

import android.app.Activity
import android.app.ProgressDialog
import com.sunstar.activityplugin.SSActivity
import com.sunstar.gyyp.R
import com.yanzhenjie.sofia.Bar
import com.yanzhenjie.sofia.Sofia
import org.jetbrains.anko.*


abstract class BaseActivity : SSActivity(), BaseView, AnkoLogger {
    private var dialog: ProgressDialog? = null
    var sofia: Bar? = null
    override fun showLoading(msg: String) {
        if (dialog == null) dialog = indeterminateProgressDialog(message = msg, title = "")
    }

    override fun viewInitComplete() {
        getTitleText().textColor = mContext.resources.getColor(R.color.color_text_black)
        info { "activityName:$localClassName" }

        sofia = Sofia.with(mContext as Activity)
                .statusBarBackground(mContext.resources.getColor(R.color.colorWhite))
                .statusBarDarkFont()
        appViewInitComplete()
    }

    abstract fun appViewInitComplete()

    override fun hiddenLoading() {
        dialog?.hide()
        dialog = null
    }

    override fun showMsg(msg: String) {
        toast(msg)
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    fun getStatusBarHeight(): Int {
        val resources = mContext.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

}
