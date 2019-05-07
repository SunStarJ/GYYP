package com.sunstar.gyyp.data

data class RootBeanX(
        val address: String,
        val addressee: String,
        val addtime: String,
        val amount: String,
        val cancancel: Int,
        val candelete: Int,
        val canmakesure: Int,
        val code: Int,
        val liaisonphone: String,
        val msg: String,
        val orderid: Int,
        val orderno: String,
        val payamount: Double,
        val paycode: String,
        val paytime: String,
        val postage: String,
        val products: List<Product>,
        val sendway: String,
        val state: Int,
        val trace: String,
        val usercontent: String
)