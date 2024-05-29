package com.nqmgaming.shoseshop.adapter.viewpagger2

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nqmgaming.shoseshop.ui.fragments.order.fragments.CancelFragment
import com.nqmgaming.shoseshop.ui.fragments.order.fragments.ProgressFragment
import com.nqmgaming.shoseshop.ui.fragments.order.fragments.ShipFragment

class OrderAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ShipFragment.newInstance("Shipped")
            }

            1 -> {
                ProgressFragment.newInstance("On Process")
            }
            2 -> {
                CancelFragment.newInstance("Cancel")
            }
            else -> {
                CancelFragment.newInstance("Cancel")
            }
        }
    }

}