package com.example.ffu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CheckJoinActivity : AppCompatActivity() {

    private lateinit var userDB: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        if (auth.currentUser != null) { // 로그인 상태
            userDB = Firebase.database.reference.child("profile").child(auth.uid.toString())
            userDB.get().addOnSuccessListener {
                if (it.child("join").value == null) {
                    val intent = Intent(this@CheckJoinActivity, JoinActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    if (it.child("join").value.toString().equals("true")) {
                        val intent = Intent(this@CheckJoinActivity, BackgroundActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@CheckJoinActivity, JoinActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        } else { // 로그아웃 상태
            val intent = Intent(this@CheckJoinActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}