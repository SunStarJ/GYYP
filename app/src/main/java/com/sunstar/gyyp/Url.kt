package com.sunstar.gyyp

object Url {
    private val BASE_URL = "http://guoyuanyoupin.tongtonging.com/"
    val getMainPage = "$BASE_URL/api/home/getmainpage"
    val login = "$BASE_URL/api/user/login"
    val register = "$BASE_URL/api/user/register"
    val getsmscode = "$BASE_URL/api/user/getsmscode"
    val getmyinfo = "$BASE_URL/api/user/getmyinfo"
    val editmyinfo = "$BASE_URL/api/user/editmyinfo"
    val uploadpic = "$BASE_URL/api/file/uploadpic"
    val baseUrl:String get() =  BASE_URL
}