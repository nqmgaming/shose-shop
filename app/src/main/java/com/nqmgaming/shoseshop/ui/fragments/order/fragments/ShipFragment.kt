package com.nqmgaming.shoseshop.ui.fragments.order.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.databinding.FragmentShipBinding

private const val ARG_PARAM1 = "param1"


class ShipFragment : Fragment() {

    private var param1: String? = null
    private var _binding: FragmentShipBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShipBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ship.text = param1
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            ShipFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}