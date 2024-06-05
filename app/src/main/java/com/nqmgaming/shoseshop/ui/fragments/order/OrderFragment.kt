package com.nqmgaming.shoseshop.ui.fragments.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.nqmgaming.shoseshop.adapter.viewpagger2.OrderAdapter
import com.nqmgaming.shoseshop.databinding.FragmentOrderBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderFragment : Fragment() {
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderAdapter = OrderAdapter(this)
        binding.viewpager.adapter = orderAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Pending"
                }

                1 -> {
                    tab.text = "Progress"
                }

                2 -> {
                    tab.text = "Shipping"
                }

                3 -> {
                    tab.text = "Delivered"
                }

                4 -> {
                    tab.text = "Cancelled"
                }

            }
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}