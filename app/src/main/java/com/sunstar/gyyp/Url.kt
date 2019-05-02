package com.sunstar.gyyp

object Url {
    private val BASE_URL = "http://47.112.216.41/"
    val getMainPage = "$BASE_URL/api/home/getmainpage"
    val login = "$BASE_URL/api/user/login"
    val register = "$BASE_URL/api/user/register"
    val getsmscode = "$BASE_URL/api/user/getsmscode"
    val getmyinfo = "$BASE_URL/api/user/getmyinfo"
    val editmyinfo = "$BASE_URL/api/user/editmyinfo"
    val uploadpic = "${BASE_URL}api/file/uploadpic"
    val changeloginpwd = "${BASE_URL}api/user/changeloginpwd"
    val changepaypwd = "${BASE_URL}api/user/changepaypwd"
    val getdeliveryaddress = "${BASE_URL}api/user/getdeliveryaddress"
    val editdeliveryaddress = "${BASE_URL}api/user/editdeliveryaddress"
    val setaddressdefault = "${BASE_URL}api/user/setaddressdefault"
    val deleteaddress = "${BASE_URL}api/user/deleteaddress"
    val getarticles = "${BASE_URL}api/article/getarticles"
    val getproductdetail = "${BASE_URL}api/product/getproductdetail"
    val collectproduct = "${BASE_URL}api/product/collectproduct"
    val addshopcart = "${BASE_URL}api/shopcart/addshopcart"
    val getshopcart = "${BASE_URL}api/shopcart/getshopcart"
    val changeshopcartnum = "${BASE_URL}api/shopcart/changeshopcartnum"
    val deleteshopcart = "${BASE_URL}api/shopcart/deleteshopcart"
    val gethotwords = "${BASE_URL}api/product/gethotwords"
    val getproductcategorys = "${BASE_URL}api/product/getproductcategorys"
    val getproducts = "${BASE_URL}api/product/getproducts"
    val baseUrl:String get() =  BASE_URL
}