package com.sunstar.gyyp.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.sunstar.gyyp.data.CatagoryBean

class AppFragmentPagerAdapter(var titleList: MutableList<CatagoryBean>,var dataList: MutableList<Fragment>, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getPageTitle(position: Int): CharSequence? = titleList[position].name
    override fun getItem(p0: Int): Fragment = dataList[p0]

    override fun getCount(): Int = dataList.size
}