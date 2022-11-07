package com.example.kelilinkseller.core.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.domain.model.Menu
import com.example.kelilinkseller.databinding.ItemMenuBinding

class MenuAdapter: RecyclerView.Adapter<MenuAdapter.MyViewHolder>() {

    private var itemList = ArrayList<Menu>()

    var onItemClick: ((Menu) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Menu>?) {
        if (items == null) return
        itemList.apply {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class MyViewHolder(
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
}