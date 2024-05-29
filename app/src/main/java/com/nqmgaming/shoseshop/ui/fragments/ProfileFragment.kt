package com.nqmgaming.shoseshop.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.databinding.FragmentProfileBinding
import com.nqmgaming.shoseshop.ui.activities.auth.AuthActivity
import com.nqmgaming.shoseshop.util.SharedPrefUtils
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutBtn.setOnClickListener {
            PopupDialog.getInstance(requireContext())
                .setStyle(Styles.STANDARD)
                .setHeading("Logout")
                .setDescription("Are you sure you want to logout?")
                .setPositiveButtonText("Yes")
                .setPositiveButtonBackground(com.saadahmedsoft.popupdialog.R.color.colorBlack)
                .setNegativeButtonBackground(com.saadahmedsoft.popupdialog.R.color.colorWhite)
                .setNegativeButtonText("No")
                .setCancelable(true)
                .showDialog(object : OnDialogButtonClickListener() {
                    override fun onNegativeClicked(dialog: Dialog?) {
                        super.onNegativeClicked(dialog)
                        dialog?.dismiss()
                    }

                    override fun onPositiveClicked(dialog: Dialog?) {
                        super.onPositiveClicked(dialog)
                        dialog?.dismiss()
                        SharedPrefUtils.clear(requireContext())
                        Intent(requireContext(), AuthActivity::class.java).apply {
                            startActivity(this)
                            requireActivity().finish()
                        }
                    }
                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}