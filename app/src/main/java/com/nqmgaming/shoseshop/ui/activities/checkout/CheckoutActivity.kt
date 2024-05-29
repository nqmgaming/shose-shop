package com.nqmgaming.shoseshop.ui.activities.checkout

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.maxkeppeler.sheets.input.InputSheet
import com.maxkeppeler.sheets.input.ValidationResult
import com.maxkeppeler.sheets.input.type.InputEditText
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.databinding.ActivityCheckoutBinding
import com.nqmgaming.shoseshop.ui.activities.order.OrderActivity
import com.nqmgaming.shoseshop.util.SharedPrefUtils
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import dagger.hilt.android.AndroidEntryPoint

class CheckoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var email: String
    private lateinit var phoneNumber: String
    private lateinit var address: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        email = SharedPrefUtils.getString(this, "email") ?: ""
        phoneNumber = SharedPrefUtils.getString(this, "phoneNumber") ?: ""
        address = SharedPrefUtils.getString(this, "address") ?: ""
        val total = intent.getStringExtra("total") ?: ""
        val shippingFee = total.toDouble() * 0.1
        val token = intent.getStringExtra("token")
        binding.tvShippingFeeValue.text = "$ $shippingFee"
        binding.tvTotalQuantityValue.text = "$ $total"
        binding.tvTotalValue.text = "$ ${total.toDouble() + shippingFee}"
        binding.apply {
            emailTv.text = email
            phoneTv.text = phoneNumber
            addressTv.text = address
            editEmailIv.setOnClickListener {
                var isEmailValid = false
                val oldEmail: String = email
                InputSheet().show(this@CheckoutActivity) {
                    title("Edit Email")
                    with(InputEditText {
                        required()
                        label("Email")
                        hint("Enter your email")
                        defaultValue(email)
                        inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                        maxLines(1)
                        startIconDrawable(R.drawable.ic_email)
                        validationListener { value ->
                            isEmailValid = value.toString().validEmail()
                            ValidationResult(isEmailValid, "Invalid email")
                        }
                        changeListener { value ->
                            email = value.toString()
                        }
                        resultListener {
                            emailTv.text = email
                        }
                        onPositive {
                            if (isEmailValid) {
                                SharedPrefUtils.saveString(this@CheckoutActivity, "email", email)
                            } else {
                                email = oldEmail
                                Toast.makeText(
                                    this@CheckoutActivity,
                                    "Invalid email",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        onNegative {
                            email = oldEmail
                            emailTv.text = email
                        }
                    })
                }
            }

            editAddressIv.setOnClickListener {
                var isAddressValid = false
                val oldAddress: String = address
                InputSheet().show(this@CheckoutActivity) {
                    title("Edit Address")
                    with(InputEditText {
                        required()
                        label("Address")
                        hint("Enter your address")
                        defaultValue(address)
                        maxLines(1)
                        startIconDrawable(R.drawable.ic_location)
                        validationListener { value ->
                            isAddressValid = value.toString().isNotEmpty()
                            ValidationResult(isAddressValid, "Address cannot be empty")
                        }
                        changeListener { value ->
                            address = value.toString()
                        }
                        resultListener {
                            addressTv.text = address
                        }
                        onPositive {
                            SharedPrefUtils.saveString(this@CheckoutActivity, "address", address)
                        }
                        onNegative {
                            address = oldAddress
                            addressTv.text = address
                        }
                    })
                }
            }

            editPhoneIv.setOnClickListener {
                var isPhoneValid = false
                val oldPhone: String = phoneNumber
                InputSheet().show(this@CheckoutActivity) {
                    title("Edit Phone Number")
                    with(InputEditText {
                        required()
                        label("Phone Number")
                        hint("Enter your phone number")
                        defaultValue(phoneNumber)
                        maxLines(1)
                        startIconDrawable(R.drawable.ic_telephone)
                        validationListener { value ->
                            isPhoneValid = value.toString().isNotEmpty()
                            ValidationResult(isPhoneValid, "Phone number cannot be empty")
                        }
                        changeListener { value ->
                            phoneNumber = value.toString()
                        }
                        resultListener {
                            phoneTv.text = phoneNumber
                        }
                        onPositive {
                            SharedPrefUtils.saveString(
                                this@CheckoutActivity,
                                "phoneNumber",
                                phoneNumber
                            )
                        }
                        onNegative {
                            phoneNumber = oldPhone
                            phoneTv.text = phoneNumber
                        }
                    })
                }
            }
            btnCheckout.setOnClickListener {
                val totalIntent = total.toDouble() + shippingFee
                Intent(this@CheckoutActivity, OrderActivity::class.java).also {
                    it.putExtra("total", totalIntent.toString())
                    it.putExtra("email", email)
                    it.putExtra("phoneNumber", phoneNumber)
                    it.putExtra("address", address)
                    it.putExtra("token", token)
                    startActivity(it)
                }
            }
        }
    }
}