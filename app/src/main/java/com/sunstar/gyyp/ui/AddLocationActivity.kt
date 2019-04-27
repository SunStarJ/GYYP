package com.sunstar.gyyp.ui

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.lljjcoder.style.citypickerview.CityPickerView
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.databinding.ActivityAddLocationBinding
import com.sunstar.gyyp.view.EditLocationView
import com.sunstar.gyyp.vm.LocationVm
import org.jetbrains.anko.toast
import com.lljjcoder.style.citylist.Toast.ToastUtils
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import kotlinx.android.synthetic.main.activity_add_location.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import com.lljjcoder.citywheel.CityConfig
import com.sunstar.gyyp.data.AddressListItem

class AddLocationActivity : BaseActivity(), EditLocationView {
    var cityPicker = CityPickerView()
    override fun saveComplete() {
        toast("保存成功")
        finish()
    }

    var vm: LocationVm? = null
    override fun appViewInitComplete() {
        if (intent.getIntExtra("type", 0) == 1) {
            changeText("修改地址")
            (intent.getSerializableExtra("data") as AddressListItem)?.let {
                vm?.initData(it)
            }
        }
        vm?.initCityShow()
        //监听选择点击事件及返回结果
        val cityConfig = CityConfig.Builder().title("").build()
        cityPicker.init(mContext)
        cityPicker.setConfig(cityConfig)
        cityPicker.setOnCityItemClickListener(object : OnCityItemClickListener() {
            override fun onSelected(province: ProvinceBean, city: CityBean, district: DistrictBean) {
                vm?.run {
                    this.province = province.name
                    this.city = city.name
                    this.area = district.name
                    initCityShow()
                }
            }

            override fun onCancel() {
            }
        })
        city_click.onClick{
            cityPicker.showCityPicker()
        }
    }

    override fun initHeadModel(): HeadVm = HeadVm("添加地址", true, R.mipmap.back)

    override fun initView(): View {
        var b = DataBindingUtil.inflate<ActivityAddLocationBinding>(LayoutInflater.from(mContext), R.layout.activity_add_location, null, false)
        vm = LocationVm(this)
        b.data = vm
        return b.root
    }
}
