package com.sunstar.gyyp.data

data class OrderBean(
        var amount: Double,
        var details: MutableList<DetailBean>,
        var id: Int,
        var orderno: String,
        var state: Int,
        var totalcount: Int
)