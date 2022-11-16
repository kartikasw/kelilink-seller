package com.example.kelilinkseller.features.order.new_order.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.core.ui.OrderMenuAdapter
import com.example.kelilinkseller.databinding.ActivityDetailOrderNewBinding
import com.example.kelilinkseller.util.dateFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailOrderNewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOrderNewBinding

    private lateinit var invoiceId: String

    private val detailOrderNewViewModel: DetailOrderNewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()

        showInvoiceInfo()
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.donToolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.page_detail_order)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun showInvoiceInfo() {
        invoiceId = detailOrderNewViewModel.getInvoiceId()

        detailOrderNewViewModel.getOrderById(invoiceId).observe(this) {
            when(it) {
                is Resource.Success -> {
                    showLoadingState(false)
                    setUpInvoiceView(it.data!!)
                    showInfo(true)
                }
                is Resource.Loading -> {
                    showInfo(false)
                    showLoadingState(true)
                }
                is Resource.Error -> {
                    showLoadingState(false)
                    showInfo(false)
                    Log.e(TAG, it.message.toString())
                }
            }
        }
    }

    private fun showLoadingState(state: Boolean) {
        binding.donLoading.root.isVisible = state
    }

    private fun showInfo(state: Boolean) {
        binding.donLayoutInfo.root.isVisible = state
    }

    private fun setUpInvoiceView(invoice: Invoice) {
        with(binding) {
            donLayoutInfo.cdoLayoutUser.cudoTvName.text = invoice.id
            donLayoutInfo.cdoLayoutUser.cudoTvTime.text = dateFormat.format(invoice.time)
            donTvTotal.text = "Rp${invoice.total_price}"

            val orderMenuAdapter = OrderMenuAdapter()

            orderMenuAdapter.setItems(invoice.orders)

            donLayoutInfo.cdoRvMenu.apply {
                layoutManager = LinearLayoutManager(this@DetailOrderNewActivity)
                adapter = orderMenuAdapter
            }
        }
    }

    companion object {
        const val TAG = "DetailOrderNewActivity"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}