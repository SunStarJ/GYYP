package com.sunstar.gyyp.data

import java.io.Serializable

data class RootBean(var msg: String = "",
                    var searchlog: MutableList<String>?,
                    var code: Int = 0,
                    var recommcount: Int = 0,
                    var pic: String = "",
                    var id: String = "",
                    var pic1: String = "",
                    var recommpoin: String = "",
                    var pic2: String = "",
                    var preference: MutableList<PreferenceItem>?,
                    var catagories: MutableList<CatagoryBean>?,
                    var hotwords: MutableList<String>?,
                    var products: MutableList<PreferenceItem>?,
//                    var addresslist: MutableList<PreferenceItem>?,
                    var hotmarket: MutableList<HotmarketItem>?,
                    var banner: MutableList<BannerItem>?,
                    var recommends: MutableList<RecommendsItem>?,
                    var token: String = "",
                    var articles: MutableList<ArticlesItem>?,
                    var orders: MutableList<OrderBean>?,
                    var hotproducts: MutableList<PreferenceItem>?,
                    var gender: Int = 0,
                    var iscollected: Int = 0,
                    var existingstocks: Int = 0,
                    var level: String = "",
                    var nickname: String = "",
                    var picurl: String = "",
                    var leftpoint: String = "",
                    var birth: String = "",
                    var uAnnex: String = "",
                    var headpic: String = "",
                    var accumulatepoint: String = "",
                    var u_annex:String="",
                    var content:String = "",
                    var title:String = "",
                    var price:Double = 0.0,
                    var points:Double = 0.0,
                    var productamount:Double = 0.0,
                    var productpoint:Double = 0.0,
                    var totalamount:Double = 0.0,
                    var totalpoint:Double = 0.0,
                    var deliveryamount:Double = 0.0,
                    var deliverypoint:Double = 0.0,
                    var ifhaveaddress:Int = 1,
                    var countrypic:String = "",
                    var countryname:String = "",
                    var recommer:recommerBean?,
                    var addresslist:MutableList<AddressListItem>?,
                    var pics:MutableList<String>?,
                    var realname: String = "",
                    var deliveryway: String = "",
                    var orderno: String = "",
                    var address: AddressBean):Serializable