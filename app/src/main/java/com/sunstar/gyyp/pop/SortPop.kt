package com.sunstar.gyyp.pop

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import com.sunstar.gyyp.R
import com.sunstar.gyyp.ui.GoodsListPageActivity
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.textColor
import razerdp.basepopup.BasePopupWindow

class SortPop: BasePopupWindow {
    var type: Int = 0
    var selectListener:SelectListener? = null

    constructor(context: Context?, type: Int) : super(context) {
        this.type = type
    }
    override fun onCreateContentView(): View {
        type = (context as GoodsListPageActivity).sort
        var v = createPopupById(R.layout.pop_goods_list_page_sort)
        var colorWhite = context.resources.getColor(R.color.colorWhite)
        var colorOrg = context.resources.getColor(R.color.color_org)
        var textList = mutableListOf(v.find<TextView>(R.id.no_sort),
                v.find<TextView>(R.id.sale_num),
                v.find<TextView>(R.id.sale_price),
                v.find<TextView>(R.id.time)
                )
        var sortUp = context.resources.getDrawable(R.mipmap.sort_up)
        var sortNo = context.resources.getDrawable(R.mipmap.sort_down_un_select)
        var sortDown = context.resources.getDrawable(R.mipmap.sort_down)
        for(data in textList){
            data.textColor = colorWhite
            initDrawable(data,sortNo)
        }
        textList[0].setCompoundDrawables(null,null,null,null)
        when(type){
            0->{
                textList[0].textColor = colorOrg
            }
            1->{
                textList[1].textColor = colorOrg
                initDrawable(textList[1],sortUp)
            }
            2->{
                textList[1].textColor = colorOrg
                initDrawable(textList[1],sortDown)
            }
            3->{
                textList[2].textColor = colorOrg
                initDrawable(textList[2],sortUp)
            }
            4->{
                textList[2].textColor = colorOrg
                initDrawable(textList[2],sortDown)
            }
            5->{
                textList[3].textColor = colorOrg
                initDrawable(textList[3],sortUp)
            }
            6->{
                textList[3].textColor = colorOrg
                initDrawable(textList[3],sortDown)
            }
        }
        v.find<TextView>(R.id.no_sort).onClick { dismiss()
            selectListener?.select(0)
        }
        v.find<TextView>(R.id.sale_num).onClick { dismiss()
            if(type == 1){
                selectListener?.select(2)
            }else{
                selectListener?.select(1)
            }
        }
        v.find<TextView>(R.id.sale_price).onClick { dismiss()
            if(type == 3){
                selectListener?.select(4)
            }else{
                selectListener?.select(3)
            }
        }
        v.find<TextView>(R.id.time).onClick { dismiss()
            if(type == 5){
                selectListener?.select(6)
            }else{
                selectListener?.select(5)
            }
        }
        return v
    }

    private fun initDrawable(data: TextView, sortNo: Drawable?) {
        sortNo?.setBounds(0,0,sortNo.minimumWidth,sortNo.minimumHeight)
        data.setCompoundDrawables(null,null,sortNo,null)
    }

    fun initListener(selectListener:SelectListener):SortPop{
        this.selectListener = selectListener
        return this
    }

    interface SelectListener{
        fun select(type : Int)
    }
}