package com.sunstar.gyyp.ui

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.databinding.ActivityLocationListBinding
import com.sunstar.gyyp.vm.LocationVm
import kotlinx.android.synthetic.main.activity_location_list.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class LocationListActivity : BaseActivity() {
    var vm: LocationVm? = null
    override fun appViewInitComplete() {
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
