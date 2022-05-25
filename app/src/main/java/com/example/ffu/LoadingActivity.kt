package com.example.ffu

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoadingActivity : AppCompatActivity() {

    private lateinit var userDB: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading)

        auth = Firebase.auth
        if (auth.currentUser != null) {
            addUserProfile()
            addUserAnimation()
        }
        processLoading()
    }

    private fun addUserProfile() {
        userDB = Firebase.database.reference.child("profile").child(auth.uid.toString())
        userDB.get().addOnSuccessListener {
            if (it.child("join").value.toString().equals("true")) {
                //val intent = Intent(this@CheckJoinActivity, BackgroundActivity::class.java)
                startActivity(intent)
                finish()
            } else {
               //val intent = Intent(this@CheckJoinActivity, JoinActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun addUserAnimation() {

    }

    private fun processLoading() {
        val loadButton = findViewById<Button>(R.id.loading_button)
        val progressBar = findViewById<ProgressBar>(R.id.loading_progressBar)

        if (auth.currentUser != null) {
            loadButton.setText("로그인 중")
            loadButton.setOnClickListener {
                progressBar.visibility = View.VISIBLE
                Thread(Runnable {
                    Thread.sleep(3000)
                    Handler(Looper.getMainLooper()).post {
                        progressBar.visibility = View.INVISIBLE
                        val intent = Intent(this, CheckJoinActivity::class.java)
                        startActivity(intent)
                    }
                }).start()
            }
        } else {
            loadButton.setText("로그아웃 중")
        }
    }
}