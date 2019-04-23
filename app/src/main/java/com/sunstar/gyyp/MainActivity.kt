package com.sunstar.gyyp

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ViewDataBinding
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.adapter.MainAdapter
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.BaseMuiltAdapter
import com.sunstar.gyyp.data.BannerData
import com.sunstar.gyyp.data.SecondData
import com.sunstar.gyyp.databinding.ActivityMainBinding
import com.sunstar.gyyp.databinding.AdapterMainBannerLayoutBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.info

class MainActivity : BaseActivity() {

    var bindingData: ActivityMainBinding? = null
    override fun appViewInitComplete() {
        sofia?.let {
            it.statusBarBackground(mContext.resources.getColor(android.R.color.transparent)).invasionStatusBar()
        }
        hiddenTitleBar()
        search_layout.setPadding(search_layout.paddingLeft, search_layout.paddingTop + getStatusBarHeight(), search_layout.paddingRight, search_layout.paddingBottom)
        search_layout.backgroundColor = mContext.resources.getColor(R.color.alpha_20_black)
        initListener()
        initCurView()
    }

    var list =ObservableArrayList<BaseMuiltAdapter.MuiltAdapterBaseData<ViewDataBinding>>()

    private lateinit var adapter: MainAdapter<ViewDataBinding>

    private fun initCurView() {
        list.add(BannerData())
        list.add(SecondData())
        adapter = MainAdapter(mContext)
        adapter.initList(list)
        data_view.adapter = adapter
        data_view.layoutManager = LinearLayoutManager(mContext)
    }

    private fun initListener() {
        data_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                info { "dy$dy" }
            }
        })
    }

    override fun initHeadModel(): HeadVm = HeadVm("", false, -1);

    override fun initView(): View {
        bindingData = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.activity_main, null, false)
        return bindingData?.root!!
    }
}
