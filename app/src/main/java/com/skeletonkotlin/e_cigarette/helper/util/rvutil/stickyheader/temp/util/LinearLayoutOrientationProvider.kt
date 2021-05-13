package com.eastmeeteast.helper.recyclerViewUtil.sticky_header.util

/**
 * OrientationProvider for ReyclerViews who use a LinearLayoutManager
 */
class LinearLayoutOrientationProvider : OrientationProvider {

    override fun getOrientation(recyclerView: androidx.recyclerview.widget.RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager
        throwIfNotLinearLayoutManager(layoutManager!!)
        return (layoutManager as androidx.recyclerview.widget.LinearLayoutManager).orientation
    }

    override fun isReverseLayout(recyclerView: androidx.recyclerview.widget.RecyclerView): Boolean {
        val layoutManager = recyclerView.layoutManager
        throwIfNotLinearLayoutManager(layoutManager!!)
        return (layoutManager as androidx.recyclerview.widget.LinearLayoutManager).reverseLayout
    }

    private fun throwIfNotLinearLayoutManager(layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager) {
        if (layoutManager !is androidx.recyclerview.widget.LinearLayoutManager) {
            throw IllegalStateException("StickyListHeadersDecoration can only be used with a " + "LinearLayoutManager.")
        }
    }
}
