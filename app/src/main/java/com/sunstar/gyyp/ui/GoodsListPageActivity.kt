package com.sunstar.gyyp.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.View
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.MainActivity
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.adapter.AppFragmentPagerAdapter
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.data.CatagoryBean
import com.sunstar.gyyp.data.PublicStaticData
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.pop.SortPop
import com.sunstar.gyyp.ui.fragment.GoodsListFragment
import kotlinx.android.synthetic.main.activity_goods_list_page.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import razerdp.basepopup.QuickPopupBuilder
import razerdp.basepopup.QuickPopupConfig

class GoodsListPageActivity : BaseActivity() {
    var hot = 99
    var sec = 99
    var sort = 0
    var keyWord = ""
    var pageId = ""
    var categoryList = mutableListOf<CatagoryBean>()
    var fragmentList = mutableListOf<Fragment>()
    override fun appViewInitComplete() {
        hot = intent.getIntExtra("hot", 99)
        sec = intent.getIntExtra("sec", 99)
        intent.getStringExtra("keyWord")?.let {
            keyWord = it
        }
        search_text.text = keyWord
        hiddenTitleBar()
        initListener()
        tab_bar.setSelectedTabIndicatorColor(mContext.resources.getColor(R.color.color_red))
        initData()
    }


    private fun initData() {
        showLoading("提交数据中，请稍后")
        OkGo.post<RootBean>(Url.getproductcategorys)
                .execute(object : BaseCallBack() {
                    override fun dataError(data: RootBean) {
                        hiddenLoading()
                        toast(data.msg)
                    }

                    override fun success(it: Response<RootBean>) {
                        hiddenLoading()
                        categoryList.add(CatagoryBean("0", "全部"))
                        categoryList.addAll(it.body().catagories!!)
                        for (data in categoryList) {
                            tab_bar.addTab(tab_bar.newTab().setText(data.name))
                        }
                        addFragment()
                    }

                    override fun dataNull() {
                    }
                })
    }

    private fun addFragment() {
        for (data in categoryList) {
            fragmentList.add(GoodsListFragment().initId(data.id))
        }
        var adapter = AppFragmentPagerAdapter(categoryList, fragmentList, supportFragmentManager)
        vp_body.adapter = adapter
        vp_body.offscreenPageLimit = fragmentList.size
        tab_bar.setupWithViewPager(vp_body)
        initPageIndex()
    }

    private fun initPageIndex() {
        intent.getStringExtra("pageId")?.let {
            pageId = it
        }
        if (pageId == "") return
        var pageIndex = 0
        for (i in 0 until categoryList.size) {
            var id = categoryList[i].id
            if(pageId == id){
                pageIndex = i
            }
        }
        vp_body.currentItem = pageIndex
    }

    private fun initListener() {
        home_back.onClick {
            var intent = Intent(mContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        user_center.onClick {
            if (PublicStaticData.tooken == "") {
                startActivity<LoginActivity>()
            } else {
                startActivity<VipCenterActivity>()
            }
        }
        search_text.onClick {
            var intent = Intent(mContext, SearchPageActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        sort_click.onClick {
            SortPop(mContext,sort).initListener(object:SortPop.SelectListener{
                override fun select(type: Int) {
                    sort = type
                    for (fragment in fragmentList){
                        (fragment as GoodsListFragment).outLoadData()
                    }
                }
            }).showPopupWindow(sort_click)
        }
    }

    override fun initHeadModel(): HeadVm = HeadVm("", false, -1)
    override fun initView(): View = View.inflate(mContext, R.layout.activity_goods_list_page, null)
}
