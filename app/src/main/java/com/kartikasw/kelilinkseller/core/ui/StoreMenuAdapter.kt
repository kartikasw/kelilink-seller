package com.kartikasw.kelilinkseller.core.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kartikasw.kelilinkseller.databinding.ItemStoreMenuBinding

class StoreMenuAdapter : RecyclerView.Adapter<StoreMenuAdapter.MyViewHolder>() {

    private var itemList = ArrayList<StoreMenu>()

    var onItemClick: ((StoreMenu) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(menu: ArrayList<StoreMenu>) {
        itemList.apply {
            clear()
            addAll(menu)
        }
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val binding: ItemStoreMenuBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(itemList[adapterPosition])
            }
        }

        fun bind(data: StoreMenu) {
            with(binding) {
                ismTvName.text = data.name
                ismIvMenu.setImageResource(data.icon)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemStoreMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size
}

data class StoreMenu(
    val name: String,
    val icon: Int
)