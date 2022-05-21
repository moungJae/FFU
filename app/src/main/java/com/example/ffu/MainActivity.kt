package com.example.ffu

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var userDB: DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var phoneAuthCredential: PhoneAuthCredential
    private lateinit var verificationId : String
    private lateinit var progressBar : ProgressBar
    private lateinit var phoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        initialSetting()
        requestVerification()
        checkVerification()
        completeVerification()
    }

    private fun initialSetting() {
        auth = Firebase.auth
        progressBar = findViewById<ProgressBar>(R.id.main_progressBar)
    }

    private fun requestVerification() {
        val phoneEditText = findViewById<EditText>(R.id.main_editPhone)
        val requestButton = findViewById<Button>(R.id.main_requestButton)
        val verificationEditText = findViewById<EditText>(R.id.main_editVerificationNum)
        val checkVerificationButton = findViewById<Button>(R.id.main_checkVerification)
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                TODO("Not yet implemented")
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                TODO("Not yet implemented")
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                this@MainActivity.verificationId = verificationId
                verificationEditText.isEnabled = true
                checkVerificationButton.isEnabled = true
            }
        }

        requestButton.setOnClickListener {
            var phoneNum = phoneEditText.text.toString()

            phoneNumber = phoneNum
            phoneNum = "+82" + phoneNum.substring(1, phoneNum.length)
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum,
                90,
                TimeUnit.SECONDS,
                this,
                callbacks )
        }
    }

    private fun checkVerification() {
        val verificationEditText = findViewById<EditText>(R.id.main_editVerificationNum)
        val checkVerificationButton = findViewById<Button>(R.id.main_checkVerification)
        val joinButton = findViewById<Button>(R.id.main_joinButton)
        var myVerification : String

        checkVerificationButton.setOnClickListener {
            myVerification = verificationEditText.text.toString()
            phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, myVerification)
            Log.d("phoneAuthCredential", phoneAuthCredential.toString())
            auth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val profile = mutableMapOf<String, Any>()
                        Toast.makeText(this, "인증 성공! 다음 버튼을 누르세요", Toast.LENGTH_SHORT).show()
                        joinButton.isEnabled = true
                        userDB = Firebase.database.reference.child("profile").child(auth.uid.toString())
                        profile["tel"] = phoneNumber
                        profile["join"] = "false"
                        userDB.updateChildren(profile)
                    } else {
                        Toast.makeText(this, "인증 실패! 인증 번호를 다시 확인하세요", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun completeVerification() {
        val joinButton = findViewById<Button>(R.id.main_joinButton)

        joinButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            Thread(Runnable {
                Thread.sleep(1500)
                Handler(Looper.getMainLooper()).post {
                    progressBar.visibility = View.INVISIBLE
                    val intent = Intent(this@MainActivity, CheckJoinActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }).start()
        }
    }
}