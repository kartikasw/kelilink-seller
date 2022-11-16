package com.example.kelilinkseller.features.order.new_order

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
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Fcm
import com.example.kelilinkseller.core.domain.model.FcmData
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
                    Log.e(TAG, it.message.toString())
                }
            }
        }
    }

    private fun setUpOrderView(invoice: List<Invoice>?) {
        orderAdapter = OrderAdapter()

        orderAdapter.apply {
            onAcceptClick = {
                updateOrderStatus(it.id, "cooking")
            }

            onDeclineClick = {
                updateOrderStatus(it.id, "declined")
            }

            onReadyClick = {
                orderViewModel.updateOrderStatus(it.id, it.status).observe(viewLifecycleOwner) { resource ->
                    when(resource) {
                        is Resource.Success -> {
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
                                        Toast.makeText(
                                            requireContext(),
                                            resources.getString(R.string.toast_order_ready),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    is Resource.Error -> {
                                        Log.e(TAG, fcmResource.message.toString())
                                    }
                                    else -> {}
                                }
                            }
                        }
                        is Resource.Error -> {
                            Log.e(TAG, resource.message.toString())
                        }
                        else -> {}
                    }
                }
            }

            onDoneClick = {
                updateOrderStatus(it.id, "done")
            }
        }

        orderAdapter.setItems(invoice)

        with(binding.crvRvOrder) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }

    private fun updateOrderStatus(invoiceId: String, status: String) {
        orderViewModel
            .updateOrderStatus(invoiceId, status)
            .observe(viewLifecycleOwner) { resource ->
                when(resource) {
                    is Resource.Success -> {
                        val toastText = when(status) {
                            COOKING ->  resources.getString(R.string.toast_order_accepted)
                            DECLINED ->  resources.getString(R.string.toast_order_declined)
                            else ->  resources.getString(R.string.toast_order_done)
                        }

                        Toast.makeText(requireContext(), toastText, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error -> {
                        Log.e(TAG, resource.message.toString())
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

    companion object {
        const val TAG = "OrderNewFragment"
    }

}