package com.example.kelilinkseller.features.store.stock.edit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.databinding.ActivityEditStockBinding
import com.example.kelilinkseller.features.store.stock.StockActivity
import com.example.kelilinkseller.features.store.stock.StockActivity.Companion.EXTRA_MENU_ID
import com.example.kelilinkseller.features.store.stock.StockActivity.Companion.EXTRA_MENU_STOCK
import com.example.kelilinkseller.util.custom_view.KelilinkLoadingDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditStockActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditStockBinding

    private val editStockViewModel: EditStockViewModel by viewModels()

    private lateinit var loading: KelilinkLoadingDialog

    private var menuId: String = ""
    private var menuStock: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditStockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loading = KelilinkLoadingDialog(this)

        menuId = intent?.getStringExtra(EXTRA_MENU_ID).toString()
        menuStock = intent?.getBooleanExtra(EXTRA_MENU_STOCK, true)!!

        setUpToolbar()

        setUpSwitchView()

        setOnClickLister()
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.esToolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.page_edit_stock)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setUpSwitchView() {
        binding.esSwitchStock.isChecked = menuStock
    }

    private fun setOnClickLister() {
        with(binding) {
            esSwitchStock.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked) {
                    editStock(true)
                } else {
                    editStock(false)
                }
            }
        }
    }

    private fun editStock(stock: Boolean) {
        editStockViewModel.updateMenuStock(menuId, stock).observe(this) {
            when(it) {
                is Resource.Success -> {
                    loading.dismiss()
                    val intent = Intent(this, StockActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
                is Resource.Loading -> {
                    loading.show()
                }
                is Resource.Error -> {
                    Log.e(TAG, it.message.toString())
                }
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val TAG = "EditStockActivity"
    }
}