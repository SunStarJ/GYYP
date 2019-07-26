package com.sunstar.gyyp.base

import android.content.Intent
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request
import com.sunstar.gyyp.ProjectApplication
import com.sunstar.gyyp.data.PublicStaticData
import com.sunstar.gyyp.data.RootBean
import com.sunstar.gyyp.ui.LoginActivity
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

abstract class BaseCallBack : JsonCallBack<RootBean>(RootBean::class.java) {
    override fun onStart(request: Request<RootBean, out Request<Any, Request<*, *>>>?) {
        super.onStart(request)
        var params = request?.params
        if (PublicStaticData.tooken != "") request!!.params("token", PublicStaticData.tooken)

    }

    override fun onSuccess(response: Response<RootBean>?) {
        response?.let {
            if(it.body() == null){
                dataNull()
            }else if(it.body().code == -1){
                dataError(it.body())
                relogin()
            }else if(it.body().code == 0){
                success(it)
            }else{
                dataError(it.body())
            }
        }
    }

    abstract fun dataError(data:RootBean)

    abstract fun success(it: Response<RootBean>)

    private fun relogin() {
        var intent = Intent(ProjectApplication.instance.applicationContext,LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("type",1)
        ProjectApplication.instance.applicationContext.startActivity(intent)
    }

    abstract fun dataNull()
}