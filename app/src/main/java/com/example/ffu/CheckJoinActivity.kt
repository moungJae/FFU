package com.example.ffu

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class CheckJoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading)

        auth = Firebase.auth
        if (auth.currentUser != null) { // 로그인 상태
            UserInformation()
            processLogin()
        } else { // 로그아웃 상태 => 핸드폰 인증 화면으로 즉시 이동
            processLogout()
        }
    }

    private fun processLogin() {
        val loadButton = findViewById<Button>(R.id.loading_button)
        val progressBar = findViewById<ProgressBar>(R.id.loading_progressBar)

        loadButton.text = "입장"
        loadButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            Thread(Runnable {
                Thread.sleep(3000)
                Handler(Looper.getMainLooper()).post {
                    progressBar.visibility = View.INVISIBLE
                    if (UserInformation.JOIN) {
                        val intent = Intent(this@CheckJoinActivity, BackgroundActivity::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@CheckJoinActivity, JoinActivity::class.java)
                        startActivity(intent)
                    }
                }
            }).start()
        }
    }

    private fun processLogout() {
        val loadButton = findViewById<Button>(R.id.loading_button)
        val progressBar = findViewById<ProgressBar>(R.id.loading_progressBar)

        loadButton.text = "핸드폰 인증"

        loadButton.setOnClickListener {
            val intent = Intent(this@CheckJoinActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}