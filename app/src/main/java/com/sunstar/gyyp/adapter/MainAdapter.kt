package com.sunstar.gyyp.adapter

import android.content.Context
import android.databinding.ViewDataBinding
import com.sunstar.gyyp.base.BaseMuiltAdapter
import com.sunstar.gyyp.data.BannerData
import com.sunstar.gyyp.databinding.AdapterMainBannerLayoutBinding

class MainAdapter<T0:ViewDataBinding>(var context: Context) : BaseMuiltAdapter<T0, BaseMuiltAdapter.MuiltAdapterBaseData<T0>>(context)
