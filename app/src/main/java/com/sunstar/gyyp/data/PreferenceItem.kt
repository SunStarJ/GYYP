package com.sunstar.gyyp.data

data class PreferenceItem(var price: Double = 0.0,
                          var id: Int = 0,
                          var points: Int = 0,
                          var num: Int = 0,
                          var pic: String = "",
                          var title: String = ""):SelectData(){
    var priceShow = price.toString()
    var showNum = ""
    fun changeShowNum(){
        showNum = num.toString()
    }
}