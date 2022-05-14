package com.example.ffu

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.concurrent.TimeUnit

class CheckPhoneNumActivity : AppCompatActivity() {

    private lateinit var userDB: DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var progressBar : ProgressBar
    private lateinit var handler : Handler
    private lateinit var emailInfo: String
    private lateinit var passwdInfo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkpn)

        emailInfo = intent.getStringExtra("email").toString()
        passwdInfo = intent.getStringExtra("passwd").toString()
        auth = Firebase.auth
        auth.setLanguageCode(Locale.getDefault().language)
        progressBar = findViewById<ProgressBar>(R.id.checkpn_progressBar)
        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                progressBar?.visibility = View.INVISIBLE
                finish()
            }
        }
        goHome()
        requestVerification()
        checkVerification()
    }

    private fun goHome() {
        val backButton = findViewById<Button>(R.id.checkpn_back)

        backButton.setOnClickListener {
            userDB = Firebase.database.reference.child("users").child(auth.uid.toString())
            userDB.removeValue()
            userDB = Firebase.database.reference.child("profile").child(auth.uid.toString())
            userDB.removeValue()
            auth.currentUser?.delete()
            finish()
        }
    }

    private fun requestVerification() {
        val requestButton = findViewById<Button>(R.id.checkpn_requestButton)
        val phoneEditText = findViewById<EditText>(R.id.checkpn_editPhone)

        requestButton.setOnClickListener{

        }
    }

    private fun checkVerification() {

    }
}