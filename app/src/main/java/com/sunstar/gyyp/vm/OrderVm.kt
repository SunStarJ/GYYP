package com.sunstar.gyyp.vm

import android.databinding.BaseObservable
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.view.View
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.gyyp.BigDecimalUtils
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.BaseView
import com.sunstar.gyyp.data.AddressBean
import com.sunstar.gyyp.data.AddressListItem
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.ui.LocationListActivity
import com.sunstar.gyyp.view.OrderView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity
import java.math.BigDecimal

class OrderVm(var mv: OrderView) :BaseObservable(){

    var dataList = ObservableArrayList<PreferenceItem>()
    var addressData:AddressListItem?= null
    var addressShowData:AddressBean?=null
    var showPrice = "￥0.0"
    var showPoints = "积分：0.0"
    var shipFee = "￥0.0"
    var shipPoints = "积分：0.0"
    var remark = ""
    var rootBean:RootBean? = null
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(addressData:AddressListItem){
        this.addressData = addressData
        rootBean!!.address.addressdetail = addressData.province+addressData.city+addressData.area
        rootBean!!.address.addressid = addressData.id.toInt()
        rootBean!!.address.name = addressData.name
        rootBean!!.address.phone = addressData.phone
        notifyChange()
    }

    fun commitOrderData(){
        mv.showLoading("提交数据中请稍候")
        var commitList = mutableListOf<ShopCartVm.CommitData>()
        for (data in rootBean!!.products!!){
            var data = ShopCartVm.CommitData(data.shoppingcartid,data.count)
            commitList.add(data)
        }
        OkGo.post<RootBean>(Url.makeorder)
                .params("orderinfo",Gson().toJson(commitList))
                .params("remark",remark)
                .params("addressid",rootBean!!.address.addressid)
                .execute(object:BaseCallBack(){
                    override fun dataError(data: RootBean) {
                        mv.hiddenLoading()
                        mv.showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        mv.hiddenLoading()
                        it.body().orderno
                        mv.commitComplete()
                        EventBus.getDefault().post("order_commit_complete")
                    }

                    override fun dataNull() {

                    }
                })
    }

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

    fun initData(rootBean:RootBean) {
        this.rootBean = rootBean
        dataList.clear()
        for (innerData in rootBean.products!!){
            dataList.add(innerData)
        }
        addressShowData = rootBean.address
    }


}