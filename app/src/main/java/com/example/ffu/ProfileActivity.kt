package com.example.ffu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        val recommend = findViewById<Button>(R.id.home_recommend)
        val editprofile_button = findViewById<Button>(R.id.profile_profileedit_button)


        recommend.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        editprofile_button.setOnClickListener {
            val intent = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent)
        }

    }

}