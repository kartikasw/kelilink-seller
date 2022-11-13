package com.example.kelilinkseller.features.order.new_order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.core.ui.OrderAdapter
import com.example.kelilinkseller.databinding.ContentRecyclerViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderNewFragment : Fragment() {

    private var _binding: ContentRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private val orderViewModel: OrderNewViewModel by viewModels()

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
        orderViewModel.getAllNewOrder().observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    Log.d(TAG, it.data.toString())
                    showLoadingState(false)

                    if(it.data != null) {
                        setUpOrderView(it.data)
                    }
                }
                is Resource.Loading -> {
                    showLoadingState(true)
                }
                is Resource.Error -> {
                    showLoadingState(false)
                    Log.e(TAG, it.message.toString())
                }
            }
        }
    }

    private fun setUpOrderView(invoice: List<Invoice>?) {
        orderAdapter = OrderAdapter()
        orderAdapter.setItems(invoice)

        with(binding.crvRvOrder) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }

    private fun showLoadingState(state: Boolean) {
        binding.crvLoading.root.isVisible = state
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "OrderNewFragment"
    }

}