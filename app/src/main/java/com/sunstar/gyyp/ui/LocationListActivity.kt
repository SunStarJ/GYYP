package com.sunstar.gyyp.ui

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.JavaUtil
import com.sunstar.gyyp.R
import com.sunstar.gyyp.adapter.AddressAdapter
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.databinding.ActivityLocationListBinding
import com.sunstar.gyyp.databinding.AdapterLocationListBinding
import com.sunstar.gyyp.vm.LocationVm
import kotlinx.android.synthetic.main.activity_location_list.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class LocationListActivity : BaseActivity() {
    var vm: LocationVm? = null
    var selectType = 0
    override fun appViewInitComplete() {
        selectType = intent.getIntExtra("selectType",0)
        vm?.run {
            var adapter = AddressAdapter(mContext).initDataList(addressList).initBindView(object:SSBaseDataBindingAdapter.BindView<AdapterLocationListBinding>{
                override fun onBindViewHolder(b: AdapterLocationListBinding, position: Int) {
                    b.data = addressList[position]
                    b.change.onClick {
                        startActivity<AddLocationActivity>("data" to addressList[position],"type" to 1)
                    }
                    b.delete.onClick {
                        vm?.deleteLocation(position)
                    }
                    b.isDefaultClick.isSelected = addressList[position].isdefault == 1
                    b.isDefaultClick.onClick {
                        changeDefault(position)
                    }
                    b.body.onClick {
                        if(selectType == 1){
                            EventBus.getDefault().post(addressList[position])
                            finish()
                        }
                    }
                }
            })
            addressList.addOnListChangedCallback(JavaUtil.getListChangedCallback(adapter))
            data_view.layoutManager = LinearLayoutManager(mContext)
            data_view.addItemDecoration(DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL))
            data_view.adapter = adapter
        }
        vm?.getLocationLis()
        add_location.onClick {
            startActivity<AddLocationActivity>()
        }

    }

    override fun initHeadModel(): HeadVm = HeadVm("地址管理", true, R.mipmap.back)

    override fun initView(): View {
        var b = DataBindingUtil.inflate<ActivityLocationListBinding>(LayoutInflater.from(mContext), R.layout.activity_location_list, null, false)
        vm = LocationVm(this)
        b.locationVm = vm
        return b.root
    }
}
