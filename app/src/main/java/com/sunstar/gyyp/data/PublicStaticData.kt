package com.sunstar.gyyp.data

import com.sunstar.gyyp.base.Preference

class PublicStaticData {
    companion object {
        private var Token = ""
        val tooken: String
            get() {
                if (Token == "") {
                    var string by Preference<String>("token", "")
                    Token = string
                }
                return Token
            }
    }
}