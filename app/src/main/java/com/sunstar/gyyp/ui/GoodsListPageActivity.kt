package com.sunstar.gyyp.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
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
import com.sunstar.gyyp.ui.fragment.GoodsListFragment
import kotlinx.android.synthetic.main.activity_goods_list_page.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class GoodsListPageActivity : BaseActivity() {
    var hot = 99
    var sec = 99
    var sort = 0
    var keyWord = ""
    override fun appViewInitComplete() {
        hiddenTitleBar()
        initListener()
        tab_bar.setSelectedTabIndicatorColor(mContext.resources.getColor(R.color.color_red))
        initData()
    }

    var categoryList = mutableListOf<CatagoryBean>()
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
        var dataList = mutableListOf<Fragment>()
        for (data in categoryList) {
            dataList.add(GoodsListFragment().initId(data.id))
        }
        var adapter = AppFragmentPagerAdapter(categoryList, dataList, supportFragmentManager)
        vp_body.adapter = adapter
        tab_bar.setupWithViewPager(vp_body)
    }

    private fun initListener() {
        home_back.onClick {
            var intent = Intent(mContext, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
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
            finish()
        }
        sort_click.onClick {
            toast("排序")
        }
    }

    override fun initHeadModel(): HeadVm = HeadVm("", false, -1)
    override fun initView(): View = View.inflate(mContext, R.layout.activity_goods_list_page, null)
}
