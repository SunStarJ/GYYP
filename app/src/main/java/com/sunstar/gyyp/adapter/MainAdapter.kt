package com.sunstar.gyyp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.databinding.ViewDataBinding
import android.view.View
import android.widget.ViewSwitcher
import com.sunstar.gyyp.R
import com.sunstar.gyyp.WebActivity
import com.sunstar.gyyp.base.BaseMuiltAdapter
import com.sunstar.gyyp.base.GlideImageLoader
import com.youth.banner.Banner
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import com.sunstar.gyyp.MainActivity
import android.support.v4.widget.TextViewCompat.setTextAppearance
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.widget.TextView
import android.widget.TextSwitcher
import android.view.animation.AnimationUtils
import android.view.animation.Animation
import com.sunstar.gyyp.base.SSBaseDataBindingAdapter
import com.sunstar.gyyp.databinding.*
import com.sunstar.gyyp.ui.ArticalListActivity
import com.sunstar.gyyp.vm.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.lang.Exception
import java.util.concurrent.TimeUnit


class MainAdapter<T0 : ViewDataBinding>(var context: Context) : BaseMuiltAdapter<T0, BaseMuiltAdapter.MuiltAdapterBaseData<T0>>(context), AnkoLogger {
    @SuppressLint("CheckResult")
    override fun initBandingComplete(holder: BaseHolder,t2: T0, t1: MuiltAdapterBaseData<T0>) {
        super.initBandingComplete(holder,t2, t1)
        t2.run {
            if (this is AdapterMainBannerLayoutBinding) {
                var banner = root.find<Banner>(R.id.banner)
                banner.setImageLoader(GlideImageLoader())
                banner.setImages((t1 as BannerVM).banners)
                banner.setOnBannerListener {
                    var url = t1.banners[it].url
                    info { it.toString() }
                    mContext.startActivity<WebActivity>("url" to url)
                }
                banner.start()
            }
            createTextSwitcher(this, t1, t2)
            if(this is AdapterMainControlBinding){
                var controlView = root.find<RecyclerView>(R.id.main_control_rec)
                controlView.adapter = MainControlAdapter(mContext).initDataList((t1 as MainControlVm).controlList)
                        .initBindView(object:SSBaseDataBindingAdapter.BindView<AdapterMainControlInnerAdapterBinding>{
                    override fun onBindViewHolder(b: AdapterMainControlInnerAdapterBinding, position: Int) {
                        b.data = t1.controlList[position]
                        b.root.onClick {
                            mContext.toast(position.toString())
                        }
                    }
                })
                controlView.layoutManager = GridLayoutManager(mContext,4)
            }
            if(this is AdapterMainPreferenceLayoutBinding){
                var view = root.find<RecyclerView>(R.id.main_preference_recycle_view)
                view.adapter = MainPreferenceAdapter(mContext).initDataList((t1 as MainPreferenceVM).preferenceDatas).initBindView(object :SSBaseDataBindingAdapter.BindView<AdapterPerferenceInnerAdapterBinding>{
                    override fun onBindViewHolder(b: AdapterPerferenceInnerAdapterBinding, position: Int) {
                        b.data = t1.preferenceDatas[position]
                        b.root.onClick { mContext.toast(position.toString()) }
                    }
                })
                view.layoutManager = GridLayoutManager(mContext,t1.preferenceDatas.size)
            }
            if(this is AdapterMainHotMarketBinding){
                var view = root.find<RecyclerView>(R.id.market_view)
                view.adapter = MainHotmarketItemAdapter(mContext).initDataList((t1 as MainHotmarketVM).hotMarketList).initBindView(object :SSBaseDataBindingAdapter.BindView<AdapterMainHotmarktInnerBinding>{
                    override fun onBindViewHolder(b: AdapterMainHotmarktInnerBinding, position: Int) {
                        b.data = t1.hotMarketList[position]
                        b.root.onClick { mContext.toast(position.toString()) }
                    }
                })
                view.layoutManager = GridLayoutManager(mContext,2)
            }
        }
    }


    private var result: Disposable? = null

    private fun createTextSwitcher(it: T0, t1: MuiltAdapterBaseData<T0>, t2: T0) {
        if (it is AdapterMainTextswitcherLayoutBinding) {
            var curIndex = 0
            var list = (t1 as MainTextSwitcherVM).adList
            it.root.find<View>(R.id.ad_check_all).onClick {
                mContext.startActivity<ArticalListActivity>()
            }

            var switcher = it.root.find<TextSwitcher>(R.id.text_switcher)
            val `in` = AnimationUtils.loadAnimation(mContext,
                    R.anim.in_animation)
            val out = AnimationUtils.loadAnimation(mContext,
                    R.anim.out_animation)
            switcher.inAnimation = `in`
            switcher.outAnimation = out
            try {
                switcher.setFactory {
                    View.inflate(mContext,R.layout.textswitcher_text_layout,null)
                }
            }catch (e:Exception){}

            if (list.size != 0 &&result == null) {
                switcher.setText(list[curIndex].title)
               result = Observable.interval(4, 4, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
                    if (curIndex == list.size - 1) {
                        curIndex = 0
                    } else {
                        curIndex += 1
                    }
                    switcher.setText(list[curIndex].title)
                }
            }
            switcher.onClick {
                var url = (t1 as MainTextSwitcherVM<*>).adList[curIndex].url
                mContext.startActivity<WebActivity>("url" to url)
            }
        }
    }
}
