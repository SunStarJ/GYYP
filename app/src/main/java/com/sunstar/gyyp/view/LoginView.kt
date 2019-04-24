package com.sunstar.gyyp.view

import com.sunstar.gyyp.base.BaseView

interface LoginView :BaseView {
    fun loginSuccess()
    fun loginError(msg:String)
}