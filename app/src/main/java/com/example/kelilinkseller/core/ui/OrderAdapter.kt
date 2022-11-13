package com.example.kelilinkseller.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.databinding.ItemOrderCookingBinding
import com.example.kelilinkseller.databinding.ItemOrderDoneBinding
import com.example.kelilinkseller.databinding.ItemOrderReadyBinding
import com.example.kelilinkseller.databinding.ItemOrderWaitingBinding
import com.example.kelilinkseller.util.dateFormat

class OrderAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemList = ArrayList<Invoice>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when(viewType) {
            WAITING -> {
                val view =
                    ItemOrderWaitingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                WaitingViewHolder(view)
            }
            COOKING -> {
                val view =
                    ItemOrderCookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CookingViewHolder(view)
            }
            READY -> {
                val view =
                    ItemOrderReadyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ReadyViewHolder(view)
            }
            DONE -> {
                val view =
                    ItemOrderDoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DoneViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is WaitingViewHolder -> { holder.bind(itemList[position]) }
            is CookingViewHolder -> { holder.bind(itemList[position]) }
            is ReadyViewHolder -> { holder.bind(itemList[position]) }
            is DoneViewHolder -> { holder.bind(itemList[position]) }
        }
    }

    inner class WaitingViewHolder(
        private val binding: ItemOrderWaitingBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(invoice: Invoice) {
            with(binding) {
                iowTvUserName.text = invoice.id
                iowTvTime.text = dateFormat.format(invoice.time)
            }
        }
    }

    inner class CookingViewHolder(
        private val binding: ItemOrderCookingBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(invoice: Invoice) {

        }
    }

    inner class ReadyViewHolder(
        private val binding: ItemOrderReadyBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(invoice: Invoice) {

        }
    }

    inner class DoneViewHolder(
        private val binding: ItemOrderDoneBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(invoice: Invoice) {

        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int =
        when(itemList[position].status) {
            "waiting" -> WAITING
            "cooking" -> COOKING
            "ready" -> READY
            else -> DONE
        }

    companion object {
        const val WAITING = 0
        const val COOKING = 1
        const val READY  = 2
        const val DONE = 3
    }
}