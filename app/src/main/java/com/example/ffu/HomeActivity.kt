package com.example.ffu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)


        val profile = findViewById<Button>(R.id.home_profile)



        profile.setOnClickListener {
            val intent10 = Intent(this, ProfileActivity::class.java)
            startActivity(intent10)
        }
    }
}