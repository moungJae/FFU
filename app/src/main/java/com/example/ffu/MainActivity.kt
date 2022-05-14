package com.example.ffu

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var progressBar : ProgressBar
    private lateinit var handler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        auth = Firebase.auth
        progressBar = findViewById<ProgressBar>(R.id.main_progressBar)
        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                progressBar?.visibility = View.INVISIBLE
                moveHomePage(auth?.currentUser)
            }
        }
        moveHomePage(auth?.currentUser)
        signUp()
        loginStart()
    }

    private fun signUp() {
        val signUpButton = findViewById<Button>(R.id.main_signUpButton)

        signUpButton.setOnClickListener {
            startActivity(Intent(this, JoinActivity::class.java))
        }
    }

    private fun loginStart() {
        val startButton = findViewById<Button>(R.id.main_startButton)

        startButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.main_editEmail).text.toString()
            val password = findViewById<EditText>(R.id.main_editPassword).text.toString()

            if (email.length > 0 && password.length > 0) {
                auth?.signInWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            progressBar?.visibility = View.VISIBLE
                            Thread (Runnable {
                                Thread.sleep(3000)
                                handler?.handleMessage(Message())
                            }).start()
                        } else {
                            Toast.makeText(this, "로그인을 실패하셨습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun moveHomePage(user: FirebaseUser?){
        if (user != null) {
            startActivity(Intent(this, BackgroundActivity::class.java))
        }
    }
}