package com.yudiz.e_cigarette.helper.util.rvutil.stickyheader.temp.caching

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yudiz.e_cigarette.helper.util.rvutil.stickyheader.temp.StickyRecyclerHeadersAdapter
import com.yudiz.e_cigarette.helper.util.rvutil.stickyheader.temp.util.OrientationProvider

/**
 * An implementation of [HeaderProvider] that creates and caches header views
 */
class HeaderViewCache(
    private val mAdapter: StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>,
    private val mOrientationProvider: OrientationProvider
) : HeaderProvider {
    private val mHeaderViews = androidx.collection.LongSparseArray<View>()

    override fun getHeader(parent: androidx.recyclerview.widget.RecyclerView, position: Int): View {
        val headerId = mAdapter.getHeaderId(position)

        var header: View? = mHeaderViews.get(headerId)
        if (header == null) {
            //TODO - recycle views
            val viewHolder = mAdapter.onCreateHeaderViewHolder(parent)
            mAdapter.onBindHeaderViewHolder(viewHolder, position)
            header = viewHolder.itemView
            if (header!!.layoutParams == null) {
                header.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            val widthSpec: Int
            val heightSpec: Int

            if (mOrientationProvider.getOrientation(parent) == androidx.recyclerview.widget.LinearLayoutManager.VERTICAL) {
                widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
                heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)
            } else {
                widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.UNSPECIFIED)
                heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.EXACTLY)
            }

            val childWidth = ViewGroup.getChildMeasureSpec(
                widthSpec,
                parent.paddingLeft + parent.paddingRight, header.layoutParams.width
            )
            val childHeight = ViewGroup.getChildMeasureSpec(
                heightSpec,
                parent.paddingTop + parent.paddingBottom, header.layoutParams.height
            )
            header.measure(childWidth, childHeight)
            header.layout(0, 0, header.measuredWidth, header.measuredHeight)
            mHeaderViews.put(headerId, header)
        }
        return header
    }

    override fun invalidate() {
        mHeaderViews.clear()
    }
}
