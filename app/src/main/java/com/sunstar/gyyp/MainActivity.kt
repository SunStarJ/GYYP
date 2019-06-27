package com.sunstar.gyyp

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.adapter.MainAdapter
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.base.BaseMuiltAdapter
import com.sunstar.gyyp.data.ControlData
import com.sunstar.gyyp.data.PublicStaticData
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.databinding.ActivityMainBinding
import com.sunstar.gyyp.model.PayModel
import com.sunstar.gyyp.ui.LoginActivity
import com.sunstar.gyyp.ui.SearchPageActivity
import com.sunstar.gyyp.ui.UserCenterActivity
import com.sunstar.gyyp.ui.VipCenterActivity
import com.sunstar.gyyp.vm.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.pay_way_dialog.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.info
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onScrollChange
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import com.sunstar.gyyp.ui.*


class MainActivity : BaseActivity() {

    var bindingData: ActivityMainBinding? = null
    override fun appViewInitComplete() {
//        sofia?.let {
//            it.statusBarBackground(mContext.resources.getColor(android.R.color.transparent)).invasionStatusBar().statusBarLightFont()
//        }
        if (!isTaskRoot) {
            /* If this is not the root activity */
            var intent = intent;
            var action = intent.action;
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == action) {
                finish()
                return
            }
        }
        var window = (mContext as Activity).getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        PayModel.initE()
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
                list.add(MainImageAdapterVM(it.body().pic1))
                list.add(MainPreferenceVM(it.body().preference!!))
                list.add(MainHotmarketVM(it.body().hotmarket!!))
                list.add(MainImageAdapterVM(it.body().pic2))
                list.add(MainPreferenceVM(it.body().hotproducts!!))
                for(data in it.body().recommends!!){
                    list.add(MainRecomendVm(data))
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
        scroll_view.onScrollChange { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            info { scrollY }
            if(scrollY>300&&colorId == R.color.alpha_20_black){
                colorId = R.color.colorAccent
                search_layout.backgroundColor = mContext.resources.getColor(colorId)
            }else if (scrollY<300&&colorId == R.color.colorAccent){
                colorId = R.color.alpha_20_black
                search_layout.backgroundColor = mContext.resources.getColor(colorId)
            }
        }
        main_cart.onClick {
            if(PublicStaticData.tooken == ""){
                startActivity<LoginActivity>("type" to 1)
            }else{
                startActivity<ShoopCartActivity>()
            }
        }
        search_layout.onClick {  }
        mine_center.onClick {
            if(PublicStaticData.tooken == ""){
                startActivity<LoginActivity>("type" to 1)
            }else{
                startActivity<VipCenterActivity>()
            }
        }
        search_click_layout.onClick {
            startActivity<SearchPageActivity>()
        }
    }

    override fun initHeadModel(): HeadVm = HeadVm("", false, -1);

    override fun initView(): View {
        bindingData = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.activity_main, null, false)
        return bindingData?.root!!
    }

    override fun onDestroy() {
        super.onDestroy()
        PayModel.destroy()
    }
}
