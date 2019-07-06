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
import com.sunstar.gyyp.adapter.PointHistoryAdapter
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.FinanaceLogData
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.databinding.AdapterCostOrReceiveHistoryBinding
import kotlinx.android.synthetic.main.activity_points_history.*
import org.jetbrains.anko.textColor

class PointsHistoryActivity : BaseActivity() {
    var page = 1
    var dataList: ObservableArrayList<FinanaceLogData>? = ObservableArrayList()
    override fun appViewInitComplete() {
        var adapter = PointHistoryAdapter(mContext).initDataList(dataList!!).initBindView(object : SSBaseDataBindingAdapter.BindView<AdapterCostOrReceiveHistoryBinding> {
            override fun onBindViewHolder(b: AdapterCostOrReceiveHistoryBinding, position: Int) {
                var data = dataList!![position]
                b.data = data
                b.point.textColor = if(data.fpoint >= 0.0)  mContext.resources.getColor(R.color.color_green) else mContext.resources.getColor(R.color.color_red)
            }
        })
        data_view.adapter = adapter
        data_view.layoutManager = LinearLayoutManager(mContext)
        data_view.addItemDecoration(DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL))
        dataList!!.addOnListChangedCallback(JavaUtil.getListChangedCallback(adapter))
        refresh_view.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout?) {
                page += 1
                getData()
            }

            override fun onRefresh(refreshLayout: RefreshLayout?) {
                page = 1
                refreshLayout?.setNoMoreData(false)
                getData()
            }
        })
        refresh_view.autoRefresh()
    }

    private fun getData() {
        OkGo.post<RootBean>(Url.getmyfinancelog)
                .params("pageindex", page)
                .params("pagesize", 20)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        refresh_view?.run {
                            finishRefresh()
                            finishLoadMore()
                        }
                        showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        refresh_view?.run {
                            finishRefresh()
                            finishLoadMore()
                        }
                        if (it.body().code == 0) {
                            for (data in it.body().financelog!!) {
                                dataList?.add(data)
                            }
                        }
                    }

                    override fun dataNull() {

                    }
                })
    }

    override fun initHeadModel(): HeadVm = HeadVm("资产记录", true, R.mipmap.back)

    override fun initView(): View = View.inflate(mContext, R.layout.activity_points_history, null)
}
