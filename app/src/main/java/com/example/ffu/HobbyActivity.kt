package com.example.ffu

import android.app.Person
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HobbyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.hobby)

        val exitButton = findViewById<Button>(R.id.hobby_exitButton)

        exitButton.setOnClickListener {
            val intent1 = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent1)
            finish()
        }




    }
}