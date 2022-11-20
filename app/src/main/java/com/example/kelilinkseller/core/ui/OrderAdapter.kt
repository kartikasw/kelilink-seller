package com.example.kelilinkseller.core.ui

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.databinding.ItemOrderCookingBinding
import com.example.kelilinkseller.databinding.ItemOrderDoneBinding
import com.example.kelilinkseller.databinding.ItemOrderReadyBinding
import com.example.kelilinkseller.databinding.ItemOrderWaitingBinding
import com.example.kelilinkseller.util.dateFormat

class OrderAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val itemList = ArrayList<Invoice>()

    var onItemClick: ((Invoice) -> Unit)? = null

    var onAcceptClick: ((Invoice) -> Unit)? = null

    var onDeclineClick: ((Invoice) -> Unit)? = null

    var onReadyClick: ((Invoice) -> Unit)? = null

    var onDoneClick: ((Invoice) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Invoice>?) {
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

    @SuppressLint("NotifyDataSetChanged")
    inner class WaitingViewHolder(
        private val binding: ItemOrderWaitingBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(itemList[adapterPosition])
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(invoice: Invoice) {
            with(binding) {

                val timer: CountDownTimer = object : CountDownTimer(15000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        val seconds = millisUntilFinished/1000
                        binding.iowBtnAccept.text = "Terima 00:${(seconds % 60).toString().padStart(2, '0')}"
                    }

                    override fun onFinish() {

                    }
                }.start()

                iowBtnDecline.setOnClickListener {
                    onDeclineClick?.invoke(itemList[adapterPosition])
                }

                iowBtnAccept.setOnClickListener {
                    timer.cancel()
                    onAcceptClick?.invoke(itemList[adapterPosition])
                }

                iowTvUserName.text = "${invoice.id.take(10)}..."
                iowTvTime.text = dateFormat.format(invoice.time)

                val orderMenuAdapter = OrderMenuAdapter()
                orderMenuAdapter.setItems(invoice.orders)
                with(iowRvMenu) {
                    layoutManager = LinearLayoutManager(context)
                    adapter = orderMenuAdapter
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    inner class CookingViewHolder(
        private val binding: ItemOrderCookingBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.iocBtnReady.setOnClickListener {
                onReadyClick?.invoke(itemList[adapterPosition])
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(itemList[adapterPosition])
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(invoice: Invoice) {
            with(binding) {
                iocTvUserName.text = "${invoice.id.take(10)}..."
                iocTvTime.text = dateFormat.format(invoice.time)

                val orderMenuAdapter = OrderMenuAdapter()
                orderMenuAdapter.setItems(invoice.orders)
                with(iocRvMenu) {
                    layoutManager = LinearLayoutManager(context)
                    adapter = orderMenuAdapter
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    inner class ReadyViewHolder(
        private val binding: ItemOrderReadyBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.iorBtnDone.setOnClickListener {
                onDoneClick?.invoke(itemList[adapterPosition])
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(itemList[adapterPosition])
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(invoice: Invoice) {
            with(binding) {
                iorTvUserName.text = "${invoice.id.take(10)}..."
                iorTvTime.text = dateFormat.format(invoice.time)

                val orderMenuAdapter = OrderMenuAdapter()
                orderMenuAdapter.setItems(invoice.orders)
                with(iorRvMenu) {
                    layoutManager = LinearLayoutManager(context)
                    adapter = orderMenuAdapter
                }
            }
        }
    }

    inner class DoneViewHolder(
        private val binding: ItemOrderDoneBinding
    ): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(itemList[adapterPosition])
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(invoice: Invoice) {
            with(binding) {
                iodTvUserName.text = "${invoice.id.take(10)}..."
                iodTvTime.text = dateFormat.format(invoice.time)

                val orderMenuAdapter = OrderMenuAdapter()
                orderMenuAdapter.setItems(invoice.orders)
                with(iodRvMenu) {
                    layoutManager = LinearLayoutManager(context)
                    adapter = orderMenuAdapter
                }
            }
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