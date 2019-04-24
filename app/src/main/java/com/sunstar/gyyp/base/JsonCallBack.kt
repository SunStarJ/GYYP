package com.sunstar.gyyp.base

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.lzy.okgo.callback.AbsCallback
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


abstract class JsonCallBack<T> : AbsCallback<T> {

    private var type: Type? = null
    private var clazz: Class<T>? = null

    constructor(type: Type?) : super() {
        this.type = type
    }

    constructor(clazz: Class<T>?) : super() {
        this.clazz = clazz
    }


    override fun convertResponse(response: okhttp3.Response?): T {
        val body = response?.body() ?: return null!!
        var data: T? = null
        val gson = Gson()
        var jsonReader = JsonReader(body.charStream())
        if (type != null) data = gson.fromJson(jsonReader, type)
        if (clazz != null) data = gson.fromJson(jsonReader, clazz)
        return data!!
    }

}
