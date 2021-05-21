package com.yudiz.e_cigarette.main.base.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView

open class BaseRvAdapter<T>() : RecyclerView.Adapter<BaseRvAdapter<T>.ViewHolder>() {
    private var layoutId: Int = 0
    private lateinit var getLayout: (Int) -> Int
    private lateinit var viewType: (Int, T) -> Int
    var list: MutableList<T>? = null
    private var isAnimate: Boolean = true
    private var viewHolder: ((RecyclerView.ViewHolder, T) -> Unit)? = null
    private val clickableViews = ArrayList<Int>()
    private var clickListener: ((RecyclerView.ViewHolder, View, T, Int) -> Unit)? = null

    constructor(
        layoutId: Int, list: MutableList<T>?,
        viewHolder: ((RecyclerView.ViewHolder, T) -> Unit)
    ) : this(layoutId, list, viewHolder, listOf(), null)

    constructor(
        viewType: (Int, T) -> Int,
        layoutId: (viewType: Int) -> Int,
        list: MutableList<T>?,
        viewHolder: ((RecyclerView.ViewHolder, T) -> Unit),
        clickableViews: List<Int>,
        clickListener: ((RecyclerView.ViewHolder, View, T, Int) -> Unit)? = null
    ) : this() {
        this.list = list
        this.viewType = viewType
        this.getLayout = layoutId
        this.viewHolder = viewHolder
        this.clickableViews.addAll(clickableViews)
        this.clickListener = clickListener
    }

    constructor(
        layoutId: Int,
        list: MutableList<T>?,
        viewHolder: ((RecyclerView.ViewHolder, T) -> Unit),
        clickableViews: List<Int>,
        clickListener: ((RecyclerView.ViewHolder, View, T, Int) -> Unit)?
    ) : this() {
        this.layoutId = layoutId
        this.list = list
        this.viewHolder = viewHolder
        this.clickableViews.addAll(clickableViews)
        this.clickListener = clickListener
    }

    override fun getItemCount() = list?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        return if (::viewType.isInitialized) viewType(position, list!![position])
        else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                if (layoutId != 0)
                    layoutId
                else getLayout(viewType), parent, false
            )
        )
    }

    fun addData(list: List<T>, insertPos: Int = -1, isClear: Boolean = false) {
        if (isClear) {
            this.list?.clear()
            this.list?.addAll(list)
            notifyDataSetChanged()
        } else {
            val listSize = this.list?.size ?: 0
            if (insertPos == -1) {
                this.list?.addAll(list)
                notifyItemRangeInserted(listSize, list.size)
                /**
                 * done to notify last decoration of last item of old list
                 * https://stackoverflow.com/a/47355363
                 */
                notifyItemChanged(listSize - 1, false)
            } else {
                this.list?.addAll(insertPos, list)
                notifyItemRangeInserted(insertPos, list.size)
                notifyItemChanged(insertPos - 1, false)
            }
        }
    }

    fun addDataInReverse(list: List<T>, isClear: Boolean = false) {
        if (isClear) {
            this.list?.clear()
            this.list?.addAll(list)
            notifyDataSetChanged()
        } else {
            this.list?.addAll(0, list)

            notifyItemRangeInserted(0, list.size)
        }
    }

    fun isAnimate(boolean: Boolean) {
        isAnimate = boolean
    }

    fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 500
        view.startAnimation(anim)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewHolder?.let { it(holder, list!![position]) }
//        if (isAnimate) {
//            setFadeAnimation(holder.itemView)
//        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            for (clickableView in clickableViews)
                view.findViewById<View>(clickableView).setOnClickListener { v ->
                    clickListener?.invoke(
                        this,
                        v,
                        list!![adapterPosition],
                        adapterPosition
                    )
                }
        }
    }
}
