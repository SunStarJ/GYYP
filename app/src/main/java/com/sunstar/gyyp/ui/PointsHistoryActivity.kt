package com.sunstar.gyyp.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.data.RootBean

class PointsHistoryActivity : BaseActivity() {
    var page = 1
    override fun appViewInitComplete() {
        getData()
    }

    private fun getData() {
        OkGo.post<RootBean>(Url.getmyfinancelog)
                .params("pageindex", page)
                .params("pagesize", 20)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {

                    }

                    override fun dataNull() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
    }

    override fun initHeadModel(): HeadVm = HeadVm("资产记录",true,R.mipmap.back)

    override fun initView(): View = View.inflate(mContext,R.layout.activity_points_history,null)
}
