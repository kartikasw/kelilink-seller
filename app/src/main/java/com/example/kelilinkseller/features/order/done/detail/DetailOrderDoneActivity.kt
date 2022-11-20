package com.example.kelilinkseller.features.order.done.detail

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.core.ui.OrderMenuAdapter
import com.example.kelilinkseller.databinding.ActivityDetailOrderDoneBinding
import com.example.kelilinkseller.util.dateFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailOrderDoneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOrderDoneBinding

    private lateinit var invoiceId: String

    private lateinit var phoneNumber: String

    private val detailOrderDoneViewModel: DetailOrderDoneViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderDoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()

        showInvoiceInfo()

        setOnClickListener()
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.dodToolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.page_detail_order)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun showInvoiceInfo() {
        invoiceId = detailOrderDoneViewModel.getInvoiceId()

        detailOrderDoneViewModel.getOrderById(invoiceId).observe(this) {
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
                    Log.e(TAG, it.message.toString())
                }
            }
        }
    }

    private fun showLoadingState(state: Boolean) {
        binding.dodLoading.root.isVisible = state
    }

    private fun showInfo(state: Boolean) {
        binding.dodLayoutInfo.root.isVisible = state
    }

    private fun setUpInvoiceView(invoice: Invoice) {
        with(binding) {
            dodLayoutInfo.cdoLayoutUser.cudoTvName.text = invoice.id
            dodLayoutInfo.cdoLayoutUser.cudoTvTime.text = dateFormat.format(invoice.time)
            dodTvTotal.text = "Rp${invoice.total_price}"

            val orderMenuAdapter = OrderMenuAdapter()

            orderMenuAdapter.setItems(invoice.orders)

            dodLayoutInfo.cdoRvMenu.apply {
                layoutManager = LinearLayoutManager(this@DetailOrderDoneActivity)
                adapter = orderMenuAdapter
            }
        }
    }

    companion object {
        const val TAG = "DetailOrderDoneActivity"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setOnClickListener() {
        binding.dodLayoutInfo.cdoLayoutUser.ibReport.setOnClickListener {
            val message = resources.getString(R.string.placeholder_whatsapp_message)
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse("http://api.whatsapp.com/send?phone=+6281352247312&text=$message")
            }

            startActivity(sendIntent)
        }

        binding.dodLayoutInfo.cdoLayoutUser.ibCall.setOnClickListener {
            val uri = "tel:${Uri.encode(phoneNumber)}"
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse(uri))
            startActivity(intent)
        }
    }

}