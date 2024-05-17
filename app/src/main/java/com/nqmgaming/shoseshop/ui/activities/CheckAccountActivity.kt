package com.nqmgaming.shoseshop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.databinding.ActivityCheckAccountBinding
import com.nqmgaming.shoseshop.ui.activities.splash.SplashViewModel
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                checkEmailExits(email) { isExits ->
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

    private fun checkEmailExits(email: String, callback: (Boolean) -> Unit) {
        lifecycleScope.launch {
            Log.d("SplashActivity", "Checking server connection")
            val isServerConnected = withContext(Dispatchers.IO) {
                try {
                    viewModel.checkUserExist(email)
                } catch (e: Exception) {
                    Log.e("SplashActivity", "Error checking server connection: ${e.message}")
                    false
                }
            }
            Log.d("SplashActivity", "Server connected: $isServerConnected")
            callback(isServerConnected)
        }
    }
}