package com.sunstar.gyyp.data

data class PreferenceItem(val price: Double = 0.0,
                          val id: Int = 0,
                          val pic: String = "",
                          val title: String = ""){
    var priceShow = price.toString()
}