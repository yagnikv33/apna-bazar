package com.yudiz.e_cigarette.main.base.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.yudiz.BR

open class BaseRvBindingAdapter<T>(
    private var layoutId: Int = 0,
    var list: MutableList<T>,
    private var getLayout: ((viewType : Int) -> Int)? = null,
    private var getBR: ((position : Int) -> Int)? = null,
    private var viewType: ((position : Int, item : T) -> Int)? = null,
    private var br: Int = -1,
    private var brs: Map<Int, Any>? = null,
    private var clickListener: ((view: View, item: T, pos : Int) -> Unit)? = null,
    private var viewHolder: ((holder: RecyclerView.ViewHolder, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<BaseRvBindingAdapter<T>.ViewHolder>() {

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                if (getLayout == null)
                    layoutId
                else getLayout!!(viewType),
                parent,
                false
            )
        )

    override fun getItemViewType(position: Int): Int {
        return if (viewType != null) viewType!!(position, list[position])
        else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (br != -1) holder.binding.setVariable(
            if (getBR == null) br else getBR!!(position),
            list[holder.adapterPosition]
        )

        brs?.forEach { (br, value) ->
            holder.binding.setVariable(br, value)
        }

//        holder.binding.setVariable(BR.position, position)

        viewHolder?.invoke(holder,position)

        holder.binding.setVariable(
            BR.click,
            View.OnClickListener { v ->
                if (holder.adapterPosition >= 0) clickListener?.invoke(
                    v,
                    list[holder.adapterPosition],
                    holder.adapterPosition
                )
            })

        holder.binding.executePendingBindings()
    }

    fun addData(list: List<T>, insertPos: Int = -1, isClear: Boolean = false) {
        if (isClear) {
            this.list.clear()
            this.list.addAll(list)
            notifyDataSetChanged()
        } else {
            val listSize = this.list.size
            if (insertPos == -1) {
                this.list.addAll(list)
                notifyItemRangeInserted(listSize, list.size)
                /**
                 * done to notify last decoration of last item of old list
                 * https://stackoverflow.com/a/47355363
                 */
                notifyItemChanged(listSize - 1, false)
            } else {
                this.list.addAll(insertPos, list)
                notifyItemRangeInserted(insertPos, list.size)
                notifyItemChanged(insertPos - 1, false)
            }
        }
    }

    fun addDataInReverse(list: List<T>, isClear: Boolean = false) {
        if (isClear) {
            this.list.clear()
            this.list.addAll(list)
            notifyDataSetChanged()
        } else {
            this.list.addAll(0, list)

            notifyItemRangeInserted(0, list.size)
        }
    }

    inner class ViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root)
}