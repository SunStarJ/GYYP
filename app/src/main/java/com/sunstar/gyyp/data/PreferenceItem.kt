package com.sunstar.gyyp.data

data class PreferenceItem(var price: Double = 0.0,
                          var id: Int = 0,
                          var shoppingcartid: Int = 0,
                          var points: Double = 0.0,
                          var num: Int = 0,
                          var count: Int = 0,
                          var pricepoint: Double = 0.0,
                          var pic: String = "",
                          var countrypic: String = "",
                          var title: String = ""):SelectData(){
    var priceShow = price.toString()
    var showNum = ""
    fun changeShowNum(){
        showNum = num.toString()
    }
}