package com.sunstar.gyyp.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.data.RootBean
import kotlinx.android.synthetic.main.activity_artical_list.*

class ArticalListActivity : BaseActivity() {
    var page = 1
    var pageSize = 20
    override fun appViewInitComplete() {
        refresh_view.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page += 1
                initData()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 1
                refresh_view.setNoMoreData(false)
                initData()
            }
        })
        refresh_view.autoRefresh()
    }

    private fun initData() {
        OkGo.post<RootBean>(Url.getarticles)
                .params("pageindex", page)
                .params("pagesize", pageSize)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {

                    }

                    override fun success(it: Response<RootBean>) {

                    }

                    override fun dataNull() {

                    }
                })
    }

    override fun initHeadModel(): HeadVm = HeadVm("国源头条", true, R.mipmap.back)

    override fun initView(): View = View.inflate(mContext, R.layout.activity_artical_list, null)

}
