package com.example.ffu.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.ffu.BackgroundActivity
import com.example.ffu.MainActivity
import com.example.ffu.R
import com.google.firebase.auth.FirebaseAuth

class SettingActivity :AppCompatActivity(){
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initSignOutButton()
    }

    private fun initSignOutButton(){
        val signOutButton = findViewById<Button>(R.id.activity_setting_signOutButton)
        signOutButton.setOnClickListener {
            auth.signOut()
            ActivityCompat.finishAffinity(this)
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}