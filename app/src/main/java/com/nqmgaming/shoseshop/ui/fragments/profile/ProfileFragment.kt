package com.nqmgaming.shoseshop.ui.fragments.profile

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.gun0912.tedpermission.coroutine.TedPermission
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.data.model.main.user.User
import com.nqmgaming.shoseshop.databinding.FragmentProfileBinding
import com.nqmgaming.shoseshop.ui.activities.auth.AuthActivity
import com.nqmgaming.shoseshop.ui.activities.settings.SettingActivity
import com.nqmgaming.shoseshop.util.RealPathUtil
import com.nqmgaming.shoseshop.util.SharedPrefUtils
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel>()

    private var imageUri: Uri? = null
    private lateinit var userId: String
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = SharedPrefUtils.getString(requireContext(), "id", "") ?: ""
        token = ("Bearer " + SharedPrefUtils.getString(requireContext(), "accessToken", ""))
        val email = SharedPrefUtils.getString(requireContext(), "email", "") ?: ""
        val firstName = SharedPrefUtils.getString(requireContext(), "firstName", "") ?: ""
        val lastName = SharedPrefUtils.getString(requireContext(), "lastName", "") ?: ""
        val avatar = SharedPrefUtils.getString(requireContext(), "avatar") ?: ""

        binding.profileNameTv.text = "$firstName $lastName"
        Glide.with(this).load(avatar).placeholder(R.drawable.ic_user_profile)
            .into(binding.profileImageIv)

        binding.profileImageIv.setOnClickListener {
            lifecycleScope.launch {
                checkPermissionAndOpenCameraOrGallery()
            }
        }


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

        binding.ordersCv.setOnClickListener{
            findNavController().navigate(R.id.orderFragment)
        }

        binding.addressesCv.setOnClickListener{

        }

        binding.settingsCv.setOnClickListener{
            val intent = Intent(requireContext(), SettingActivity::class.java)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private suspend fun checkPermissionAndOpenCameraOrGallery() {
        val permissionsResult = TedPermission.create()
            .setPermissions(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_MEDIA_IMAGES
            )
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .check()

        if (permissionsResult.isGranted) {
            withContext(Dispatchers.Main) {
                openCameraOrGallery()
            }
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }

    }

    private fun openCameraOrGallery() {
        ImagePicker.with(this)
            .crop()
            .cropSquare()
            .galleryMimeTypes(arrayOf("image/png", "image/jpeg"))
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                getResult.launch(intent)

            }
    }

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(requireContext(), "Image selected", Toast.LENGTH_SHORT).show()
                val uri = result.data?.data
                if (uri != null) {
                    imageUri = uri
                    Log.d("RegisterActivity", "Image uri: $uri")
                    Glide.with(this).load(uri).into(binding.profileImageIv)
                    uploadImage()

                } else {
                    Toast.makeText(requireContext(), "Image not found", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun uploadImage() {
        if (imageUri != null) {
            val realPath = RealPathUtil.getRealPath(requireContext(), imageUri!!)
            val avatarFile = realPath?.let { File(it) }

            if (avatarFile != null) {
                val requestUploadAvatar = MultipartBody.Part.createFormData(
                    "avatar",
                    avatarFile.name,
                    avatarFile.asRequestBody("image/*".toMediaTypeOrNull())
                )

                viewModel.updateUserImageProfile(
                    token,
                    userId,
                    requestUploadAvatar
                ) { user: User? ->
                    if (user != null) {
                        Log.d("ProfileFragment", "User: $user")
                        user.avatar?.let {
                            SharedPrefUtils.saveString(
                                requireContext(), "avatar",
                                it
                            )
                        }

                        Toast.makeText(
                            requireContext(),
                            "Image uploaded successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        Toast.makeText(
                            requireContext(),
                            "Image upload failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}