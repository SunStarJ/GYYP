package com.sunstar.gyyp.ui

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import com.sunstar.activityplugin.vm.HeadVm
import com.sunstar.gyyp.R
import com.sunstar.gyyp.base.BaseActivity
import com.sunstar.gyyp.base.BaseView
import com.sunstar.gyyp.databinding.ActivityChangeUserInfoBinding
import com.sunstar.gyyp.view.ChangeUserInfoView
import com.sunstar.gyyp.vm.UserVm
import com.yuyh.library.imgsel.ISNav
import kotlinx.android.synthetic.main.adapter_main_list_layout.view.*
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import com.yuyh.library.imgsel.config.ISCameraConfig
import com.yuyh.library.imgsel.config.ISListConfig
import org.jetbrains.anko.startActivity


class ChangeUserInfoActivity : BaseActivity(), ChangeUserInfoView {
    var bottomSheet: BottomSheetDialog? = null
    var bottomSheetView: View? = null
    private val PHOTO_CODE = 0X0000012
    private val PHOTO_CAMERA_CODE = 0X0000011
    var binding: ActivityChangeUserInfoBinding? = null
    var userM: UserVm<ChangeUserInfoView>? = null
    override fun showPicSelectPop() {
        bottomSheet = bottomSheet ?: let {
            var dialog = BottomSheetDialog(mContext)
            bottomSheetView = View.inflate(mContext, R.layout.pop_img_select_layout, null)
            initPopView()
            dialog.setContentView(bottomSheetView)
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent)
            dialog
        }
        bottomSheet?.show()
    }

    private fun initPopView() {
        bottomSheetView?.run {
            find<View>(R.id.check).onClick {
                if (userM?.headerImg == "") {
                    toast("当前用户无头像，请选择上传")
                } else {
                    startActivity<PhotoCheckActivity>("photoPath" to userM?.headerImg)
                }
                bottomSheet?.hide()
            }
            find<View>(R.id.photo).onClick {
                val config = ISCameraConfig.Builder()
                        .build()
                ISNav.getInstance().toCameraActivity(mContext, config, PHOTO_CAMERA_CODE)
                bottomSheet?.hide()
            }
            find<View>(R.id.box).onClick {
                var config = ISListConfig.Builder()
                        // 是否多选, 默认true
                        .multiSelect(false)
                        .titleBgColor(Color.parseColor("#FFFFFF"))
                        .titleColor(Color.parseColor("#333333")).build()
                ISNav.getInstance().toListActivity(mContext, config, PHOTO_CODE)
                bottomSheet?.hide()
            }
            find<View>(R.id.dismiss).onClick {
                bottomSheet?.hide()
            }
            this.setBackgroundColor(mContext.resources.getColor(R.color.color_transparent))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.run {
            if(requestCode == PHOTO_CODE && resultCode == RESULT_OK){
                    userM?.changePhoto(data.getStringArrayListExtra("result")[0])
            }
            if(requestCode == PHOTO_CAMERA_CODE && resultCode == RESULT_OK){
                    userM?.changePhoto(data.getStringExtra("result"))
            }
        }

    }

    override fun appViewInitComplete() {
        userM?.run {
            getUserData()
        }
    }

    override fun back() {
        finish()
    }

    override fun initHeadModel(): HeadVm = HeadVm("会员资料修改", true, R.mipmap.back)

    override fun initView(): View {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.activity_change_user_info, null, false)
        userM = UserVm(this)
        binding!!.data = userM
        return binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        userM?.onDestory()
    }

}
