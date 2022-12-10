package com.example.kelilinkseller.features.order.ready.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.core.ui.OrderMenuAdapter
import com.example.kelilinkseller.databinding.ActivityDetailOrderReadyBinding
import com.example.kelilinkseller.features.order.new_order.OrderNewFragment
import com.example.kelilinkseller.features.order.new_order.detail.DetailOrderNewActivity
import com.example.kelilinkseller.util.dateFormat
import com.example.kelilinkseller.util.withCurrencyFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailOrderReadyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOrderReadyBinding

    private val detailOrderReadyViewModel: DetailOrderReadyViewModel by viewModels()

    private lateinit var invoiceId: String

    private lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderReadyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()

        showInvoiceInfo()

        setOnClickListener()
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
                    phoneNumber = it.data!!.user_phone_number
                    showLoadingState(false)
                    setUpInvoiceView(it.data)
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
            dorTvTotal.text = invoice.total_price.withCurrencyFormat()

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

    private fun setOnClickListener() {
        binding.dorLayoutInfo.cdoLayoutUser.ibReport.setOnClickListener {
            val message = resources.getString(R.string.placeholder_whatsapp_message)
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse("http://api.whatsapp.com/send?phone=+6281352247312&text=$message")
            }

            startActivity(sendIntent)
        }

        binding.dorLayoutInfo.cdoLayoutUser.ibCall.setOnClickListener {
            val uri = "tel:${Uri.encode(phoneNumber)}"
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse(uri))
            startActivity(intent)
        }

        binding.dorBtnReady.setOnClickListener {
           detailOrderReadyViewModel.markOrderAsDone(invoiceId).observe(this) {
                when(it) {
                    is Resource.Success -> {
                        onBackPressed()
                    }
                    is Resource.Error -> {
                        Log.e(OrderNewFragment.TAG, it.message.toString())
                    }
                    else -> {}
                }
            }
        }
    }

}