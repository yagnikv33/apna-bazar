package com.skeletonkotlin.e_cigarette.helper.util.rvutil

import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RvHeaderDecoration(
    private val headerOffset: Int,
    private val sticky: Boolean,
    private var headerViewId: Int,
    private val sectionCallback: SectionCallback
) : RecyclerView.ItemDecoration() {

    private var headerView: View? = null
    private var header: TextView? = null

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val pos = parent.getChildAdapterPosition(view)
        if (sectionCallback.isSection(pos)) {
            outRect.top = headerOffset
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        if (headerView == null) {
            headerView = inflateHeaderView(parent, headerViewId)
//            header = headerView!!.findViewById(R.id.<tv_id>)
            fixLayoutSize(headerView!!, parent)
        }

        var previousHeader: CharSequence = ""
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)

            val title = sectionCallback.getSectionHeader(position)
            header!!.text = title
            if (!previousHeader.toString().equals(title.toString()) && sectionCallback.isSection(position)) {
                drawHeader(c, child, headerView!!)
                previousHeader = title
            }
        }
    }

    private fun drawHeader(c: Canvas, child: View, headerView: View) {
        c.save()
        if (sticky) {
            c.translate(0f, Math.max(0, child.top - headerView.height).toFloat())
        } else {
            c.translate(0f, (child.top - headerView.height).toFloat())
        }
        headerView.draw(c)
        c.restore()
    }

    private fun inflateHeaderView(parent: RecyclerView, headerViewId: Int): View {
        return LayoutInflater.from(parent.context)
            .inflate(headerViewId, parent, false)
    }

    /**
     * Measures the header view to make sure its size is greater than 0 and will be drawn
     * https://yoda.entelect.co.za/view/9627/how-to-android-recyclerview-item-decorations
     */
    private fun fixLayoutSize(view: View, parent: ViewGroup) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(
            parent.width + parent.paddingLeft + parent.paddingRight,
            View.MeasureSpec.EXACTLY
        )
        val heightSpec = View.MeasureSpec.makeMeasureSpec(
            parent.height,
            View.MeasureSpec.UNSPECIFIED
        )

        val childWidth = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeight = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )

        view.measure(childWidth, childHeight)

        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    interface SectionCallback {

        fun isSection(position: Int): Boolean

        fun getSectionHeader(position: Int): CharSequence
    }

}