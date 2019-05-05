package com.sunstar.gyyp.vm

import android.databinding.BaseObservable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.view.View
import com.sunstar.gyyp.BigDecimalUtils
import com.sunstar.gyyp.data.AddressListItem
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.ui.LocationListActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity
import java.math.BigDecimal

class OrderVm :BaseObservable(){

    var dataList = ObservableArrayList<PreferenceItem>()
    var addressData:AddressListItem?= null
    var showPrice = "￥0.0"
    var showPoints = "积分：0.0"
    var shipFee = "￥0.0"
    var shipPoints = "积分：0.0"
    var remark = ""
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(addressData:AddressListItem){
        this.addressData = addressData
        notifyChange()
    }

    fun commitOrderData(){}

    init {
        EventBus.getDefault().register(this)
    }

    fun onDestory(){
        EventBus.getDefault().unregister(this)
    }

    fun initList(dataList:MutableList<PreferenceItem>){
        this.dataList.clear()
        for(data in dataList){
            this.dataList.add(data)
        }
        var totalPrice = 0.0
        var totalPoints = 0.0
        for (data in dataList){
            totalPrice = BigDecimalUtils.add(totalPrice.toString(),BigDecimalUtils.mul(data.price.toString(),data.num.toString(),2),2).toDouble()
            totalPoints = BigDecimalUtils.add(totalPoints.toString(),BigDecimalUtils.mul(data.points.toString(),data.num.toString(),2),2).toDouble()
        }
        showPrice = "￥$totalPrice"
        showPoints = "积分：$totalPoints"
        notifyChange()
    }

    fun selectAddressList (view: View){
        view.context.startActivity<LocationListActivity>("selectType" to 1)
    }


}