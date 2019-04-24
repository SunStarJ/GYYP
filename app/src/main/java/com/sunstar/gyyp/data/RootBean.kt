package com.sunstar.gyyp.data

data class RootBean(val msg: String = "",
                    val code: Int = 0,
                    val preference: List<PreferenceItem>?,
                    val banner: MutableList<BannerItem>?,
                    val articles: MutableList<ArticlesItem>?)