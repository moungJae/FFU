package com.example.ffu

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
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class CheckPhoneNumActivity : AppCompatActivity() {

    private lateinit var userDB: DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var phoneAuthCredential: PhoneAuthCredential
    private lateinit var verificationId : String
    private lateinit var progressBar : ProgressBar
    private lateinit var handler : Handler

    private lateinit var emailUid : String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var phoneNumber: String
    private var verificationFlag = false
    private var finishFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkpn)

        setting()
        goHome()
        requestVerification()
        checkVerification()
        completeJoin()
    }

    // 비정상적인 종료는 등록된 정보들을 제거해야 함
    override fun onDestroy() {
        super.onDestroy()
        if (!finishFlag) {
            deleteUserInformation()
        }
    }

    private fun setting() {
        email = intent.getStringExtra("email").toString()
        password = intent.getStringExtra("passwd").toString()
        auth = Firebase.auth
        emailUid = auth.uid.toString()
        progressBar = findViewById<ProgressBar>(R.id.checkpn_progressBar)
        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                val user = mutableMapOf<String, Any>()

                progressBar?.visibility = View.INVISIBLE
                userDB = Firebase.database.reference.child("users").child(auth?.uid.toString())
                user["tel"] = phoneNumber
                userDB.updateChildren(user)
                auth.signOut()
                finishFlag = true
                finish()
            }
        }
    }

    // 핸드폰 인증까지 완료한 경우 : 핸드폰 정보 + 이메일 정보 전부 제거
    // 핸드폰 인증을 못한 경우 : 이메일 정보 전부 제거
    private fun deleteUserInformation() {
        if (verificationFlag) {
            auth.currentUser?.delete()
            auth.signOut()
            loginWithEmail()
        }
        // signOut 을 한 다음에 바로 로그인 인식을 못하는 issue 가 있어서 delay 추가
        Thread(Runnable {
            while (!auth.uid.toString().equals(emailUid)) {
                Thread.sleep(100)
            }
            userDB = Firebase.database.reference.child("users").child(auth.uid.toString())
            userDB.removeValue()
            userDB = Firebase.database.reference.child("profile").child(auth.uid.toString())
            userDB.removeValue()
            auth.currentUser?.delete()
            auth.signOut()
        }).start()
    }

    private fun goHome() {
        val backButton = findViewById<Button>(R.id.checkpn_back)

        backButton.setOnClickListener {
            deleteUserInformation()
            finish()
        }
    }

    private fun requestVerification() {
        val phoneEditText = findViewById<EditText>(R.id.checkpn_editPhone)
        val requestButton = findViewById<Button>(R.id.checkpn_requestButton)
        val verificationEditText = findViewById<EditText>(R.id.checkpn_editVerificationNum)
        val checkVerificationButton = findViewById<Button>(R.id.checkpn_checkVerification)
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                TODO("Not yet implemented")
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                TODO("Not yet implemented")
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                this@CheckPhoneNumActivity.verificationId = verificationId
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
        val verificationEditText = findViewById<EditText>(R.id.checkpn_editVerificationNum)
        val checkVerificationButton = findViewById<Button>(R.id.checkpn_checkVerification)
        val joinButton = findViewById<Button>(R.id.checkpn_joinButton)
        var myVerification : String

        checkVerificationButton.setOnClickListener {
            myVerification = verificationEditText.text.toString()
            phoneAuthCredential =  PhoneAuthProvider.getCredential(verificationId, myVerification)
            auth.signInWithCredential(phoneAuthCredential!!)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "인증 성공! 회원가입 버튼을 누르세요", Toast.LENGTH_SHORT).show()
                        joinButton.isEnabled = true
                        verificationFlag = true
                    } else {
                        Toast.makeText(this, "인증 실패! 인증 번호를 다시 확인하세요", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun loginWithEmail() {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task -> }
    }

    private fun completeJoin() {
        val joinButton = findViewById<Button>(R.id.checkpn_joinButton)

        joinButton.setOnClickListener {
            auth.signOut()
            loginWithEmail()
            progressBar?.visibility = View.VISIBLE
            // signOut 을 한 다음에 바로 로그인 인식을 못하는 issue 가 있어서 delay 추가
            Thread(Runnable {
                while (!auth.uid.toString().equals(emailUid)) {
                    Thread.sleep(500)
                }
                handler?.handleMessage(Message())
            }).start()
        }
    }
}