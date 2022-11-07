package com.example.kelilinkseller.core.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kelilinkseller.R
import com.example.kelilinkseller.databinding.ItemStoreMenuBinding

class StoreMenuAdapter(private val context: Context): RecyclerView.Adapter<StoreMenuAdapter.MyViewHolder>() {

    private var itemList = ArrayList<StoreMenu>()

    var onItemClick: ((StoreMenu) -> Unit)? = null

    fun setItems() {
        val name = context.resources.getStringArray(R.array.menu_name)
        val icon = context.resources.obtainTypedArray(R.array.menu_icon)
        for (i in name.indices) {
            val category = StoreMenu(name[i], icon.getResourceId(i, -1))
            itemList.add(category)
        }
        icon.recycle()
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