package com.example.ffu.join

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.ffu.BackgroundActivity
import com.example.ffu.R
import com.example.ffu.UserInformation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.ffu.UserInformation.Companion.PROFILE

class WelcomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading)

        setAuth()
        checkUser()
    }

    private fun setAuth() {
        auth = Firebase.auth
        userId = auth.uid.toString()
    }

    private fun checkUser() {
        if (auth.currentUser != null) { // 로그인 상태
            UserInformation()
            processLogin()
        } else { // 로그아웃 상태 => 핸드폰 인증 화면으로 즉시 이동
            processLogout()
        }
    }

    private fun checkJoin() {
        val progressBar = findViewById<ProgressBar>(R.id.loading_progressBar)

        progressBar.visibility = View.INVISIBLE
        if (PROFILE[userId]?.join == true) {
            startActivity(Intent(this, BackgroundActivity::class.java))
        } else {
            startActivity(Intent(this, JoinActivity::class.java))
        }
    }

    private fun processLogin() {
        val loadButton = findViewById<Button>(R.id.loading_button)
        val progressBar = findViewById<ProgressBar>(R.id.loading_progressBar)

        loadButton.text = "입장"
        loadButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            loadButton.isEnabled = false
            Thread(Runnable {
                Thread.sleep(3000)
                Handler(Looper.getMainLooper()).post {
                    loadButton.isEnabled = true
                    checkJoin()
                }
            }).start()
        }
    }

    private fun processLogout() {
        val loadButton = findViewById<Button>(R.id.loading_button)

        loadButton.text = "핸드폰 인증"
        loadButton.setOnClickListener {
            startActivity(Intent(this, PhoneVerificationActivity::class.java))
            finish()
        }
    }
}