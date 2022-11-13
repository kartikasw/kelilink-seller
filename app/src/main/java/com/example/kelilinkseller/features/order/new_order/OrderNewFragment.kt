package com.example.kelilinkseller.features.order.new_order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.databinding.ContentRecyclerViewBinding

class OrderNewFragment : Fragment() {

    private var _binding: ContentRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private val orderViewModel: OrderNewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContentRecyclerViewBinding.inflate(inflater, container, false)
        return binding.root

        showOrder()
    }

    private fun showOrder() {
        orderViewModel.getAllNewOrder().observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    showLoadingState(false)
                }
                is Resource.Loading -> {
                    showLoadingState(true)
                }
                is Resource.Error -> {
                    showLoadingState(false)
                }
            }
        }
    }

    private fun showLoadingState(state: Boolean) {
        binding.crvLoading.root.isVisible = state
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}