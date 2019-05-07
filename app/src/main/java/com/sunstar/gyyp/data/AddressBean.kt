package com.sunstar.gyyp.data

import java.io.Serializable

data class AddressBean(
        var addressdetail: String,
        var addressid: Int,
        var name: String,
        var phone: String
): Serializable