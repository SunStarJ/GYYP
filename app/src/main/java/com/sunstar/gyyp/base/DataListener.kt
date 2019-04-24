package com.sunstar.gyyp.base

class DataListener {
    interface NetDataListener<T>{
        fun success(data:T)
        fun error(msg:String)
    }
}