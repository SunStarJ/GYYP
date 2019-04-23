package com.sunstar.gyyp.base

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.nio.file.attribute.PosixFileAttributeView

open class BaseMuiltAdapter<T2:ViewDataBinding,T1 : BaseMuiltAdapter.MuiltAdapterBaseData<T2>>(var mContext: Context) : RecyclerView.Adapter<BaseMuiltAdapter.BaseHolder>() {

    var dataList: ObservableArrayList<T1>? = null
    private var layoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    fun initList(dataList: ObservableArrayList<T1>): BaseMuiltAdapter<T2,T1> {
        this.dataList = dataList
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        var view = dataList?.let {
            layoutInflater.inflate(viewType, parent, false)
        }
        return BaseHolder(view!!)
    }

    override fun getItemViewType(position: Int): Int {
        var viewtype = 0
        dataList?.let {
            viewtype = it[position].getViewId()
        }
        return viewtype
    }

    override fun getItemCount(): Int = dataList?.size!!

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        dataList?.let {
            it[position].bindView.onBindViewHolder(holder.getBinding() as T2,position)
        }
    }


    class BaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var binding: ViewDataBinding = DataBindingUtil.bind<ViewDataBinding>(view)!!
        fun getBinding(): ViewDataBinding = binding
    }

    abstract class MuiltAdapterBaseData<T2 : ViewDataBinding>: MuiltAdapterInterFace{
        var bindView: BindView<T2>
        init {
            bindView = initBindingView()
        }
        abstract fun initBindingView():BindView<T2>
    }

    interface BindView<T2> {
        fun onBindViewHolder(b: T2, position: Int)
    }

    interface MuiltAdapterInterFace {
        fun getViewId(): Int
    }
}
