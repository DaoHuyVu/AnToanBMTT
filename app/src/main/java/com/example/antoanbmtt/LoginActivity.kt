package com.example.antoanbmtt

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.antoanbmtt.databinding.ActivityLoginBinding
import com.example.antoanbmtt.ui.login.LoginFragment.UserScreenEntryPoint

class LoginActivity : AppCompatActivity(), UserScreenEntryPoint {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    override fun toUserScreen() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}
