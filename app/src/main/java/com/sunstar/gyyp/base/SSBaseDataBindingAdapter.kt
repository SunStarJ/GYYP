package com.sunstar.gyyp.base

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

abstract class SSBaseDataBindingAdapter<T1, T2 : ViewDataBinding>(var mContext: Context) : RecyclerView.Adapter<SSBaseDataBindingAdapter.SSBaseViewHolder>(), AnkoLogger {
    private var dataList: MutableList<T1>? = null
    private var layoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    private lateinit var bindView: BindView<T2>
    fun initDataList(dataList: MutableList<T1>): SSBaseDataBindingAdapter<T1, T2> {
        this.dataList = dataList
        return this
    }

    fun initBindView(bindview: BindView<T2>): SSBaseDataBindingAdapter<T1, T2> {
        this.bindView = bindview
        return this
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SSBaseViewHolder {
        var view = layoutInflater.inflate(initLayoutId(), p0, false)
        return SSBaseViewHolder(view)
    }

    abstract fun initLayoutId(): Int

    override fun getItemCount(): Int = dataList!!.size

    override fun onBindViewHolder(p0: SSBaseViewHolder, p1: Int) {
        info { dataList!!.size }
        bindView.onBindViewHolder(p0.getBinding() as T2, p1)
    }

    interface BindView<T2> {
        fun onBindViewHolder(b: T2, position: Int)
    }


    class SSBaseViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var binding: ViewDataBinding = DataBindingUtil.bind<ViewDataBinding>(v)!!
        fun getBinding(): ViewDataBinding = binding
    }
}