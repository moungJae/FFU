package com.example.ffu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class CheckPhoneNumActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkpn)

        goBack()
        startJoin()
    }

    private fun goBack() {
        val backButton = findViewById<Button>(R.id.checkpn_back)

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun startJoin() {
        val nextButton = findViewById<Button>(R.id.checkpn_nextButton)

        nextButton.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            val editTel = findViewById<EditText>(R.id.checkpn_editPhone)
            intent.putExtra("tel", editTel.text.toString())
            startActivity(intent)
            finish()
        }
    }
}