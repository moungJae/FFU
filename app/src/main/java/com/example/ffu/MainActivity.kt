package com.example.test1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val joinButton = findViewById<Button>(R.id.main_joinButton)
        val startButton = findViewById<Button>(R.id.main_startButton)

        joinButton.setOnClickListener {
            val intent = Intent(this, CheckPhoneNumActivity::class.java)
            startActivity(intent)
        }

        startButton.setOnClickListener {
            val intent2 = Intent(this, HomeActivity::class.java)
            startActivity(intent2)
        }


    }
}