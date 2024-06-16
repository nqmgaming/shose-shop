package com.nqmgaming.shoseshop.ui.activities.Settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.nqmgaming.shoseshop.R
import com.nqmgaming.shoseshop.databinding.ActivitySettingBinding
import com.nqmgaming.shoseshop.ui.activities.TermOfUser.TermOfUser
import com.nqmgaming.shoseshop.ui.fragments.profile.ProfileViewModel

class SettingActivity : AppCompatActivity() {
    private var binding = ActivitySettingBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.cvTermOfUser.setOnClickListener{
            val intent = Intent(this, TermOfUser::class.java)
            startActivity(intent)
        }
    }
}