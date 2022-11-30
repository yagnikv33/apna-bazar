package com.esjayit.apnabazar.helper.util.rvutil

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.esjayit.apnabazar.main.base.rv.BaseRvAdapter
import com.esjayit.apnabazar.main.base.rv.BaseRvBindingAdapter
import com.esjayit.apnabazar.main.base.rv.BaseRvBindingDiffUtilAdapter

class RvUtil(
    rv: RecyclerView,
    lm: RecyclerView.LayoutManager? = null,
    adapter: RecyclerView.Adapter<*>,
    isInitialisation: Boolean = false,
    decoration: RecyclerView.ItemDecoration? = null,
    noDataViews: List<View>? = null,
    dataViews: List<View>? = null,
    loadMoreViews: List<View>? = null
) {

    private var rv: RecyclerView? = null
    private var lm: RecyclerView.LayoutManager? = null
    private var noDataViews: List<View>? = null
    private var dataViews: List<View>? = null
    private var onLoadMoreListener: OnLoadMoreListener? = null
    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    var rvAdapter: RecyclerView.Adapter<*>? = null

    init {
        this.rv = rv
        this.lm = lm
        this.noDataViews = noDataViews
        this.dataViews = dataViews

        if (lm != null)
            this.rv!!.layoutManager = lm
        else this.lm = rv.layoutManager

        this.rvAdapter = adapter
        this.rv?.adapter = adapter

        manageVisibility(isInitialisation)

        if (decoration != null)
            this.rv?.run {
                /**
                 * remove already added decorations
                 */
                for (index in 0 until itemDecorationCount)
                    removeItemDecorationAt(index)

                addItemDecoration(decoration)
            }
    }

    fun notifyAdapter() {
        rvAdapter?.notifyDataSetChanged()
        manageVisibility()
    }

    fun itemRemoved(pos: Int) {
        val dataList = getDataList()!!
        if (dataList.lastIndex >= pos) {
            dataList.removeAt(pos)
            rvAdapter?.notifyItemRemoved(pos)
            rvAdapter?.notifyItemRangeChanged(pos, getDataList()!!.size)
            manageVisibility()
        }
    }

    private fun manageVisibility(isInitialisation: Boolean = false) {
        if (isInitialisation)
            hideEverything()
        else {
            if (rvAdapter == null || rvAdapter!!.itemCount == 0)
                showNoData()
            else
                hideNoData()
        }
    }

    private fun hideNoData() {
        noDataViews?.forEach { it.visibility = View.GONE }
        dataViews?.forEach { it.visibility = View.VISIBLE }
    }

    private fun hideEverything() {
        noDataViews?.forEach { it.visibility = View.GONE }
        dataViews?.forEach { it.visibility = View.GONE }
    }

    private fun showNoData() {
        noDataViews?.forEach { it.visibility = View.VISIBLE }
        dataViews?.forEach { it.visibility = View.GONE }
    }

    fun getDataList(): MutableList<Any>? {
        return when (rvAdapter) {
            is BaseRvAdapter<*> -> (rvAdapter as BaseRvAdapter<Any>).list
            is BaseRvBindingAdapter<*> -> (rvAdapter as BaseRvBindingAdapter<Any>).list
            else -> (rvAdapter as BaseRvBindingDiffUtilAdapter<Any>).list
        }
    }

}