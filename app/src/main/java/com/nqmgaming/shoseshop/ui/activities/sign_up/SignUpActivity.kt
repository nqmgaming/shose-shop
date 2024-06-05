package com.nqmgaming.shoseshop.ui.activities.sign_up

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.databinding.ActivitySignUpBinding
import com.nqmgaming.shoseshop.ui.activities.MainActivity
import com.nqmgaming.shoseshop.util.SharedPrefUtils
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val email = intent.getStringExtra("email")
        binding.emailTv.text = email

        binding.editTv.setOnClickListener {
            finish()
        }

        binding.dateEt.setOnClickListener {
            val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val date = "$dayOfMonth/${month + 1}/$year"
                binding.dateEt.setText(date)
            }, 2000, 1, 1)
            datePicker.show()
        }

        binding.createAccountBtn.setOnClickListener {
            var error = false
            val firstname = binding.firstnameEt.text.toString().trim()
            val lastname = binding.lastnameEt.text.toString().trim()
            val password = binding.passwordEt.text.toString().trim()
            val address = binding.addressEt.text.toString().trim()
            val phoneNumber = binding.phoneEt.text.toString().trim()
            val birthDateInput = binding.dateEt.text.toString().trim()
            val birthDate = if (birthDateInput.isNotEmpty()) {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(birthDateInput)
            } else {
                null
            }
            val formattedBirthDate = birthDate?.let { it1 ->
                SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(it1)
            }
            firstname.validator().nonEmpty().noNumbers().addErrorCallback {
                binding.firstnameEt.error = it
                binding.firstnameEt.requestFocus()
                error = true
            }.addSuccessCallback {
                binding.firstnameEt.error = null
            }.check()
            lastname.validator().nonEmpty().noNumbers().addErrorCallback {
                binding.lastnameEt.error = it
                binding.lastnameEt.requestFocus()
                error = true
            }.addSuccessCallback {
                binding.lastnameEt.error = null
            }.check()
            password.validator().minLength(6).atleastOneNumber().addErrorCallback {
                binding.passwordEt.error = it
                binding.passwordEt.requestFocus()
                error = true
            }.addSuccessCallback {
                binding.passwordEt.error = null
            }.check()
            address.validator().nonEmpty().minLength(10).addErrorCallback {
                binding.addressEt.error = it
                binding.addressEt.requestFocus()
                error = true
            }.addSuccessCallback {
                binding.addressEt.error = null
            }.check()
            phoneNumber.validator().nonEmpty().validNumber().noSpecialCharacters().onlyNumbers()
                .minLength(10).maxLength(12)
                .addErrorCallback {
                    binding.phoneEt.error = it
                    binding.phoneEt.requestFocus()
                    error = true
                }.addSuccessCallback { binding.phoneEt.error = null }.check()
            birthDateInput.validator().nonEmpty().addErrorCallback {
                binding.dateEt.error = it
                binding.dateEt.requestFocus()
                error = true
            }.addSuccessCallback {
                binding.dateEt.error = null
            }
                .check()
            if (error) {
                return@setOnClickListener
            } else {
                viewModel.signUpUser(
                    email!!,
                    password,
                    firstname,
                    lastname,
                    formattedBirthDate!!,
                    address,
                    phoneNumber
                ) { response ->
                    if (response != null) {
                        // save user data to shared preferences
                        SharedPrefUtils.saveString(this, "id", response.data.id)
                        SharedPrefUtils.saveString(this, "email", response.data.email)
                        SharedPrefUtils.saveString(this, "firstName", response.data.firstName)
                        SharedPrefUtils.saveString(this, "lastName", response.data.lastName)
                        SharedPrefUtils.saveString(this, "birthDate", response.data.birthDate)
                        SharedPrefUtils.saveString(this, "address", response.data.address)
                        SharedPrefUtils.saveString(this, "phoneNumber", response.data.phoneNumber)
                        SharedPrefUtils.saveString(this, "accessToken", response.accessToken)
                        SharedPrefUtils.saveBoolean(this, "isLogin", true)
                        // navigate to main activity
                        PopupDialog.getInstance(this)
                            .setStyle(Styles.SUCCESS)
                            .setHeading("Sign up success!")
                            .setDescription("Welcome to shoesshop")
                            .setDismissButtonText("OK")
                            .setCancelable(false)
                            .showDialog(object : OnDialogButtonClickListener() {
                                override fun onDismissClicked(dialog: Dialog) {
                                    super.onDismissClicked(dialog)
                                    dialog.dismiss()
                                    Intent(this@SignUpActivity, MainActivity::class.java).also {
                                        startActivity(it)
                                        finish()
                                    }
                                }
                            })
                    } else {
                        // show error message
                        Toast.makeText(this, "Error signing up", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
