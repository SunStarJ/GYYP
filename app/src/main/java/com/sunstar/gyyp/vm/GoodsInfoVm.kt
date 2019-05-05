package com.sunstar.gyyp.vm

import android.databinding.BaseObservable
import android.databinding.ObservableField
import android.text.SpannableStringBuilder
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.gyyp.ProjectApplication
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.Util
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.view.GoodsInfoView

class GoodsInfoVm(var mv: GoodsInfoView) : BaseObservable() {
    var data: RootBean? = null
    var onlyPoint: SpannableStringBuilder = SpannableStringBuilder("")
    var showPrice: SpannableStringBuilder = SpannableStringBuilder("")
    var showNum = "1"
    fun initGoodsInfo(id: String) {
        mv.showLoading("获取数据中，请稍候")
        OkGo.post<RootBean>(Url.getproductdetail)
                .params("pid", id)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        mv.hiddenLoading()
                        mv.showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        mv.hiddenLoading()
                        data = it.body()
                        showPrice = Util.changeStringColor(ProjectApplication.instance.applicationContext, "￥${data?.price}　　　积分购：${data?.points}", "积分购：", R.color.color_text_black)
                        onlyPoint = Util.changeStringColor(ProjectApplication.instance.applicationContext, "积分购：${data?.points}", "积分购：", R.color.color_text_black)
                        mv.showBanner(data?.pics!!)
                        mv.loadInfo(data?.content!!)
                        notifyChange()
                    }

                    override fun dataNull() {

                    }
                })
    }

    fun onDestory() {
        OkGo.getInstance().cancelTag(this)
    }

    fun collectionGoods() {
        mv.showLoading("提交数据中，请售后")
        OkGo.post<RootBean>(Url.collectproduct)
                .params("productid", data?.id)
                .params("type", if (data?.iscollected == 1) 0 else 1)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        mv.hiddenLoading()
                        mv.showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        mv.hiddenLoading()
                        data?.iscollected = if (data?.iscollected == 1) 0 else 1
                        mv.showMsg(it.body().msg)
                    }

                    override fun dataNull() {
                    }
                })
    }

    fun buyNowShowPop(){
        mv.showBuyDialog(1)
    }

    fun addToCart() {
        mv.showBuyDialog(0)
    }
    fun numSub(){
        if(showNum.toInt() == 1){
            mv.showMsg("已达到最小值")
        }else{
            showNum = (showNum.toInt()-1).toString()
            mv.showPrice()
        }
    }
    fun numAdd(){
        showNum = (showNum.toInt()+1).toString()
        mv.showPrice()
    }

    fun addCart() {
        buyInCart(0)
    }

    private fun buyInCart(type:Int) {
        mv.showLoading("提交数据中，请稍后")
        OkGo.post<RootBean>(Url.addshopcart)
                .params("num", showNum)
                .params("productid", data?.id)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        mv.hiddenLoading()
                        mv.showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        mv.hiddenLoading()
                        mv.showMsg(it.body().msg)
                        showNum = 1.toString()
                        mv.hideBottom()
                        mv.showPrice()
                        if(type == 1){
                            mv.buyNow()
                        }
                    }

                    override fun dataNull() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
    }

    fun buyNow(){
        buyInCart(1)
    }

}