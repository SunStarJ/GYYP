package com.sunstar.gyyp.base

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Environment
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.inputmethod.InputMethodManager
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import android.view.ViewConfiguration
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH
import android.annotation.TargetApi
import android.os.Build


object Util {
    fun isMobileNO(mobileNums: String): Boolean {
        /**
         * 判断字符串是否符合手机号码格式
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         * @param str
         * @return 待检测的字符串
         */
        val telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$"// "[1]"代表下一位为数字可以是几，"[0-9]"代表可以为0-9中的一个，"[5,7,9]"表示可以是5,7,9中的任意一位,[^4]表示除4以外的任何一个,\\d{9}"代表后面是可以是0～9的数字，有9位。
        return if (TextUtils.isEmpty(mobileNums))
            false
        else
            mobileNums.matches(telRegex.toRegex())
    }

    /**
     * xutil 中提取的生成文件地址方法
     */
    fun getDiskCacheDir(context: Context, dirName: String): String {
        var cachePath: String? = null
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val externalCacheDir = context.externalCacheDir
            if (externalCacheDir != null) {
                cachePath = externalCacheDir.path
            }
        }
        if (cachePath == null) {
            val cacheDir = context.cacheDir
            if (cacheDir != null && cacheDir.exists()) {
                cachePath = cacheDir.path
            }
        }

        return cachePath + File.separator + dirName
    }

    fun changeStringColor(context: Context, allText: String, highColorString: String, colorId: Int): SpannableStringBuilder {
        val builder = SpannableStringBuilder(allText)
        val start = allText.indexOf(highColorString)
        val end = start + highColorString.length
        builder.setSpan(ForegroundColorSpan(context.resources.getColor(colorId)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return builder
    }


    /**
     * 隐藏键盘
     */
    fun hideInput(activity: Activity) {
        var imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager;
        var v = activity.window.peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.windowToken, 0);
        }
    }

    //    获取虚拟键高度
    fun getNavigationBarHeight(context: Context): Int {
        var result = 0
        if (hasNavBar(context)) {
            val res = context.resources
            val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId)
            }
        }
        return result
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    fun hasNavBar(context: Context): Boolean {
        val res = context.resources
        val resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android")
        if (resourceId != 0) {
            var hasNav = res.getBoolean(resourceId)
            // check override flag
            val sNavBarOverride = getNavBarOverride()
            if ("1" == sNavBarOverride) {
                hasNav = false
            } else if ("0" == sNavBarOverride) {
                hasNav = true
            }
            return hasNav
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey()
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private fun getNavBarOverride(): String? {
        var sNavBarOverride: String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                val c = Class.forName("android.os.SystemProperties")
                val m = c.getDeclaredMethod("get", String::class.java)
                m.isAccessible = true
                sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String
            } catch (e: Throwable) {
            }

        }
        return sNavBarOverride
    }

}
