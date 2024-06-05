package com.nqmgaming.shoseshop.ui.activities.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.databinding.ActivityAuthBinding
import com.nqmgaming.shoseshop.ui.activities.check_account.CheckAccountActivity

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.joinUsBtn.setOnClickListener {
            navigateToCheckAccountActivity()
        }

        binding.signInBtn.setOnClickListener {
            navigateToCheckAccountActivity()
        }
    }

    private fun navigateToCheckAccountActivity() {
       Intent(this, CheckAccountActivity::class.java).also {
           startActivity(it)
       }
    }
}