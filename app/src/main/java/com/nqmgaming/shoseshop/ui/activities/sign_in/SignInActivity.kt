package com.nqmgaming.shoseshop.ui.activities.sign_in

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
import com.nqmgaming.shoseshop.databinding.ActivitySignInBinding
import com.nqmgaming.shoseshop.ui.activities.MainActivity
import com.nqmgaming.shoseshop.util.SharedPrefUtils
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val viewModel: SignInViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignInBinding.inflate(layoutInflater)
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

        binding.signInBtn.setOnClickListener {
            val password = binding.passwordEt.text.toString().trim()
            if (password.isNotEmpty()) {
                viewModel.signInUser(email!!, password) { response ->
                    if (response != null) {
                        // navigate to main activity
                        SharedPrefUtils.saveString(this, "id", response.data.id)
                        SharedPrefUtils.saveString(this, "email", response.data.email)
                        SharedPrefUtils.saveString(this, "firstName", response.data.firstName)
                        SharedPrefUtils.saveString(this, "lastName", response.data.lastName)
                        SharedPrefUtils.saveString(this, "avatar", response.data.avatar)
                        SharedPrefUtils.saveString(this, "birthDate", response.data.birthDate)
                        SharedPrefUtils.saveString(this, "address", response.data.address)
                        SharedPrefUtils.saveString(this, "phoneNumber", response.data.phoneNumber)
                        SharedPrefUtils.saveString(this, "accessToken", response.accessToken)
                        SharedPrefUtils.saveBoolean(this, "isLogin", true)

                        PopupDialog.getInstance(this)
                            .setStyle(Styles.SUCCESS)
                            .setHeading("Sign in success!")
                            .setDescription("Welcome back to shoesshop")
                            .setCancelable(false)
                            .setDismissButtonText("OK")
                            .showDialog(object : OnDialogButtonClickListener() {
                                override fun onDismissClicked(dialog: Dialog) {
                                    super.onDismissClicked(dialog)
                                    dialog.dismiss()
                                    Intent(this@SignInActivity, MainActivity::class.java).also {
                                        startActivity(it)
                                        finish()
                                    }
                                }
                            })
                    } else {
                        // show error message
                        binding.passwordEt.error = "Invalid password"
                        Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}