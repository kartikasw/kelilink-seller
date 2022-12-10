package com.example.kelilinkseller.core.ui

import android.annotation.SuppressLint
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.domain.model.Menu
import com.example.kelilinkseller.databinding.ItemMenuBinding
import com.example.kelilinkseller.databinding.ItemMenuOutOfStockBinding

class StockAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((Menu) -> Unit)? = null

    private var itemList = ArrayList<Menu>()

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Menu>?) {
        if (items == null) return
        itemList.apply {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when(viewType) {
            AVAILABLE -> {
                val view =
                    ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                AvailableViewHolder(view)
            }
            OUT_OF_STOCK -> {
                val view =
                    ItemMenuOutOfStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                OutOfStockViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is AvailableViewHolder -> { holder.bind(itemList[position]) }
            is OutOfStockViewHolder -> { holder.bind(itemList[position]) }
        }
    }

    override fun getItemCount(): Int = itemList.size

    inner class AvailableViewHolder(
        private val binding: ItemMenuBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(itemList[adapterPosition])
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(menu: Menu) {
            val corner = itemView.context.resources.getDimensionPixelSize(R.dimen.corner_image)
            with(binding) {
                Glide.with(itemView.context)
                    .load(menu.image)
                    .transform(CenterCrop(), RoundedCorners(corner))
                    .into(imIvMenu)

                imTvTitle.text = menu.name
                imTvPriceAndUnit.text = "${menu.price}/${menu.unit}"
            }
        }
    }

    inner class OutOfStockViewHolder(
        private val binding: ItemMenuOutOfStockBinding
    ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(itemList[adapterPosition])
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(menu: Menu) {
            val corner = itemView.context.resources.getDimensionPixelSize(R.dimen.corner_image)
            with(binding) {
                Glide.with(itemView.context)
                    .load(menu.image)
                    .transform(CenterCrop(), RoundedCorners(corner))
                    .into(imoosIvMenu)

                val colorMatrix = ColorMatrix()
                colorMatrix.setSaturation(0f)
                imoosIvMenu.colorFilter = ColorMatrixColorFilter(colorMatrix)

                imoosTvTitle.text = menu.name
                imoosTvPriceAndUnit.text = "${menu.price}/${menu.unit}"
            }
        }
    }

    override fun getItemViewType(position: Int): Int =
        when(itemList[position].available) {
            true -> AVAILABLE
            false -> OUT_OF_STOCK
        }

    companion object {
        const val AVAILABLE = 0
        const val OUT_OF_STOCK  = 1
    }
}