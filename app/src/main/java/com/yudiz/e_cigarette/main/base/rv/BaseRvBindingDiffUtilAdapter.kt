package com.yudiz.e_cigarette.main.base.rv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.yudiz.BR

class BaseRvBindingDiffUtilAdapter<T>(
    private var layoutId: Int,
    list: List<T>,
    private var br: Int = -1,
    private var brs: Map<Int, Any>? = null,
    var areItemsSame: (Int, Int) -> Boolean,
    var areContentsSame: (Int, Int) -> Boolean,
    private var clickListener: ((View, T, Int) -> Unit)? = null
) : androidx.recyclerview.widget.RecyclerView.Adapter<BaseRvBindingDiffUtilAdapter<T>.ViewHolder>() {

    var list: MutableList<T> = list.toMutableList()

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layoutId,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (br != -1) holder.binding.setVariable(br, list[holder.adapterPosition])

        brs?.forEach { (br, value) ->
            holder.binding.setVariable(br, value)
        }

        holder.binding.setVariable(
            BR.click,
            View.OnClickListener { v ->
                if (holder.adapterPosition >= 0) clickListener?.invoke(
                    v,
                    list[holder.adapterPosition],
                    holder.adapterPosition
                )
            })
    }

    fun addData(list: List<T>, insertPos: Int = -1, isClear: Boolean = false) {
        val changedList = mutableListOf<T>()

        if (isClear)
            changedList.addAll(list)
        else {
            changedList.addAll(this.list)
            if (insertPos != -1)
                changedList.addAll(insertPos, list)
            else
                changedList.addAll(list)
        }

        DiffUtil.calculateDiff(
            DiffCallback(this.list, changedList), false
        ).let {
            this.list = changedList
            it.dispatchUpdatesTo(this)
        }
    }

    fun addDataInReverse(list: List<T>, isClear: Boolean = false) {
        val changedList = mutableListOf<T>()

        if (isClear)
            changedList.addAll(list)
        else {
            changedList.addAll(this.list)
            changedList.addAll(0, list)
        }

        DiffUtil.calculateDiff(
            DiffCallback(this.list, changedList), false
        ).let {
            this.list = changedList
            it.dispatchUpdatesTo(this)
        }
    }

    inner class ViewHolder(val binding: ViewDataBinding) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)

    inner class DiffCallback(private val oldList: List<T>, private val newList: List<T>) :
        DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return areItemsSame(oldItemPosition, newItemPosition)
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return areContentsSame(oldItemPosition, newItemPosition)
        }
    }
}