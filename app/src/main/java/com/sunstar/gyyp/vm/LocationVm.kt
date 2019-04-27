package com.sunstar.gyyp.vm

import android.databinding.BaseObservable
import android.databinding.ObservableArrayList
import android.location.Address
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.BaseView
import com.sunstar.gyyp.base.Util
import com.sunstar.gyyp.data.AddressListItem
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.view.EditLocationView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LocationVm(var mv: BaseView) : BaseObservable() {
    var receiverName: String = ""
    var receiverPhone: String = ""
    var province: String = ""
    var city: String = ""
    var area: String = ""
    var addressdetail: String = ""
    var cityShow = ""
    var locationId = ""
    var addressList = ObservableArrayList<AddressListItem>()
    fun initCityShow() {
        cityShow = "$province  $city  $area"
        notifyChange()
    }

    init {
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(data: AddressListItem) {
        var isHase = false
        var setDeauflt  = addressList.size == 0
        for (i in 0 until addressList.size) {
            if (addressList[i].id == data.id) {
                isHase = true
                addressList[i] = data
            }
        }
        if (!isHase) {
            addressList.add(data)
        }
        if (setDeauflt) {
            changeDefault(0)
        }
    }

    private fun changeDataToDefault(data: AddressListItem) {

    }

    fun onDestory() {
        EventBus.getDefault().unregister(this)
    }

    fun saveLocation() {
        if (receiverName == "") {
            mv.showMsg("请输入收货人姓名")
            return
        }
        if (receiverPhone == "") {
            mv.showMsg("请输入电话号")
            return
        } else if (!Util.isMobileNO(receiverPhone)) {
            mv.showMsg("请检查电话格式")
            return
        }
        if (province == "") {
            mv.showMsg("请选择省市县地址")
            return
        }
        if (area == "") {
            mv.showMsg("请输入详细地址")
            return
        }
        mv.showLoading("提交数据中，请稍候")
        OkGo.post<RootBean>(Url.editdeliveryaddress)
                .params("id", locationId)
                .params("name", receiverName)
                .params("phone", receiverPhone)
                .params("province", province)
                .params("city", city)
                .params("area", area)
                .params("addressdetail", addressdetail)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        mv.hiddenLoading()
                        mv.showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        mv.hiddenLoading()
                        (mv as EditLocationView).saveComplete()
                        EventBus.getDefault().post(AddressListItem(area = area, id = locationId, name = receiverName,
                                phone = receiverPhone, addressdetail = addressdetail
                                , city = city, province = province, isdefault = 0))
                    }

                    override fun dataNull() {

                    }
                })
    }

    fun getLocationLis() {
        mv.showLoading("获取数据中，请稍后")
        OkGo.post<RootBean>(Url.getdeliveryaddress)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        mv.hiddenLoading()
                        mv.showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        mv.hiddenLoading()

                        for (data in it.body().addresslist!!) {
                            addressList.add(data)
                        }
                        notifyChange()
                    }

                    override fun dataNull() {

                    }
                })
    }

    fun changeDefault(position: Int) {
        mv.showLoading("提交数据中，请稍候")
        OkGo.post<RootBean>(Url.setaddressdefault)
                .params("id", addressList[position].id)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        mv.hiddenLoading()
                        mv.showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        mv.hiddenLoading()
                        for (i in 0 until addressList.size) {
                            var data = addressList[i]
                            data.isdefault = if (i == position) 1 else 0
                            addressList[i] = data
                        }

                    }

                    override fun dataNull() {

                    }
                })
    }

    fun deleteLocation(position: Int) {
        if (addressList[position].isdefault == 1) {
            mv.showMsg("默认收货地址不可删除")
            return
        }
        mv.showLoading("提交数据中，请稍候")
        OkGo.post<RootBean>(Url.deleteaddress).params("id", addressList[position].id)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        mv.hiddenLoading()
                        mv.showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        mv.hiddenLoading()
                        addressList.removeAt(position)
                    }

                    override fun dataNull() {

                    }
                })

    }

    fun initData(it: AddressListItem) {
        locationId = it.id
        province = it.province
        city = it.city
        area = it.area
        addressdetail = it.addressdetail
        receiverName = it.name
        receiverPhone = it.phone
        initCityShow()
        notifyChange()
    }

}