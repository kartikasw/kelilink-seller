package com.example.kelilinkseller.features.order.ready.detail

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
import com.example.kelilinkseller.databinding.ActivityDetailOrderReadyBinding
import com.example.kelilinkseller.features.order.new_order.detail.DetailOrderNewActivity
import com.example.kelilinkseller.util.dateFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailOrderReadyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOrderReadyBinding

    private val detailOrderReadyViewModel: DetailOrderReadyViewModel by viewModels()

    private lateinit var invoiceId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderReadyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()

        showInvoiceInfo()
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.dorToolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.page_detail_order)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun showInvoiceInfo() {
        invoiceId = detailOrderReadyViewModel.getInvoiceId()

        detailOrderReadyViewModel.getOrderById(invoiceId).observe(this) {
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
                    Log.e(DetailOrderNewActivity.TAG, it.message.toString())
                }
            }
        }
    }

    private fun showLoadingState(state: Boolean) {
        binding.dorLoading.root.isVisible = state
    }

    private fun showInfo(state: Boolean) {
        binding.dorLayoutInfo.root.isVisible = state
    }

    private fun setUpInvoiceView(invoice: Invoice) {
        with(binding) {
            dorLayoutInfo.cdoLayoutUser.cudoTvName.text = invoice.id
            dorLayoutInfo.cdoLayoutUser.cudoTvTime.text = dateFormat.format(invoice.time)
            dorTvTotal.text = "Rp${invoice.total_price}"

            val orderMenuAdapter = OrderMenuAdapter()

            orderMenuAdapter.setItems(invoice.orders)

            dorLayoutInfo.cdoRvMenu.apply {
                layoutManager = LinearLayoutManager(this@DetailOrderReadyActivity)
                adapter = orderMenuAdapter
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}