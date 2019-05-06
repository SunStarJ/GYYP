package com.sunstar.gyyp.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class VpBodyAdapter(var listTitle:MutableList<String>,var bodyData:MutableList<Fragment>,fm:FragmentManager) :FragmentPagerAdapter(fm){
    override fun getItem(p0: Int): Fragment = bodyData[p0]

    override fun getCount(): Int = bodyData.size

    override fun getPageTitle(position: Int): CharSequence? {
        return listTitle[position]
    }
}