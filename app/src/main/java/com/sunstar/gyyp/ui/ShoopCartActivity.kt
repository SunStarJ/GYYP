package com.sunstar.gyyp.ui

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.JavaUtil
import com.sunstar.gyyp.R
import com.sunstar.gyyp.adapter.ShopingCartAdapter
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.PreferenceItem
import com.sunstar.gyyp.databinding.ActivityShoopCartBinding
import com.sunstar.gyyp.databinding.AdapterShopCartBinding
import com.sunstar.gyyp.view.ShopCartView
import com.sunstar.gyyp.vm.ShopCartVm
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_artical_list.*
import kotlinx.android.synthetic.main.activity_artical_list.data_view
import kotlinx.android.synthetic.main.activity_shoop_cart.*
import org.jetbrains.anko.info
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit

class ShoopCartActivity : BaseActivity(), ShopCartView {
    override fun showAllSelect(select: Boolean) {
        select_all.isSelected = select
    }
    override fun goToOrederPayPage() {

    }

    var adapter: ShopingCartAdapter? = null
    override fun appViewInitComplete() {
        vm?.run {
            adapter = ShopingCartAdapter(mContext).initDataList(dataList).initBindView(object : SSBaseDataBindingAdapter.BindView<AdapterShopCartBinding> {
                override fun onBindViewHolder(b: AdapterShopCartBinding, position: Int) {
                    b.data = dataList[position]
                    b.select.isSelected = dataList[position].isSelect
                    b.bodyLayout.onClick {
                        var data = dataList[position]
                        data.isSelect = !data.isSelect
                        vm!!.dataList[position] = data
                        vm?.listrStatusChange()
                    }
                    b.numAdd.onClick {
                        vm?.numberChange(position,1)
                    }
                    b.numSub.onClick {
                        vm?.numberChange(position,2)
                    }
                }
            }) as ShopingCartAdapter
            dataList.addOnListChangedCallback(JavaUtil.getListChangedCallback(adapter))
            data_view.adapter = adapter
            data_view.layoutManager = LinearLayoutManager(mContext)
            data_view.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
            initConfig()

        }
        select_all.onClick {
            select_all.isSelected = !select_all.isSelected
            for(i in 0 until  vm!!.dataList.size){
                var data = vm!!.dataList[i]
                data.isSelect = select_all.isSelected
                vm!!.dataList[i] = data
            }
        }
        vm?.getData()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        vm?.run {
            if (!isDelete.get()) {
                menu!!.findItem(R.id.cart_change).title = "编辑"
            } else {
                menu!!.findItem(R.id.cart_change).title = "完成"
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        vm?.run {
            isDelete.set(!isDelete.get())
        }
        onPrepareOptionsMenu(menu)
        return true
    }

    var menu: Menu? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shopping_cart_edit_menu, menu)
        this.menu = menu
        return true
    }

    var vm: ShopCartVm? = null
    override fun initHeadModel(): HeadVm = HeadVm("购物车", true, R.mipmap.back)

    override fun initView(): View {
        var binding = DataBindingUtil.inflate<ActivityShoopCartBinding>(LayoutInflater.from(mContext), R.layout.activity_shoop_cart, null, false)
        vm = ShopCartVm(this)
        binding.data = vm
        return binding.root
    }

}
