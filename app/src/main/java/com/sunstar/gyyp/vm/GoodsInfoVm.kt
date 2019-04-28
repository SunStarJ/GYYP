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

class GoodsInfoVm (var mv:GoodsInfoView):BaseObservable(){
    var data:RootBean?= null
    var showPrice : SpannableStringBuilder= SpannableStringBuilder("")
    fun initGoodsInfo(id:String){
        mv.showLoading("获取数据中，请稍候")
        OkGo.post<RootBean>(Url.getproductdetail)
                .params("pid",id)
                .execute(object :BaseCallBack(){
                    override fun dataError(data: RootBean) {
                        mv.hiddenLoading()
                        mv.showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        mv.hiddenLoading()
                        data = it.body()
                        showPrice = Util.changeStringColor(ProjectApplication.instance.applicationContext,"￥${data?.price}　　　积分购：${data?.points}","积分购：", R.color.color_text_black)
                        mv.showBanner(data?.pics!!)
                        mv.loadInfo(data?.content!!)
                        notifyChange()
                    }

                    override fun dataNull() {

                    }
                })
    }

    fun onDestory(){
        OkGo.getInstance().cancelTag(this)
    }

    fun  collectionGoods(){
        mv.showLoading("提交数据中，请售后")
        OkGo.post<RootBean>(Url.collectproduct)
                .params("productid",data?.id)
                .params("type",if( data?.iscollected == 1) 0 else 1)
                .execute(object :BaseCallBack(){
                    override fun dataError(data: RootBean) {
                        mv.hiddenLoading()
                        mv.showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        mv.hiddenLoading()
                        data?.iscollected = if( data?.iscollected == 1) 0 else 1
                        mv.showMsg(it.body().msg)
                    }

                    override fun dataNull() {
                    }
                })
    }

}