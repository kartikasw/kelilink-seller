package com.example.kelilinkseller.features.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.example.kelilinkseller.R
import com.example.kelilinkseller.core.ui.OrderSectionAdapter
import com.example.kelilinkseller.databinding.FragmentOrderBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()

        setUpSections()
    }

    private fun setUpToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.oToolbar)
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = resources.getString(R.string.app_name)
        }
    }

    private fun setUpSections() {
        with(binding) {
            val sectionAdapter = OrderSectionAdapter(requireActivity())
            val viewPager = oViewpager
            viewPager.adapter = sectionAdapter
            TabLayoutMediator(oTab, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_order_new,
            R.string.tab_order_ready ,
            R.string.tab_order_done
        )
    }

}