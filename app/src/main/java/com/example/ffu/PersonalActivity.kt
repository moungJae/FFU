package com.example.ffu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PersonalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.personality)

        val exitButton = findViewById<Button>(R.id.personality_exitButton)

        exitButton.setOnClickListener {
            val intent1 = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent1)
            finish()
        }


    }
}