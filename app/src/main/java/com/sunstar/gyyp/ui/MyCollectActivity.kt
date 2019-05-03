package com.sunstar.gyyp.ui

import android.databinding.ObservableArrayList
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
import com.sunstar.gyyp.adapter.CollectAdapter
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.databinding.AdapterMyCollectLatoutBinding
import kotlinx.android.synthetic.main.activity_my_collect.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MyCollectActivity : BaseActivity() {
    var page = 1
    var dataList = ObservableArrayList<PreferenceItem>()
    override fun appViewInitComplete() {
        initBodyView()
        initListener()
        refresh_view.autoRefresh()
    }

    private fun initBodyView() {
        var adapter = CollectAdapter(mContext).initDataList(dataList).initBindView(object:SSBaseDataBindingAdapter.BindView<AdapterMyCollectLatoutBinding>{
            override fun onBindViewHolder(b: AdapterMyCollectLatoutBinding, position: Int) {
                b.data = dataList[position]
                b.delete.onClick {
                    deleteData(position)
                }
                b.body.onClick {
                    startActivity<GoodsInfoActivity>("id" to dataList[position].id.toString())
                }
            }
        }) as CollectAdapter
        dataList.addOnListChangedCallback(JavaUtil.getListChangedCallback(adapter))
        data_view.adapter = adapter
        data_view.layoutManager = LinearLayoutManager(mContext)
        data_view.addItemDecoration(DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL))
    }

    private fun deleteData(position: Int) {
        showLoading("提交数据中，请稍后")
        OkGo.post<RootBean>(Url.collectproduct)
                .params("productid",dataList[position].id)
                .params("type",2)
                .execute(object:BaseCallBack(){
                    override fun dataError(data: RootBean) {
                        hiddenLoading()
                        toast(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        hiddenLoading()
                        toast(it.body().msg)
                        dataList.removeAt(position)
                    }

                    override fun dataNull() {

                    }
                })
    }

    private fun initListener() {
        refresh_view.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout?) {
                page += 1
                getData(refreshLayout!!)
            }

            override fun onRefresh(refreshLayout: RefreshLayout?) {
                refreshLayout!!.setNoMoreData(false)
                page = 1
                getData(refreshLayout)
            }
        })
    }

    private fun getData(refreshLayout: RefreshLayout) {
        OkGo.post<RootBean>(Url.getcollects)
                .params("pageindex",page)
                .params("pagesize",20)
                .execute(object:BaseCallBack(){
                    override fun dataError(data: RootBean) {
                        refreshLayout.run {
                            finishRefresh()
                            finishLoadMore()
                        }
                        showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        refreshLayout.run {
                            finishRefresh()
                            finishLoadMore()
                        }
                        if(page == 1){
                            dataList.clear()
                        }
                        for(data in it.body().products!!){
                            dataList.add(data)
                        }
                        if(it.body().products!!.size<20){
                            refreshLayout.setNoMoreData(true)
                        }
                    }

                    override fun dataNull() {

                    }
                })
    }

    override fun initHeadModel(): HeadVm = HeadVm("我的收藏", true, R.mipmap.back)

    override fun initView(): View = View.inflate(mContext, R.layout.activity_my_collect, null)

}
