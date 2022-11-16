package com.example.kelilinkseller.features.order.ready

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
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.core.ui.OrderAdapter
import com.example.kelilinkseller.databinding.ContentRecyclerViewBinding
import com.example.kelilinkseller.features.order.new_order.OrderNewFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderReadyFragment : Fragment() {

    private var _binding: ContentRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private val orderViewModel: OrderReadyViewModel by viewModels()

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
        orderViewModel.getAllReadyOrder().observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    Log.d(OrderNewFragment.TAG, it.data.toString())
                    showLoadingState(false)

                    if(it.data != null) {
                        setUpOrderView(it.data)
                    } else {
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
                    Log.e(OrderNewFragment.TAG, it.message.toString())
                }
            }
        }
    }

    private fun setUpOrderView(invoice: List<Invoice>?) {
        orderAdapter = OrderAdapter()

        orderAdapter.apply {
            onDoneClick = {
                updateOrderStatus(it.id)
            }
        }

        orderAdapter.setItems(invoice)

        with(binding.crvRvOrder) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }

    private fun updateOrderStatus(invoiceId: String) {
        orderViewModel
            .updateOrderStatus(invoiceId, "done")
            .observe(viewLifecycleOwner) { resource ->
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

    private fun showLoadingState(state: Boolean) {
        binding.crvLoading.root.isVisible = state
    }

    private fun showEmptyState(state: Boolean) {
        binding.crvEmpty.apply {
            seTvTitle.text = resources.getString(R.string.title_order_empty)
            seTvContent.isVisible = false
            root.isVisible = state
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}