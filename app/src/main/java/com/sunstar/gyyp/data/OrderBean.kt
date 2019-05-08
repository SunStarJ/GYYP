package com.sunstar.gyyp.data

data class OrderBean(
        var amount: Double,
        var details: MutableList<DetailBean>,
        var id: Int,
        var orderno: String,
        var cancancel: Int,
        var candelete: Int,
        var canmakesure: Int,
        var state: Int,
        var totalcount: Int
)