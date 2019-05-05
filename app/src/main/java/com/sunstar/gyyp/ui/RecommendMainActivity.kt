package com.sunstar.gyyp.ui

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.databinding.ActivityRecommendMainBinding
import com.sunstar.gyyp.vm.RecommendVm

class RecommendMainActivity : BaseActivity() {
    var recommendVm : RecommendVm?=null
    override fun appViewInitComplete() {
        recommendVm?.run {
            initUserData(intent.getSerializableExtra("userData") as RootBean)
            initBaseView(this@RecommendMainActivity)
            getRecommendData()
        }
    }

    override fun initHeadModel(): HeadVm = HeadVm("我的推荐", true, R.mipmap.back)

    override fun initView(): View {
        var binding = DataBindingUtil.inflate<ActivityRecommendMainBinding>(LayoutInflater.from(mContext), R.layout.activity_recommend_main, null, false)
        recommendVm = RecommendVm()
        binding.data = recommendVm
        return binding.root
    }
}

