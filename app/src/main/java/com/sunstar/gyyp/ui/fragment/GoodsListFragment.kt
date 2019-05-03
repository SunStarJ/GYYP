package com.sunstar.gyyp.ui.fragment

import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.support.v7.widget.GridLayoutManager
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.sunstar.gyyp.JavaUtil
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.adapter.GoodsListGridAdapter
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.databinding.AdapterGoodsGridListBinding
import com.sunstar.gyyp.ui.GoodsInfoActivity
import com.sunstar.gyyp.ui.GoodsListPageActivity
import com.sunstar.gyyp.util.MDGridRvDividerDecoration
import kotlinx.android.synthetic.main.fragment_goods_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivity

class GoodsListFragment : LazyFragment(), AnkoLogger {
    var id = ""
    var dataList = ObservableArrayList<PreferenceItem>()
    fun initId(id: String): GoodsListFragment {
        this.id = id
        return this
    }

    override fun loadPageView() {
        var adapter = GoodsListGridAdapter(activity as Context).initDataList(dataList).initBindView(object : SSBaseDataBindingAdapter.BindView<AdapterGoodsGridListBinding> {
            override fun onBindViewHolder(b: AdapterGoodsGridListBinding, position: Int) {
                b.data = dataList[position]
                b.root.onClick {
                    startActivity<GoodsInfoActivity>("id" to dataList[position].id.toString())
                }
            }
        })
        dataList.addOnListChangedCallback(JavaUtil.getListChangedCallback(adapter))
        data_view.adapter = adapter
        data_view.addItemDecoration(MDGridRvDividerDecoration(activity as Context))
        data_view.layoutManager = GridLayoutManager(activity as Context, 2)
        initListener()

    }

    var page = 1

    private fun initListener() {
        refresh_view.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                page += 1
                getData(refreshLayout)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                refresh_view.setNoMoreData(false)
                page = 1
                getData(refreshLayout)
            }
        })
    }

    private fun getData(refreshLayout: RefreshLayout) {
        (activity as GoodsListPageActivity).run {
            OkGo.post<RootBean>(Url.getproducts)
                    .params("pageindex", page)
                    .params("pagesize", 20)
                    .params("categoryid", id)
                    .params("sort", sort)
                    .params("keyword", keyWord)
                    .params("hot", hot)
                    .params("sec", sec)
                    .execute(object : BaseCallBack() {
                        override fun dataError(data: RootBean) {
                            refreshLayout.finishRefresh()
                            refreshLayout.finishLoadMore()
                            showMsg(data.msg)
                        }

                        override fun success(it: Response<RootBean>) {
                            refreshLayout.finishRefresh()
                            refreshLayout.finishLoadMore()
                            if (page == 1) {
                                dataList.clear()
                            }
                            for (data in it.body().products!!) {
                                dataList.add(data)
                            }
                            if (it.body().products!!.size < 20) {
                                refreshLayout.setNoMoreData(true)
                            }

                        }

                        override fun dataNull() {

                        }
                    })
        }
    }

    override fun initLayoutId(): Int = R.layout.fragment_goods_list

    override fun lazyLoad() {
        refresh_view.autoRefresh()
    }

    fun outLoadData() {
        refresh_view?.autoRefresh()
    }
}