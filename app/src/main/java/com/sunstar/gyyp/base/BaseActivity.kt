package com.sunstar.gyyp.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import com.sunstar.activityplugin.SSActivity
import com.sunstar.gyyp.R
import com.yanzhenjie.sofia.Bar
import com.yanzhenjie.sofia.Sofia
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jetbrains.anko.*
import java.util.concurrent.TimeUnit


abstract class BaseActivity : SSActivity(), BaseView, AnkoLogger {
    private var dialog: ProgressDialog? = null
    var sofia: Bar? = null
    @SuppressLint("CheckResult")
    override fun showLoading(msg: String) {
        Observable.timer(50,TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (dialog == null) dialog = indeterminateProgressDialog(message = msg, title = "")
        }

    }

    override fun viewInitComplete() {
        getTitleText().textColor = mContext.resources.getColor(R.color.color_text_black)
        info { "activityName:$localClassName" }

        sofia = Sofia.with(mContext as Activity)
                .statusBarBackground(mContext.resources.getColor(R.color.colorWhite))
                .statusBarDarkFont()
        appViewInitComplete()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog?.run {
            dismiss()
        }
        dialog = null
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
