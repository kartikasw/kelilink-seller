package com.example.kelilinkseller.features.order.done

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.DONE
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.core.ui.OrderAdapter
import com.example.kelilinkseller.databinding.ContentRecyclerViewBinding
import com.example.kelilinkseller.features.order.done.detail.DetailOrderDoneActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDoneFragment : Fragment() {

    private var _binding: ContentRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private val orderViewModel: OrderDoneViewModel by viewModels()

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
        orderViewModel.getAllDoneOrderLiveData().observe(viewLifecycleOwner) { invoiceList ->
            showEmptyState(invoiceList.isNullOrEmpty())

            for(invoice in invoiceList) {
               if(invoice.id != "") {
                   orderViewModel.getAllOrderMenuLiveData(invoice.id).observe(viewLifecycleOwner) { order ->
                       invoice.orders = order
                       val list = invoiceList.filter {
                           it.status == DONE
                       }
                       setUpOrderView(list)
                   }
               }
            }
        }
    }

    private fun setUpOrderView(invoice: List<Invoice>?) {
        orderAdapter = OrderAdapter()

        orderAdapter.onItemClick = {
            orderViewModel.setInvoiceId(it.id)
            startActivity(Intent(requireContext(), DetailOrderDoneActivity::class.java))
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



}