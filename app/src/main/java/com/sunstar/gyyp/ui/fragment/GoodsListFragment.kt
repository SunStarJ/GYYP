package com.sunstar.gyyp.ui.fragment

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.ui.GoodsListPageActivity
import kotlinx.android.synthetic.main.fragment_goods_list.*

class GoodsListFragment:LazyFragment() {
    var id = ""
    var dataList = ObservableArrayList<PreferenceItem>()
    fun initId(id:String):GoodsListFragment{
        this.id = id
        return this
    }

    override fun loadPageView() {
        initListener()
    }

    var page = 1

    private fun initListener() {
        refresh_view.setOnRefreshLoadMoreListener(object:OnRefreshLoadMoreListener{
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page += 1
                getData()
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 1
                getData()
            }
        })
    }

    private fun getData() {
        (activity as GoodsListPageActivity).run {
            OkGo.post<RootBean>(Url.getproducts)
                    .params("pageindex",page)
                    .params("pagesize",20)
                    .params("categoryid",id)
                    .params("sort",sort)
                    .params("keyword",keyWord)
                    .params("hot",hot)
                    .params("sec",sec)
                    .execute(object:BaseCallBack(){
                        override fun dataError(data: RootBean) {
                            refresh_view.finishRefresh()
                            refresh_view.finishLoadMore()
                            showMsg(data.msg)
                        }

                        override fun success(it: Response<RootBean>) {
                            refresh_view.finishRefresh()
                            refresh_view.finishLoadMore()
                            if(page == 0){
                                dataList.clear()
                            }
                            for(data in it.body().products!!){
                                dataList.add(data)
                            }
                            if(it.body().products!!.size<20){
                                refresh_view.setNoMoreData(true)
                            }
                        }

                        override fun dataNull() {

                        }
                    })
        }
    }

    override fun initLayoutId(): Int = R.layout.fragment_goods_list

    override fun lazyLoad() {

    }
}