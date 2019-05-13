package com.sunstar.gyyp.data

data class RootBeanX(
        var address: String,
        var addressee: String,
        var addtime: String,
        var amount: String,
        var cancancel: Int,
        var candelete: Int,
        var canmakesure: Int,
        var code: Int,
        var liaisonphone: String,
        var msg: String,
        var orderid: Int,
        var orderno: String,
        var payamount: Double,
        var paycode: String,
        var paytime: String,
        var postage: String,
        var products: MutableList<Product>,
        var sendway: String,
        var state: Int,
        var trace: String,
        var usercontent: String
)