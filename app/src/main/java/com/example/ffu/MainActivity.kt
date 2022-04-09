package com.example.ffu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        val signUpButton = findViewById<Button>(R.id.main_signUpButton)
        val startButton = findViewById<Button>(R.id.main_startButton)

        auth = Firebase.auth

        moveHomePage(auth?.currentUser)

        signUpButton.setOnClickListener {
            startActivity(Intent(this, CheckPhoneNumActivity::class.java))
        }

        startButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.main_editEmail).text.toString()
            val password = findViewById<EditText>(R.id.main_editPassword).text.toString()

            if (email.length > 0 && password.length > 0) {
                auth?.signInWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            moveHomePage(auth?.currentUser)
                        } else {
                            Toast.makeText(this, "로그인을 실패하셨습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    fun moveHomePage(user: FirebaseUser?){
        if (user != null) {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}