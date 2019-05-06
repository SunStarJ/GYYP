package com.sunstar.gyyp.ui.fragment

import android.support.v4.app.Fragment
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.data.RootBean
import kotlinx.android.synthetic.main.refresh_layout.*
import org.jetbrains.anko.support.v4.toast

class OrderListBaseFragment:LazyFragment() {
    override fun initLayoutId(): Int = R.layout.refresh_layout
    var page = 1
    var type = 99
    fun initType(type:Int):OrderListBaseFragment{
        this.type = type
        return this
    }
    override fun loadPageView() {
        refresh_view.setOnRefreshLoadMoreListener(object:OnRefreshLoadMoreListener{
            override fun onLoadMore(refreshLayout: RefreshLayout?) {
                page+=1
                getData()
            }

            override fun onRefresh(refreshLayout: RefreshLayout?) {
                page =1
                getData()
            }
        })
        refresh_view.isEnableLoadMore = false
    }

    private fun getData() {
        OkGo.post<RootBean>(Url.getorders)
                .params("type",type)
                .execute(object:BaseCallBack(){
                    override fun dataError(data: RootBean) {
                        refresh_view.finishRefresh()
                        toast(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        refresh_view.finishRefresh()
                    }

                    override fun dataNull() {
                    }
                })
    }

    override fun lazyLoad() {
        refresh_view.autoRefresh()
    }

}