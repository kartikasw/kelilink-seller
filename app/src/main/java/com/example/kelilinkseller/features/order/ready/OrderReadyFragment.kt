package com.example.kelilinkseller.features.order.ready

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.READY
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.core.ui.OrderAdapter
import com.example.kelilinkseller.databinding.ContentRecyclerViewBinding
import com.example.kelilinkseller.features.order.new_order.OrderNewFragment
import com.example.kelilinkseller.features.order.ready.detail.DetailOrderReadyActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderReadyFragment : Fragment() {

    private var _binding: ContentRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrderReadyViewModel by viewModels()

    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContentRecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showOrder()
    }

    private fun showOrder() {
        viewModel.getAllReadyOrderLiveData().observe(viewLifecycleOwner) { invoiceList ->
            showEmptyState(invoiceList.isNullOrEmpty())

            for(invoice in invoiceList) {
                if(invoice.id != "") {
                    viewModel.getAllOrderMenuLiveData(invoice.id).observe(viewLifecycleOwner) { order ->
                        invoice.orders = order
                        val list = invoiceList.filter {
                            it.status == READY
                        }
                        setUpOrderView(list)
                    }
                }
            }
        }
    }

    private fun setUpOrderView(invoice: List<Invoice>?) {
        orderAdapter = OrderAdapter()

        orderAdapter.apply {
            onDoneClick = {
                viewModel.markOrderAsDone(it.id).observe(viewLifecycleOwner) { resource ->
                    when(resource) {
                        is Resource.Success -> {
                            Toast.makeText(
                                requireContext(),
                                resources.getString(R.string.toast_order_done),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is Resource.Error -> {
                            Log.e(OrderNewFragment.TAG, resource.message.toString())
                        }
                        else -> {}
                    }

                }
            }

            onItemClick = {
                viewModel.setInvoiceId(it.id)
                startActivity(Intent(requireContext(), DetailOrderReadyActivity::class.java))
            }
        }

        orderAdapter.setItems(invoice)

        showEmptyState(invoice.isNullOrEmpty())

        with(binding.crvRvOrder) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }

    private fun showEmptyState(state: Boolean) {
        binding.crvEmpty.root.isVisible = state
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "OrderReadyFragment"
    }

}