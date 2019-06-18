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
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.view.Display
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN








abstract class BaseActivity : SSActivity(), BaseView, AnkoLogger {
    private var dialog: ProgressDialog? = null
    var sofia: Bar? = null
    @SuppressLint("CheckResult")
    override fun showLoading(msg: String) {
        Observable.timer(50,TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (dialog == null) dialog = indeterminateProgressDialog(message = msg, title = "")
            dialog?.setOnDismissListener {
                dialog = null
            }
        }

    }

    override fun viewInitComplete() {
        setWindow()
        getTitleText().textColor = mContext.resources.getColor(R.color.color_text_black)
        info { "activityName:$localClassName" }
        val decor = (mContext as Activity).getWindow().getDecorView()
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
//        sofia = Sofia.with(mContext as Activity)
//                .statusBarBackground(mContext.resources.getColor(R.color.colorWhite))
//                .statusBarDarkFont()
//                .invasionNavigationBar().navigationBarBackgroundAlpha(100)
        window.statusBarColor = mContext.resources.getColor(R.color.colorWhite)


        appViewInitComplete()
    }

    private fun setWindow() {
//        val display = windowManager.defaultDisplay
//        var heigth = display.height
//        var width = display.width
//        var params = getWindowView()!!.layoutParams!!
//        params.height = heigth
//        getWindowView()!!.layoutParams = params
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog?.run {
            dismiss()
        }
        dialog = null
    }

    override fun onResume() {
        super.onResume()
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
