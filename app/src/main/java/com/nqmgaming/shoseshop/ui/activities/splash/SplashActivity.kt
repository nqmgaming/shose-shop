package com.nqmgaming.shoseshop.ui.activities.splash

import android.app.Dialog
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
import com.nqmgaming.shoseshop.data.remote.ShoesApi
import com.nqmgaming.shoseshop.databinding.ActivitySplashBinding
import com.nqmgaming.shoseshop.ui.activities.AuthActivity
import com.nqmgaming.shoseshop.ui.activities.MainActivity
import com.nqmgaming.shoseshop.util.SharedPrefUtils
import com.saadahmedsoft.popupdialog.PopupDialog
import com.saadahmedsoft.popupdialog.Styles
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        checkServerConnection("test@gmail.com") { isConnected ->
            if (isConnected) {
                val isLogin = SharedPrefUtils.getBoolean(this, "isLogin", false)
                if (isLogin) {
                    navigateToMainActivity()
                } else {
                    navigateToAuthActivity()
                }
            } else {
                // show error message
                PopupDialog.getInstance(this)
                    .setStyle(Styles.FAILED)
                    .setHeading("Uh-Oh")
                    .setDescription("Unexpected error occurred. Try again later.")
                    .setCancelable(false)
                    .showDialog(object : OnDialogButtonClickListener() {
                        override fun onDismissClicked(dialog: Dialog) {
                            super.onDismissClicked(dialog)
                            finish()
                        }
                    })
            }
        }
    }

    private fun checkServerConnection(email: String, callback: (Boolean) -> Unit) {
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

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}