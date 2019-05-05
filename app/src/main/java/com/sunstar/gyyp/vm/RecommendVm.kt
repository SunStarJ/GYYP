package com.sunstar.gyyp.vm

import android.databinding.BaseObservable
import android.text.SpannableStringBuilder
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.gyyp.ProjectApplication
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.BaseView
import com.sunstar.gyyp.base.Util
import com.sunstar.gyyp.data.RootBean

class RecommendVm : BaseObservable() {
    var mv: BaseView? = null
    var recommendData: RootBean? = null
    var userImg = ""
    var userName = ""
    var userPhone = ""
    var recommpoin: SpannableStringBuilder? = null
    var recommcount: SpannableStringBuilder? = null

    fun initBaseView(mv: BaseView) {
        this.mv = mv
    }

    fun initUserData(data: RootBean) {
        userImg = data.headpic
        userName = data.nickname
        userPhone = data.u_annex
        notifyChange()
    }

    fun getRecommendData() {
        mv?.showLoading("获取数据中，请稍候")
        OkGo.post<RootBean>(Url.getmyrecomm)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        mv?.run {
                            hiddenLoading()
                            showMsg(data.msg)
                        }
                    }

                    override fun success(it: Response<RootBean>) {
                        mv?.hiddenLoading()
                        recommendData = it.body()
                        initRecommendData()
                    }

                    override fun dataNull() {
                    }
                })
    }

    private fun initRecommendData() {
        recommendData?.let {
            recommpoin = Util.changeStringColor(ProjectApplication.instance.applicationContext, "${it.recommpoin}\n\n我的推荐奖", it.recommpoin, R.color.color_red)
            recommcount = Util.changeStringColor(ProjectApplication.instance.applicationContext, "${it.recommcount}\n\n我的推荐奖", "${it.recommcount}", R.color.color_red)
            notifyChange()
        }

    }
}