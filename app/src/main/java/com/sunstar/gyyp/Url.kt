package com.sunstar.gyyp

object Url {
    private val BASE_URL = "http://guoyuanyoupin.tongtonging.com/"
    val getMainPage = "$BASE_URL/api/home/getmainpage"
    val login = "$BASE_URL/api/user/login"
    val register = "$BASE_URL/api/user/register"
    val getsmscode = "$BASE_URL/api/user/getsmscode"
    val baseUrl:String get() =  BASE_URL
}