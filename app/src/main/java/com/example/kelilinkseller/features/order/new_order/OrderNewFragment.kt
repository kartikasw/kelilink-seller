package com.example.kelilinkseller.features.order.new_order

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
import com.example.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.COOKING
import com.example.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.DECLINED
import com.example.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.READY
import com.example.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.WAITING
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Fcm
import com.example.kelilinkseller.core.domain.model.FcmData
import com.example.kelilinkseller.core.domain.model.Invoice
import com.example.kelilinkseller.core.ui.OrderAdapter
import com.example.kelilinkseller.databinding.ContentRecyclerViewBinding
import com.example.kelilinkseller.features.order.new_order.detail.DetailOrderNewActivity
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
//        orderViewModel.orders.observe(viewLifecycleOwner) { invoiceList ->
//            Log.d(TAG, invoiceList.toString())
//            setUpOrderView(invoiceList)
//        }

        orderViewModel.getAllNewOrderLiveData().observe(viewLifecycleOwner) { invoiceList ->
            for(invoice in invoiceList) {
                if(invoice.id != "") {
                    orderViewModel.getAllOrderMenuLiveData(invoice.id).observe(viewLifecycleOwner) { order ->
                        invoice.orders = order
                        val list = invoiceList.filter {
                            it.status == COOKING || it.status == WAITING
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
            onItemClick = {
                orderViewModel.setInvoiceId(it.id)
                startActivity(Intent(requireContext(), DetailOrderNewActivity::class.java))
            }

            onAcceptClick = {
                orderViewModel.acceptOrder(it.id).observe(viewLifecycleOwner) { resource ->
                    updateResponse(resource, COOKING)
                }
            }

            onDeclineClick = {
                orderViewModel.declineOrder(it.id).observe(viewLifecycleOwner) { resource ->
                    updateResponse(resource, DECLINED)
                }
            }

            onReadyClick = {
                orderViewModel.sendFcm(
                    Fcm(
                        to = it.user_fcm_token,
                        FcmData(
                            invoice_id = it.id,
                            store_id = it.store_id,
                            store_name = it.store_name
                        )
                    )
                ).observe(viewLifecycleOwner) { fcmResource ->
                    when(fcmResource) {
                        is Resource.Success -> {
                            orderViewModel.markOrderAsReady(it.id).observe(viewLifecycleOwner) { resource ->
                                updateResponse(resource, READY)
                            }
                        }
                        is Resource.Error -> {
                            Log.e(TAG, fcmResource.message.toString())
                        }
                        else -> {}
                    }
                }
            }

        }

        orderAdapter.setItems(invoice)

        showEmptyState(invoice.isNullOrEmpty())

        with(binding.crvRvOrder) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }

    private fun updateResponse(resource: Resource<Unit>, status: String) {
        when(resource) {
            is Resource.Success -> {
                val toastText = when (status) {
                    COOKING -> {
                        resources.getString(R.string.toast_order_accepted)
                    }
                    DECLINED -> {
                        resources.getString(R.string.toast_order_declined)
                    }
                    else -> {
                        resources.getString(R.string.toast_order_done)
                    }
                }

                Toast.makeText(requireContext(), toastText, Toast.LENGTH_SHORT).show()
            }
            is Resource.Error -> {
                Log.e(TAG, resource.message.toString())
                Toast.makeText(requireContext(), resource.message.toString(), Toast.LENGTH_SHORT).show()
            }
            else -> {}
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
        const val TAG = "OrderNewFragment"
    }

}