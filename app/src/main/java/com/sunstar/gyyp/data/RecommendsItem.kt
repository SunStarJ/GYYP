package com.sunstar.gyyp.data

data class RecommendsItem(val price: Double = 0.0,
                          val countryname: String = "",
                          val id: Int = 0,
                          val pic: String = "",
                          val title: String = "",
                          val countrypic: String = ""){
    var showPrice = price.toString()
}