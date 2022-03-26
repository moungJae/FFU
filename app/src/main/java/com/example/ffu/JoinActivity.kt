package com.example.test1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join)

        val backButton = findViewById<Button>(R.id.join_backButton)
        val joinCompleteButton = findViewById<Button>(R.id.join_joinCompleteButton)

        backButton.setOnClickListener {
            val intent = Intent(this, CheckPhoneNumActivity::class.java)
            startActivity(intent)
        }

        joinCompleteButton.setOnClickListener {
            val intent2 = Intent(this, HomeActivity::class.java)
            startActivity(intent2)
        }
    }
}

