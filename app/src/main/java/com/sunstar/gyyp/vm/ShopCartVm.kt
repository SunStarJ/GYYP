package com.sunstar.gyyp.vm

import android.databinding.*
import android.text.SpannableStringBuilder
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.gyyp.BigDecimalUtils
import com.sunstar.gyyp.ProjectApplication
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.Util
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.view.ShopCartView

class ShopCartVm(var mv: ShopCartView) : BaseObservable() {
    var dataList = ObservableArrayList<PreferenceItem>()

    fun initConfig() {
        totalMoney.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                showPrice = Util.changeStringColor(ProjectApplication.instance.applicationContext, "共：￥${totalMoney.get()}", "￥${totalMoney.get()}", R.color.color_red)
                notifyChange()
            }
        })
        totalCont.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                showCount = "(${totalCont.get()}积分)"
                notifyChange()
            }
        })
        isDelete.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                notifyChange()
            }
        })
    }

    var isDelete = ObservableBoolean(false)
    var isSelect = false
    var totalMoney = ObservableDouble(-1.0)
    var totalCont = ObservableDouble(-1.0)
    var showPrice: SpannableStringBuilder? = null
    var showCount = ""
    fun listrStatusChange() {
        var caluateMoney = 0.0
        var caluateCount = 0.0
        var selectNum = 0
        for (data in dataList) {
            data.run {
                if (isSelect) {
                    selectNum +=1
                    var dataMoney = BigDecimalUtils.mul(data.price.toString(), data.num.toString(), 2)
                    caluateMoney = BigDecimalUtils.add(caluateMoney.toString(), dataMoney, 2).toDouble()
                    var dataCount = BigDecimalUtils.mul(data.points.toString(), data.num.toString(), 2)
                    caluateCount = BigDecimalUtils.add(caluateCount.toString(), dataCount, 2).toDouble()
                }
            }
        }
        isSelect = selectNum == dataList.size
        mv.showAllSelect(isSelect)
        totalMoney.set(caluateMoney)
        totalCont.set(caluateCount)
    }

    fun goBuy() {

        if (isDelete.get()) {
            var deleteIds = ""
            for( data in dataList){
                if(data.isSelect){
                    deleteIds+="${data.id},"
                }
            }
            if(deleteIds.isNotEmpty()) deleteIds = deleteIds.substring(0,deleteIds.length-1)
            if(deleteIds == ""){
                mv.showMsg("未选择要删除的商品")
                return
            }
            mv.showLoading("提交数据中，请稍后")
            OkGo.post<RootBean>(Url.deleteshopcart)
                    .params("ids", deleteIds)
                    .execute(object : BaseCallBack() {
                        override fun dataError(data: RootBean) {
                            mv.hiddenLoading()
                            mv.showMsg(data.msg)
                        }

                        override fun success(it: Response<RootBean>) {
                            mv.hiddenLoading()
                            mv.showMsg(it.body().msg)
                            for(i in dataList.size-1 downTo 0){
                                if(dataList[i].isSelect){
                                    dataList.removeAt(i)
                                }
                            }
                        }

                        override fun dataNull() {
                        }
                    })
        } else {

        }
    }

    fun selectAll() {}
    fun getData() {
        mv.showLoading("加载数据中，请稍后")
        OkGo.post<RootBean>(Url.getshopcart)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        mv.hiddenLoading()
                        mv.showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        mv.hiddenLoading()
                        dataList.clear()
                        for (data in it.body().products!!) {
                            data.changeShowNum()
                            dataList.add(data)
                        }
                        totalMoney.set(0.0)
                        totalCont.set(0.0)
                    }

                    override fun dataNull() {

                    }
                })
    }

    fun numberChange(position: Int, type: Int) {
        mv.showLoading("提交数据中，请稍后")
        OkGo.post<RootBean>(Url.changeshopcartnum)
                .params("id", dataList[position].id)
                .params("type", type)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        mv.hiddenLoading()
                        mv.showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        mv.hiddenLoading()
                        mv.showMsg(it.body().msg)
                        if (type == 1) {
                            var data = dataList[position]
                            data.num += 1
                            data.changeShowNum()
                            this@ShopCartVm.dataList[position] = data
                            listrStatusChange()
                        } else if (type == 2) {
                            var data = dataList[position]
                            data.num -= 1
                            data.changeShowNum()
                            this@ShopCartVm.dataList[position] = data
                            listrStatusChange()
                        }
                    }

                    override fun dataNull() {

                    }
                })
    }
}