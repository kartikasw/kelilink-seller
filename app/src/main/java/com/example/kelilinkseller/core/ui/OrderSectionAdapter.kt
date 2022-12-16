package com.example.kelilinkseller.core.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.kelilinkseller.features.order.done.OrderDoneFragment
import com.example.kelilinkseller.features.order.new_order.OrderNewFragment
import com.example.kelilinkseller.features.order.ready.OrderReadyFragment

class OrderSectionAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = OrderNewFragment()
            1 -> fragment = OrderReadyFragment()
            2 -> fragment = OrderDoneFragment()
        }
        return fragment as Fragment
    }
}