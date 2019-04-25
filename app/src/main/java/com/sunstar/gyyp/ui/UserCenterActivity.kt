package com.sunstar.gyyp.ui

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.adapter.UserCenterAdapter
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.databinding.ActivityUserCenterBinding
import com.sunstar.gyyp.databinding.AdapterSingletextViewBinding
import com.sunstar.gyyp.model.UserModel
import com.sunstar.gyyp.view.UserCenterView
import com.sunstar.gyyp.vm.UserVm
import kotlinx.android.synthetic.main.activity_user_center.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class UserCenterActivity : BaseActivity(), UserCenterView {
    var dataBinding: ActivityUserCenterBinding? = null
    var userM: UserVm<UserCenterView>? = null
    var controlList = mutableListOf<String>("会员资料修改", "修改登录密码", "修改支付密码", "收货地址管理", "退出登录")
    lateinit var adapter: UserCenterAdapter
    override fun appViewInitComplete() {
        userM!!.getUserData()
        adapter = UserCenterAdapter(mContext).initDataList(controlList).initBindView(object : SSBaseDataBindingAdapter.BindView<AdapterSingletextViewBinding> {
            override fun onBindViewHolder(b: AdapterSingletextViewBinding, position: Int) {
                b.name = controlList[position]
                b.root.onClick {
                    when(position){
                        0->{
                          startActivity<ChangeUserInfoActivity>()
                        }
                    }
                }
            }
        }) as UserCenterAdapter
        data_view.adapter = adapter
        data_view.layoutManager = LinearLayoutManager(mContext)
        data_view.addItemDecoration(DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL))
    }

    override fun initHeadModel(): HeadVm = HeadVm("个人中心", true, R.mipmap.back)

    override fun initView(): View {
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.activity_user_center, null, false)
        userM = UserVm(this)
        dataBinding!!.userData = userM
        return dataBinding!!.root
    }
}
