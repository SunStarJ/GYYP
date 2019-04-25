package com.sunstar.gyyp.ui

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.MainActivity
import com.sunstar.gyyp.R
import com.sunstar.gyyp.adapter.UserCenterAdapter
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.data.PublicStaticData
import com.sunstar.gyyp.databinding.ActivityUserCenterBinding
import com.sunstar.gyyp.databinding.AdapterSingletextViewBinding
import com.sunstar.gyyp.model.UserModel
import com.sunstar.gyyp.view.UserCenterView
import com.sunstar.gyyp.vm.UserVm
import kotlinx.android.synthetic.main.activity_user_center.*
import kotlinx.android.synthetic.main.textswitcher_text_layout.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class UserCenterActivity : BaseActivity(), UserCenterView {

    private var pop: AlertBuilder<DialogInterface>? = null
    var dataBinding: ActivityUserCenterBinding? = null
    var userM: UserVm<UserCenterView>? = null
    var controlList = mutableListOf<String>("会员资料修改", "修改登录密码", "修改支付密码", "收货地址管理", "退出登录")
    var logoutView: View? = null
    var dialog: Dialog? = null
    lateinit var adapter: UserCenterAdapter
    override fun appViewInitComplete() {
        userM!!.getUserData()
        adapter = UserCenterAdapter(mContext).initDataList(controlList).initBindView(object : SSBaseDataBindingAdapter.BindView<AdapterSingletextViewBinding> {
            override fun onBindViewHolder(b: AdapterSingletextViewBinding, position: Int) {
                b.name = controlList[position]
                b.root.onClick {
                    when (position) {
                        0 -> {
                            startActivity<ChangeUserInfoActivity>()
                        }
                        1 -> {
                            startActivity<ChangeLoginPasswordActivity>()
                        }
                        2 -> {
                            startActivity<ChangeBuyPasswordActivity>("userPhone" to userM?.user?.u_annex)
                        }
                        4 -> {
                            dialog ?: let {
                                logoutView = View.inflate(mContext, R.layout.pop_log_out, null)
                                logoutView?.find<View>(R.id.log_out)!!.onClick {
                                    this@UserCenterActivity.dialog?.dismiss()
                                    PublicStaticData.logout()
                                    var intent = Intent(mContext, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                logoutView?.find<View>(R.id.cancel)!!.onClick {
                                    dialog?.dismiss()
                                }
                                dialog = AlertDialog.Builder(mContext).setView(logoutView).show()
                                dialog!!.getWindow().setBackgroundDrawableResource(android.R.color.transparent)
                            }
                            dialog?.show()

//                            CustomDialog.show(mContext,R.layout.pop_log_out,object:CustomDialog.BindView{
//                                override fun onBind(dialog: CustomDialog?, rootView: View?) {
//                                    rootView?.run {
//                                        find<View>(R.id.log_out).onClick {
//                                            dialog?.doDismiss()
//                                            PublicStaticData.logout()
//                                            var intent = Intent(mContext,MainActivity::class.java)
//                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                                            startActivity(intent)
//                                            finish()
//                                        }
//                                        find<View>(R.id.cancel).onClick {
//                                            dialog?.doDismiss()
//                                        }
//                                    }
//                                }
//                            }).showDialog()
                        }
                    }
                }
            }
        }) as UserCenterAdapter
        data_view.adapter = adapter
        data_view.layoutManager = LinearLayoutManager(mContext)
        data_view.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
    }

    override fun initHeadModel(): HeadVm = HeadVm("个人中心", true, R.mipmap.back)

    override fun initView(): View {
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.activity_user_center, null, false)
        userM = UserVm(this)
        dataBinding!!.userData = userM
        return dataBinding!!.root
    }
}
