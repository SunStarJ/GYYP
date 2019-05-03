package com.sunstar.gyyp.util

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View

class MDGridRvDividerDecoration(context: Context) : RecyclerView.ItemDecoration() {

    /**
     * 用于绘制间隔样式
     */
    private val mDivider: Drawable?

    init {
        // 获取默认主题的属性
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        // 绘制间隔，每一个item，绘制右边和下方间隔样式
        val childCount = parent.childCount
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        val orientation = (parent.layoutManager as GridLayoutManager).orientation
        var isDrawHorizontalDivider = true
        var isDrawVerticalDivider = true
        var extra = childCount % spanCount
        extra = if (extra == 0) spanCount else extra
        for (i in 0 until childCount) {
            isDrawVerticalDivider = true
            isDrawHorizontalDivider = true
            // 如果是竖直方向，最右边一列不绘制竖直方向的间隔
            if (orientation == OrientationHelper.VERTICAL && (i + 1) % spanCount == 0) {
                isDrawVerticalDivider = false
            }

            // 如果是竖直方向，最后一行不绘制水平方向间隔
            if (orientation == OrientationHelper.VERTICAL && i >= childCount - extra) {
                isDrawHorizontalDivider = false
            }

            // 如果是水平方向，最下面一行不绘制水平方向的间隔
            if (orientation == OrientationHelper.HORIZONTAL && (i + 1) % spanCount == 0) {
                isDrawHorizontalDivider = false
            }

            // 如果是水平方向，最后一列不绘制竖直方向间隔
            if (orientation == OrientationHelper.HORIZONTAL && i >= childCount - extra) {
                isDrawVerticalDivider = false
            }

            if (isDrawHorizontalDivider) {
                drawHorizontalDivider(c, parent, i)
            }

            if (isDrawVerticalDivider) {
                drawVerticalDivider(c, parent, i)
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        val orientation = (parent.layoutManager as GridLayoutManager).orientation
        val position = parent.getChildLayoutPosition(view)
        if (orientation == OrientationHelper.VERTICAL && (position + 1) % spanCount == 0) {
            outRect.set(0, 0, 0, mDivider!!.intrinsicHeight)
            return
        }

        if (orientation == OrientationHelper.HORIZONTAL && (position + 1) % spanCount == 0) {
            outRect.set(0, 0, mDivider!!.intrinsicWidth, 0)
            return
        }

        outRect.set(0, 0, mDivider!!.intrinsicWidth, mDivider.intrinsicHeight)
    }

    /**
     * 绘制竖直间隔线
     *
     * @param canvas
     * @param parent
     * 父布局，RecyclerView
     * @param position
     * irem在父布局中所在的位置
     */
    private fun drawVerticalDivider(canvas: Canvas, parent: RecyclerView, position: Int) {
        val child = parent.getChildAt(position)
        val params = child
                .layoutParams as RecyclerView.LayoutParams
        val top = child.top - params.topMargin
        val bottom = child.bottom + params.bottomMargin + mDivider!!.intrinsicHeight
        val left = child.right + params.rightMargin
        val right = left + mDivider.intrinsicWidth
        mDivider.setBounds(left, top, right, bottom)
        mDivider.draw(canvas)
    }

    /**
     * 绘制水平间隔线
     *
     * @param canvas
     * @param parent
     * 父布局，RecyclerView
     * @param position
     * item在父布局中所在的位置
     */
    private fun drawHorizontalDivider(canvas: Canvas, parent: RecyclerView, position: Int) {
        val child = parent.getChildAt(position)
        val params = child
                .layoutParams as RecyclerView.LayoutParams
        val top = child.bottom + params.bottomMargin
        val bottom = top + mDivider!!.intrinsicHeight
        val left = child.left - params.leftMargin
        val right = child.right + params.rightMargin + mDivider.intrinsicWidth
        mDivider.setBounds(left, top, right, bottom)
        mDivider.draw(canvas)
    }

    companion object {

        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}
