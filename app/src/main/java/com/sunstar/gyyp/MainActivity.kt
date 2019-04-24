package com.sunstar.gyyp

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ViewDataBinding
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.adapter.MainAdapter
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.BaseMuiltAdapter
import com.sunstar.gyyp.data.ControlData
import com.sunstar.gyyp.data.PublicStaticData
import com.sunstar.gyyp.vm.BannerVM
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.data.SecondData
import com.sunstar.gyyp.databinding.ActivityMainBinding
import com.sunstar.gyyp.ui.LoginActivity
import com.sunstar.gyyp.vm.MainControlVm
import com.sunstar.gyyp.vm.MainListVM
import com.sunstar.gyyp.vm.MainTextSwitcherVM
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.info
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {

    var bindingData: ActivityMainBinding? = null
    override fun appViewInitComplete() {
        sofia?.let {
            it.statusBarBackground(mContext.resources.getColor(android.R.color.transparent)).invasionStatusBar().statusBarLightFont()
        }
        hiddenTitleBar()
        search_layout.setPadding(search_layout.paddingLeft, search_layout.paddingTop + getStatusBarHeight(), search_layout.paddingRight, search_layout.paddingBottom)
        search_layout.backgroundColor = mContext.resources.getColor(R.color.alpha_20_black)
        initListener()
        initCurView()
        initData()
    }

    private fun initData() {
        showLoading("加载数据中，请稍候")
        OkGo.post<RootBean>(Url.getMainPage).execute(object : BaseCallBack() {
            override fun dataError(data: RootBean) {
                toast(data.msg)
            }

            override fun success(it: Response<RootBean>) {
                list[0] = BannerVM<ViewDataBinding>().initBanner(it.body().banner!!)
                list[1] = MainTextSwitcherVM<ViewDataBinding>().initAdList(it.body().articles!!)
                for (data in it.body().preference!!) {
                    list.add(MainListVM(data))
                }
                hiddenLoading()
            }

            override fun dataNull() {
                hiddenLoading()
            }

        })
    }

    var list = ObservableArrayList<BaseMuiltAdapter.MuiltAdapterBaseData<ViewDataBinding>>()

    private lateinit var adapter: MainAdapter<ViewDataBinding>

    private fun initCurView() {
        list.add(BannerVM())
        list.add(MainTextSwitcherVM())
        var controlList = mutableListOf<ControlData>()
        controlList.add(ControlData("精选商品", R.mipmap.jingxuansahngpin))
        controlList.add(ControlData("限时秒杀", R.mipmap.tejiamiaosha))
        controlList.add(ControlData("活动专区", R.mipmap.huodongzhuanqu))
        controlList.add(ControlData("积分兑换", R.mipmap.jijfenduihuan))
        var control = MainControlVm()
        control.initControlList(controlList)
        list.add(control)
        adapter = MainAdapter(mContext)
        adapter.initList(list)
        data_view.adapter = adapter
        data_view.layoutManager = LinearLayoutManager(mContext)
        list.addOnListChangedCallback(JavaUtil.getListChangedCallback(adapter))
    }

    var colorId: Int = R.color.alpha_20_black

    private fun initListener() {
        data_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                var layoutManeger = recyclerView!!.layoutManager
                if (layoutManeger is LinearLayoutManager) {
                    layoutManeger as LinearLayoutManager
                    var position = layoutManeger.findFirstVisibleItemPosition()
                    if (position == 1) {
                        colorId = R.color.colorAccent
                        search_layout.backgroundColor = mContext.resources.getColor(colorId)
                        info { "深色" }
                    } else if (position == 0) {
                        info { "浅色" }
                        colorId = R.color.alpha_20_black
                        search_layout.backgroundColor = mContext.resources.getColor(colorId)
                    }
                }
            }
        })
        mine_center.onClick {
            if(PublicStaticData.tooken == ""){
                startActivity<LoginActivity>()
            }
        }
    }

    override fun initHeadModel(): HeadVm = HeadVm("", false, -1);

    override fun initView(): View {
        bindingData = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.activity_main, null, false)
        return bindingData?.root!!
    }
}