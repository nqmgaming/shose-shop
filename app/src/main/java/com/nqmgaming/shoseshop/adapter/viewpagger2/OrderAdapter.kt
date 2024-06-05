package com.nqmgaming.shoseshop.adapter.viewpagger2

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nqmgaming.shoseshop.ui.fragments.order.fragments.CancelFragment
import com.nqmgaming.shoseshop.ui.fragments.order.fragments.DeliveredFragment
import com.nqmgaming.shoseshop.ui.fragments.order.fragments.PendingFragment
import com.nqmgaming.shoseshop.ui.fragments.order.fragments.ProgressFragment
import com.nqmgaming.shoseshop.ui.fragments.order.fragments.ShipFragment

class OrderAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                PendingFragment.newInstance("Pending")
            }

            1 -> {
                ProgressFragment.newInstance("Processing")
            }

            2 -> {
                ShipFragment.newInstance("Shipping")
            }

            3 -> {
                DeliveredFragment.newInstance("Delivered")
            }

            else -> {
                CancelFragment.newInstance("Cancelled")
            }
        }
    }

}