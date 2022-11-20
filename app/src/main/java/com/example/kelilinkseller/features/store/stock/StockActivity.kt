package com.example.kelilinkseller.features.store.stock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Menu
import com.example.kelilinkseller.core.ui.StockAdapter
import com.example.kelilinkseller.databinding.ActivityStockBinding
import com.example.kelilinkseller.features.store.stock.edit.EditStockActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StockActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStockBinding

    private val viewModel: StockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()

        showMenu()

    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.sToolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.page_stock)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun showMenu() {
        viewModel.getMyMenu().observe(this) {
            when(it) {
                is Resource.Success -> {
                    if(it.data != null) {
                        showEmptyState(false)
                        setUpMenuView(it.data)
                        showLoadingState(false)
                    } else {
                        showLoadingState(false)
                        showEmptyState(true)
                    }
                }
                is Resource.Loading -> {
                    showEmptyState(false)
                    showLoadingState(true)
                }
                is Resource.Error -> {
                    showEmptyState(false)
                    showLoadingState(false)
                    Log.e(TAG, it.message.toString())
                    Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setUpMenuView(data: List<Menu>?) {
        val stockAdapter = StockAdapter(this)

        stockAdapter.onItemClick = {
            val intent = Intent(this, EditStockActivity::class.java)
            intent
                .putExtra(EXTRA_MENU_ID, it.id)
                .putExtra(EXTRA_MENU_STOCK, it.available)
            startActivity(intent)
        }

        stockAdapter.setItems(data!!)

        with(binding.sRvMenu) {
            layoutManager = LinearLayoutManager(this@StockActivity)
            adapter = stockAdapter
        }
    }

    private fun showLoadingState(state: Boolean) {
        binding.sLoading.root.isVisible = state
    }
    private fun showEmptyState(state: Boolean) {
        binding.sEmpty.root.isVisible = state
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val TAG = "StockActivity"
        const val EXTRA_MENU_ID = "menu_id"
        const val EXTRA_MENU_STOCK = "menu_stock"
    }

}