package com.example.ffu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    // firebase
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val signUpButton = findViewById<Button>(R.id.main_signUpButton)
        val startButton = findViewById<Button>(R.id.main_startButton)

        auth = Firebase.auth

        signUpButton.setOnClickListener {
            val intent = Intent(this, CheckPhoneNumActivity::class.java)
            startActivity(intent)
        }

        startButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.main_editEmail).text.toString()
            val password = findViewById<EditText>(R.id.main_editPassword).text.toString()
            val intent = Intent(this, HomeActivity::class.java)

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "로그인을 실패하셨습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}