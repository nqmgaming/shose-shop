package com.nqmgaming.shoseshop.ui.activities.check_account

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.databinding.ActivityCheckAccountBinding
import com.nqmgaming.shoseshop.ui.activities.sign_in.SignInActivity
import com.nqmgaming.shoseshop.ui.activities.sign_up.SignUpActivity
import com.nqmgaming.shoseshop.ui.activities.splash.SplashViewModel
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckAccountBinding
    private val viewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCheckAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.continueBtn.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            email.validEmail() {
                binding.emailEt.error = it
                return@validEmail
            }
            if (email.validEmail()) {
                viewModel.checkEmailExits(email) { isExits ->
                    if (isExits) {
                        Intent(this, SignInActivity::class.java).also {
                            it.putExtra("email", email)
                            startActivity(it)
                        }
                    } else {
                        Intent(this, SignUpActivity::class.java).also {
                            it.putExtra("email", email)
                            startActivity(it)
                        }
                    }

                }
            }
        }

    }

}