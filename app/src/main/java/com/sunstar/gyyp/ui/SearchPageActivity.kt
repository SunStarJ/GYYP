package com.sunstar.gyyp.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.Url
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.BaseCallBack
import com.sunstar.gyyp.data.RootBean
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_search_page.*
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class SearchPageActivity : BaseActivity() {
    override fun appViewInitComplete() {
        hiddenTitleBar()
        initData()
        back.onClick {
            super.onBackPressed()
        }
        search.onClick {
            startActivity<GoodsListPageActivity>("keyWord" to key_word.text.toString())
        }
    }

    private fun initData() {
        showLoading("获取数据中，请稍后")
        OkGo.post<RootBean>(Url.gethotwords).execute(object:BaseCallBack(){
            override fun dataError(data: RootBean) {
                hiddenLoading()
                toast(data.msg)
            }

            override fun success(it: Response<RootBean>) {
                var datas = it.body().hotwords
                hiddenLoading()
                flow_layout.adapter = object:TagAdapter<String>(datas){
                    override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                        var v = View.inflate(mContext,R.layout.adapter_flow_layout_item,null)
                        v.find<TextView>(R.id.flow_text).text = t
                        return v
                    }
                }
                flow_layout.setOnTagClickListener { view, position, parent ->
                    startActivity<GoodsListPageActivity>("keyWord" to datas!![position])
                    true
                }
            }

            override fun dataNull() {
            }
        })
    }

    override fun initHeadModel(): HeadVm = HeadVm("",false,-1)

    override fun initView(): View = View.inflate(mContext,R.layout.activity_search_page,null)
}
