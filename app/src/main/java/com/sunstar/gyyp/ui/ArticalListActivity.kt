package com.sunstar.gyyp.ui

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.JavaUtil
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.WebActivity
import com.sunstar.gyyp.adapter.AdapterArticalItem
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.ArticlesItem
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.databinding.AdapertAticalItemBinding
import kotlinx.android.synthetic.main.activity_artical_list.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class ArticalListActivity : BaseActivity() {
    var page = 1
    var pageSize = 20
    var listData: ObservableArrayList<ArticlesItem> = ObservableArrayList()
    override fun appViewInitComplete() {
        var adapter = AdapterArticalItem(mContext).initDataList(listData).initBindView(object : SSBaseDataBindingAdapter.BindView<AdapertAticalItemBinding> {
            override fun onBindViewHolder(b: AdapertAticalItemBinding, position: Int) {
                b.data = listData[position]
                b.root.onClick {
                    startActivity<WebActivity>("url" to listData[position].url)
                }
            }
        })
        listData.addOnListChangedCallback(JavaUtil.getListChangedCallback(adapter))
        data_view.adapter = adapter
        data_view.addItemDecoration(DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL))
        data_view.layoutManager = LinearLayoutManager(mContext)
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
                        toast(data.msg)
                        refresh_view.finishLoadMore()
                        refresh_view.finishRefresh()
                    }

                    override fun success(it: Response<RootBean>) {
                        refresh_view.finishLoadMore()
                        refresh_view.finishRefresh()
                        if(page == 1){
                            listData.clear()
                        }
                        for(data in it.body().articles!!){
                            listData.add(data)
                        }
                    }

                    override fun dataNull() {

                    }
                })
    }

    override fun initHeadModel(): HeadVm = HeadVm("国源头条", true, R.mipmap.back)

    override fun initView(): View = View.inflate(mContext, R.layout.activity_artical_list, null)

}
