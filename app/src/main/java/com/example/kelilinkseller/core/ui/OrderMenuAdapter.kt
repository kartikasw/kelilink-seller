package com.example.kelilinkseller.core.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.domain.model.Order
import com.example.kelilinkseller.databinding.ItemOrderedMenuBinding
import com.example.kelilinkseller.databinding.ItemOrderedMenuWithNoteBinding

class OrderMenuAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemList = ArrayList<Order>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Order>?) {
        itemList.clear()

        if (items == null) {
            notifyDataSetChanged()
            return
        } else {
            itemList.addAll(items)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when(viewType) {
            NOTE -> {
                val view =
                    ItemOrderedMenuWithNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WithNoteViewHolder(view)
            }
            WITHOUT_NOTE -> {
                val view =
                    ItemOrderedMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WithoutNoteViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is WithNoteViewHolder -> { holder.bind(itemList[position]) }
            is WithoutNoteViewHolder -> { holder.bind(itemList[position]) }
        }
    }

    inner class WithNoteViewHolder(
        private val binding: ItemOrderedMenuWithNoteBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            with(binding) {
                iomwnTvName.text = itemView.context.resources.getString(R.string.ordered_menu_format, order.amount, order.name)
                iomwnTvNote.text = order.note
                iomwnTvPrice.text = order.total_price.toString()
            }
        }
    }

    inner class WithoutNoteViewHolder(
        private val binding: ItemOrderedMenuBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            with(binding) {
                iomTvName.text = itemView.context.resources.getString(R.string.ordered_menu_format, order.amount, order.name)
                iomTvPrice.text = order.total_price.toString()
            }
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int =
        when(itemList[position].note) {
            "" -> WITHOUT_NOTE
            else -> NOTE
        }

    companion object {
        const val NOTE = 0
        const val WITHOUT_NOTE = 1
    }
}