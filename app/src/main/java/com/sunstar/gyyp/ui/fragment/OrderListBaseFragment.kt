package com.sunstar.gyyp.ui.fragment

import android.content.Context
import android.databinding.ObservableArrayList
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.sunstar.gyyp.JavaUtil
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.adapter.OrderListAdapter
import com.sunstar.gyyp.adapter.OrderListChildAdapter
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.OrderBean
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.databinding.AdapterOrderChildItemBinding
import com.sunstar.gyyp.databinding.AdapterOrderFatherItemBinding
import kotlinx.android.synthetic.main.refresh_layout.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.toast

class OrderListBaseFragment:LazyFragment() {
    override fun initLayoutId(): Int = R.layout.refresh_layout
    var dataList = ObservableArrayList<OrderBean>()
    var page = 1
    var type = 99
    fun initType(type:Int):OrderListBaseFragment{
        this.type = type
        return this
    }
    override fun loadPageView() {
        var adapter = OrderListAdapter(activity as Context).initDataList(dataList)
                .initBindView(object:SSBaseDataBindingAdapter.BindView<AdapterOrderFatherItemBinding>{
                    override fun onBindViewHolder(b: AdapterOrderFatherItemBinding, position: Int) {
                        b.orderData = dataList[position]
                        when(dataList[position].state){
                            0->{
                                b.secondText.text = "取消订单"
                                b.mainText.text = "付款"
                                b.secondText.visibility = View.VISIBLE
                                b.mainText.visibility = View.VISIBLE
                            }
                            1->{
                                b.secondText.visibility = View.GONE
                                b.mainText.visibility = View.GONE
                            }
                            2->{
                                b.secondText.visibility = View.GONE
                                b.mainText.visibility = View.VISIBLE
                                b.mainText.text = "确认收货"
                            }
                            3->{
                                b.secondText.visibility = View.VISIBLE
                                b.mainText.visibility = View.GONE
                                b.secondText.text = "删除订单"
                            }
                            5->{
                                b.secondText.visibility = View.VISIBLE
                                b.mainText.visibility = View.GONE
                                b.secondText.text = "删除订单"
                            }
                        }
                        b.secondText.onClick {
                            when(dataList[position].state){
                                0->{ cacleOrderList(position)}
                                3->{ deleteOrderList(position)}
                                5->{ deleteOrderList(position)}
                            }

                        }
                        b.mainText.onClick {  }
                        var adapter = OrderListChildAdapter(mContext = activity as Context).initDataList(dataList[position].details)
                                .initBindView(object:SSBaseDataBindingAdapter.BindView<AdapterOrderChildItemBinding>{
                                    override fun onBindViewHolder(b: AdapterOrderChildItemBinding, p2: Int) {
                                        b.data = dataList[position].details[p2]
                                    }
                                })
                        b.innerDataView.adapter = adapter
                        b.innerDataView.layoutManager = LinearLayoutManager(activity as Context)
                        var divider = DividerItemDecoration(activity as Context,DividerItemDecoration.VERTICAL)
                        divider.setDrawable(activity!!.resources.getDrawable(R.drawable.divider_bg))
                        b.innerDataView.addItemDecoration(divider)
                    }
                })
        data_view.adapter = adapter
        data_view.layoutManager = LinearLayoutManager(activity as Context)
        dataList.addOnListChangedCallback(JavaUtil.getListChangedCallback(adapter))
        data_view.addItemDecoration(DividerItemDecoration(activity as Context,DividerItemDecoration.VERTICAL))
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

    private fun deleteOrderList(position: Int) {
        (activity as BaseActivity).showLoading("提交数据中，请稍候")
        OkGo.post<RootBean>(Url.deleteorder)
                .params("orderid",dataList[position].id)
                .execute(object:BaseCallBack(){
                    override fun dataError(data: RootBean) {
                        (activity as BaseActivity).hiddenLoading()
                        (activity as BaseActivity).showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        (activity as BaseActivity).showMsg(it.body().msg)
                        (activity as BaseActivity).hiddenLoading()
                        dataList.removeAt(position)
                    }

                    override fun dataNull() {

                    }
                })
    }

    private fun cacleOrderList(position: Int) {
        (activity as BaseActivity).showLoading("提交数据中，请稍候")
        OkGo.post<RootBean>(Url.cancelorder)
                .params("orderid",dataList[position].id)
                .execute(object:BaseCallBack(){
                    override fun dataError(data: RootBean) {
                        (activity as BaseActivity).hiddenLoading()
                        (activity as BaseActivity).showMsg(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        (activity as BaseActivity).showMsg(it.body().msg)
                        (activity as BaseActivity).hiddenLoading()
                        if(type == 99){
                            var data = dataList[position]
                            data.state = 5
                            dataList[position] = data
                        }else dataList.removeAt(position)

                    }

                    override fun dataNull() {

                    }
                })
    }

    private fun getData() {
        OkGo.post<RootBean>(Url.getorders)
                .params("type",type)
                .execute(object:BaseCallBack(){
                    override fun dataError(data: RootBean) {
                        refresh_view?.finishRefresh()
                        toast(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        refresh_view?.finishRefresh()
                        dataList.clear()
                        for (data in it.body().orders!!){
                            dataList.add(data)
                        }
                    }

                    override fun dataNull() {
                    }
                })
    }

    override fun lazyLoad() {
        refresh_view.autoRefresh()
    }

}