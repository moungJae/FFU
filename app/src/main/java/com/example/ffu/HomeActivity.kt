package com.example.ffu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        val profile = findViewById<Button>(R.id.home_profile)
        val checkProfile = findViewById<Button>(R.id.home_checkprofile)

        auth = Firebase.auth

        checkProfile.setOnClickListener{
            auth?.signOut()
            finish()
        }

        profile.setOnClickListener {
            val intent10 = Intent(this, ProfileActivity::class.java)
            startActivity(intent10)
        }
    }
}