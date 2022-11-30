package com.esjayit.apnabazar.helper.util.rvutil

abstract class EndlessRecyclerViewScrollListener(
    internal var mLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager,
    isLinear: Boolean,
    var isReverse: Boolean
) : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
    private var visibleThreshold = 5
    private var currentPage = 0
    private var previousTotalItemCount = 0
    private var loading = true
    private var startingPageIndex = 0

    init {
        if (!isLinear)
            visibleThreshold *= (mLayoutManager as androidx.recyclerview.widget.GridLayoutManager).spanCount
    }

    fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    override fun onScrolled(view: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        val totalItemCount = mLayoutManager.itemCount

        if (mLayoutManager is androidx.recyclerview.widget.StaggeredGridLayoutManager) {
            val lastVisibleItemPositions =
                (mLayoutManager as androidx.recyclerview.widget.StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
        } else if (mLayoutManager is androidx.recyclerview.widget.GridLayoutManager) {
            lastVisibleItemPosition = (mLayoutManager as androidx.recyclerview.widget.GridLayoutManager).findLastVisibleItemPosition()
        } else if (mLayoutManager is androidx.recyclerview.widget.LinearLayoutManager) {
            lastVisibleItemPosition = if (isReverse) (mLayoutManager as androidx.recyclerview.widget.LinearLayoutManager).findFirstVisibleItemPosition() else
                (mLayoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition()
        }

        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if (!loading && if (isReverse) lastVisibleItemPosition == 0 else lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            currentPage++
            onLoadMore(currentPage, totalItemCount, view)
            loading = true
        }
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: androidx.recyclerview.widget.RecyclerView?)

}

interface OnLoadMoreListener {
}