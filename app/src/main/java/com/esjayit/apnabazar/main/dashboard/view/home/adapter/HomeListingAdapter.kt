package com.esjayit.apnabazar.main.dashboard.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.esjayit.apnabazar.data.model.response.ListItem
import com.esjayit.databinding.RawHomeItem1Binding
import com.esjayit.databinding.RawHomeItem2Binding

class HomeListingAdapter(
    private val list: List<ListItem> = arrayListOf(),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_1 = 0
        const val ITEM_2 = 1
    }

    fun getList(): List<ListItem> {
        return list
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position].code) {
            "1" -> {
                ITEM_1
            }
            "0" -> {
                ITEM_2
            }
            else -> {
                ITEM_1
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_1 -> {
                val binding = RawHomeItem1Binding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                Item1ViewHolder(binding)
            }
            ITEM_2 -> {
                val binding = RawHomeItem2Binding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                Item2ViewHolder(binding)
            }
            else -> {
                val binding = RawHomeItem1Binding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                Item1ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is Item1ViewHolder -> {
                holder.bind(list[position])
            }
            is Item2ViewHolder -> {
                holder.bind(list[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class Item1ViewHolder(val binding: RawHomeItem1Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listItem: ListItem) {
            binding.tvMessage.text = listItem.message
        }

    }

    class Item2ViewHolder(val binding: RawHomeItem2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listItem: ListItem) {
            binding.tvMessage.text = listItem.message
        }
    }

}