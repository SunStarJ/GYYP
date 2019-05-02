package com.sunstar.gyyp.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.sunstar.gyyp.R

abstract class LazyFragment : Fragment() {

    /**
     * Fragment是否可见状态
     */
    var isFragmentVisible: Boolean = false
    /**
     * 标志位，View是否已经初始化完成。
     */
    private var isPrepared: Boolean = false
    /**
     * 是否第一次加载
     */
    private var isFirstLoad = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val inflate = inflater.inflate(initLayoutId(), container, false)

        // 若 viewpager 不设置 setOffscreenPageLimit 或设置数量不够
        // 销毁的Fragment onCreateView 每次都会执行(但实体类没有从内存销毁)
        isFirstLoad = true


        return inflate
    }

    protected abstract fun initLayoutId(): Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //界面初始化完成
        isPrepared = true
        loadPageView()
        loadData()
    }

    abstract fun loadPageView()

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     *
     * @param isVisibleToUser 是否显示出来了
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            onVisible()
        } else {
            onInvisible()
        }
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     *
     * @param hidden hidden True if the fragment is now hidden, false if it is not
     * visible.
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            onVisible()
        } else {
            onInvisible()
        }
    }

    protected fun onInvisible() {
        isFragmentVisible = false

    }

    /**
     * 当界面可见的时候执行
     */
    protected fun onVisible() {
        isFragmentVisible = true
        loadData()

    }

    /**
     * 这里执行懒加载的逻辑
     */
    abstract fun lazyLoad()
    private fun loadData() {
        //判断View是否已经初始化完成 并且 fragment是否可见 并且是第一次加载
        if (isPrepared && isFragmentVisible && isFirstLoad) {
            isFirstLoad = false
            lazyLoad()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isPrepared = false
    }

}