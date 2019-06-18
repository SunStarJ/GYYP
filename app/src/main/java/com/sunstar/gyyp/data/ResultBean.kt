package com.sunstar.gyyp.data

data class ResultBean(
        val `package`: String,
        val appid: String,
        val noncestr: String,
        val partnerid: String,
        val prepayid: String,
        val sign: String,
        val timestamp: String
)