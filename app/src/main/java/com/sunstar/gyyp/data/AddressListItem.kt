package com.sunstar.gyyp.data

import java.io.Serializable

data class AddressListItem(var area: String = "",
                           var province: String = "",
                           var addressdetail: String = "",
                           var phone: String = "",
                           var city: String = "",
                           var name: String = "",
                           var id: String = "",
                           var isdefault: Int = 0):Serializable