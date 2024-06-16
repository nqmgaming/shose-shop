package com.nqmgaming.shoseshop.ui.activities.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.databinding.ActivitySettingBinding
import com.nqmgaming.shoseshop.ui.activities.term_of_user.TermOfUserActivity

class SettingActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.cvTermOfUser.setOnClickListener{
            val intent = Intent(this, TermOfUserActivity::class.java)
            startActivity(intent)
        }
    }
}