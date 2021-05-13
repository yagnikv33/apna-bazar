package com.eastmeeteast.helper.recyclerViewUtil.sticky_header.util

/**
 * Interface for getting the orientation of a RecyclerView from its LayoutManager
 */
interface OrientationProvider {

    fun getOrientation(recyclerView: androidx.recyclerview.widget.RecyclerView): Int

    fun isReverseLayout(recyclerView: androidx.recyclerview.widget.RecyclerView): Boolean
}
