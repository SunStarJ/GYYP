package com.sunstar.gyyp.data

data class RootBean(var msg: String = "",
                    var searchlog: MutableList<String>?,
                    var code: Int = 0,
                    var pic: String = "",
                    var pic1: String = "",
                    var pic2: String = "",
                    var preference: MutableList<PreferenceItem>?,
//                    var addresslist: MutableList<PreferenceItem>?,
                    var hotmarket: MutableList<HotmarketItem>?,
                    var banner: MutableList<BannerItem>?,
                    var recommends: MutableList<RecommendsItem>?,
                    var token: String = "",
                    var articles: MutableList<ArticlesItem>?,
                    var hotproducts: MutableList<PreferenceItem>?,
                    var gender: Int = 0,
                    var level: String = "",
                    var nickname: String = "",
                    var leftpoint: String = "",
                    var birth: String = "",
                    var uAnnex: String = "",
                    var headpic: String = "",
                    var accumulatepoint: String = "",
                    var u_annex:String="",
                    var realname: String = "")