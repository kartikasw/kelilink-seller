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

    private var itemList = ArrayList<Invoice>()

    var onAcceptClick: ((Invoice) -> Unit)? = null

    var onDeclineClick: ((Invoice) -> Unit)? = null

    var onReadyClick: ((Invoice) -> Unit)? = null

    var onDoneClick: ((Invoice) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Invoice>?) {
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

        private val timer: CountDownTimer = object : CountDownTimer(59000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished/1000
                binding.iowBtnAccept.text = "Terima 00:${(seconds % 60).toString().padStart(2, '0')}"
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFinish() {
                itemList.remove(itemList[adapterPosition])
                notifyDataSetChanged()
            }
        }.start()

        init {
            with(binding) {
                iowBtnDecline.setOnClickListener {
                    onDeclineClick?.invoke(itemList[adapterPosition])
                }

                iowBtnAccept.setOnClickListener {
                    timer.cancel()
                    onAcceptClick?.invoke(itemList[adapterPosition])
                }

            }
        }

        fun bind(invoice: Invoice) {
            with(binding) {
                iowTvUserName.text = invoice.id
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

    inner class CookingViewHolder(
        private val binding: ItemOrderCookingBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.iocBtnReady.setOnClickListener {
                onReadyClick?.invoke(itemList[adapterPosition])
            }
        }

        fun bind(invoice: Invoice) {
            with(binding) {
                iocTvUserName.text = invoice.id
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

    inner class ReadyViewHolder(
        private val binding: ItemOrderReadyBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.iorBtnDone.setOnClickListener {
                onDoneClick?.invoke(itemList[adapterPosition])
            }
        }

        fun bind(invoice: Invoice) {
            with(binding) {
                iorTvUserName.text = invoice.id
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
        fun bind(invoice: Invoice) {
            with(binding) {
                iodTvUserName.text = invoice.id
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