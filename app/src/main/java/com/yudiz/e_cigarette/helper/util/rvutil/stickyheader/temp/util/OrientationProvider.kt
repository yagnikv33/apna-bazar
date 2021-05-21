package com.yudiz.e_cigarette.helper.util.rvutil.stickyheader.temp.util

/**
 * Interface for getting the orientation of a RecyclerView from its LayoutManager
 */
interface OrientationProvider {

    fun getOrientation(recyclerView: androidx.recyclerview.widget.RecyclerView): Int

    fun isReverseLayout(recyclerView: androidx.recyclerview.widget.RecyclerView): Boolean
}
