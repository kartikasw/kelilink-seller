package com.kartikasw.kelilinkseller.features.order.new_order

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
import com.kartikasw.kelilinkseller.R
import com.kartikasw.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.COOKING
import com.kartikasw.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.DECLINED
import com.kartikasw.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.READY
import com.kartikasw.kelilinkseller.core.data.helper.Constants.ORDER_STATUS.WAITING
import com.kartikasw.kelilinkseller.core.domain.Resource
import com.kartikasw.kelilinkseller.core.domain.model.Fcm
import com.kartikasw.kelilinkseller.core.domain.model.FcmData
import com.kartikasw.kelilinkseller.core.domain.model.Invoice
import com.kartikasw.kelilinkseller.core.ui.OrderAdapter
import com.kartikasw.kelilinkseller.databinding.ContentRecyclerViewBinding
import com.kartikasw.kelilinkseller.features.order.new_order.detail.DetailOrderNewActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

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
        orderViewModel.getAllNewOrder().observe(viewLifecycleOwner) { invoiceList ->
            showEmptyState(invoiceList.isNullOrEmpty())

            for(invoice in invoiceList) {
                if(invoice.id != "") {
                    orderViewModel.getAllOrderMenu(invoice.id).observe(viewLifecycleOwner) { order ->
                        invoice.orders = order
                        val timeNow = Calendar.getInstance().time.time
                        val list = invoiceList.filter {
                            it.status == COOKING || (it.status == WAITING && it.time_expire.time >= timeNow)
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
                    updateResponse(resource, COOKING, it)
                }
            }

            onDeclineClick = {
                orderViewModel.declineOrder(it.id).observe(viewLifecycleOwner) { resource ->
                    updateResponse(resource, DECLINED, it)
                }
            }

            onReadyClick = {
                orderViewModel.markOrderAsReady(it.id).observe(viewLifecycleOwner) { resource ->
                    updateResponse(resource, READY, it)
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

    private fun updateResponse(resource: Resource<Unit>, status: String, invoice: Invoice) {
        when(resource) {
            is Resource.Success -> {
                val toastText = when (status) {
                    COOKING -> {
                        requireContext().resources.getString(R.string.toast_order_accepted)
                    }
                    DECLINED -> {
                        requireContext().resources.getString(R.string.toast_order_declined)
                    }
                    READY -> {
                        requireContext().resources.getString(R.string.toast_order_ready)
                    }
                    else -> {
                        requireContext().resources.getString(R.string.toast_order_done)
                    }
                }

                if(status == READY) {
                    if(invoice.user_fcm_token != "") {
                        orderViewModel.sendFcm(
                            Fcm(
                                to = invoice.user_fcm_token,
                                FcmData(
                                    invoice_id = invoice.id,
                                    store_id = invoice.store_id,
                                    store_name = invoice.store_name
                                )
                            )
                        ).observe(viewLifecycleOwner) { fcmResource ->
                            when(fcmResource) {
                                is Resource.Success -> {
                                    Log.d(TAG, "FCM sent successfully")
                                }
                                is Resource.Error -> {
                                    Log.e(TAG, fcmResource.message.toString())
                                }
                                else -> {}
                            }
                        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "OrderNewFragment"
    }

}