package com.example.ffu.join

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.ffu.utils.DBKey.Companion.DB_PROFILE

class CheckLoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userDB: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        if (auth.currentUser == null) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        } else {
            // 로그인 상태이지만 dummy uid 가 들어간 경우를 걸러내기 위함
            userDB = Firebase.database.reference.child(DB_PROFILE)
            userDB.get().addOnSuccessListener {
                var flag = false
                for (user in it.children) {
                    if (auth.uid == user.key) {
                        flag = true
                    }
                }
                if (!flag) {
                    auth.signOut()
                }
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }
}