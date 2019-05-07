package com.sunstar.gyyp.ui

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.databinding.ActivityFindPasswordBinding
import com.sunstar.gyyp.model.UserModel
import com.sunstar.gyyp.view.FindPasswordView
import com.sunstar.gyyp.vm.UserVm
import org.jetbrains.anko.toast

class FindPasswordActivity : BaseActivity(),FindPasswordView {
    override fun findPasswordComplete() {
        toast("修改成功")
        finish()
    }

    var binding : ActivityFindPasswordBinding?=null
    var vm : UserVm<FindPasswordView>?=null
    override fun appViewInitComplete() {
        vm?.run { createRegistCodeBitmap() }
    }

    override fun initHeadModel(): HeadVm = HeadVm("忘记密码",true,R.mipmap.back)
    override fun initView(): View {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.activity_find_password,null,false)
        vm = UserVm(this)
        binding!!.data = vm
        return binding!!.root
    }
    override fun back() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        vm?.onDestory()
    }
}
