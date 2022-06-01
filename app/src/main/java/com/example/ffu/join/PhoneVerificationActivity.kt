package com.example.ffu.join

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.dx.dxloadingbutton.lib.LoadingButton
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.metagalactic.dotprogressbar.DotProgressBar
import java.util.concurrent.TimeUnit
import com.example.ffu.utils.DBKey.Companion.DB_PROFILE
import com.example.ffu.R

class PhoneVerificationActivity : AppCompatActivity() {

    private lateinit var userDB: DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var phoneAuthCredential: PhoneAuthCredential
    private lateinit var verificationId : String
    private lateinit var phoneNumber: String
    private lateinit var callbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private var requestFlag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.phone_verification)

        initSetting()
        initCallback()
        requestVerification()
        checkVerification()
        completeVerification()
    }

    private fun initSetting() {
        auth = Firebase.auth
        auth.setLanguageCode("kr")
    }

    private fun initCallback() {
        val requestButton = findViewById<LoadingButton>(R.id.main_requestButton)

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {  }
            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(this@PhoneVerificationActivity, "잘못된 전화번호입니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                requestButton.loadingFailed()
                setPhoneEnable()
            }
            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                this@PhoneVerificationActivity.verificationId = verificationId
                setVerificationEnable()
                requestButton.loadingSuccessful()
                setPhoneEnable()
            }
        }
    }

    private fun setPhoneEnable() {
        val phoneEditText = findViewById<EditText>(R.id.main_editPhone)
        val requestButton = findViewById<LoadingButton>(R.id.main_requestButton)

        phoneEditText.isEnabled = true
        requestButton.isEnabled = true
    }

    private fun setPhoneDisable() {
        val phoneEditText = findViewById<EditText>(R.id.main_editPhone)
        val requestButton = findViewById<LoadingButton>(R.id.main_requestButton)

        requestFlag = true
        phoneEditText.isEnabled = false
        requestButton.isEnabled = false
    }

    private fun setVerificationEnable() {
        val verificationEditText = findViewById<EditText>(R.id.main_editVerificationNum)
        val checkVerificationButton = findViewById<LoadingButton>(R.id.main_checkVerification)

        verificationEditText.isEnabled = true
        checkVerificationButton.isEnabled = true
    }

    private fun setVerificationDisable() {
        val verificationEditText = findViewById<EditText>(R.id.main_editVerificationNum)
        val checkVerificationButton = findViewById<LoadingButton>(R.id.main_checkVerification)

        verificationEditText.isEnabled = false
        checkVerificationButton.isEnabled = false
    }

    private fun requestPhone() {
        val phoneEditText = findViewById<EditText>(R.id.main_editPhone)
        var phoneNum = phoneEditText.text.toString()

        phoneNumber = phoneNum
        phoneNum = "+82" + phoneNum.substring(1, phoneNum.length)
        setPhoneDisable()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNum,
            90,
            TimeUnit.SECONDS,
            this,
            callbacks )
    }

    private fun requestVerification() {
        val phoneEditText = findViewById<EditText>(R.id.main_editPhone)
        val requestButton = findViewById<LoadingButton>(R.id.main_requestButton)

        requestButton.setOnClickListener {
            val phoneNum = phoneEditText.text.toString()

            requestButton.startLoading()
            if (phoneNum.length == 11) {
                if (!requestFlag) {
                    requestPhone()
                } else {
                    requestButton.loadingFailed()
                    Toast.makeText(this, "이미 인증 요청을 하셨습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                requestButton.loadingFailed()
                Toast.makeText(this, "휴대전화번호를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun successVerification() {
        val profile = mutableMapOf<String, Any>()
        val joinButton = findViewById<Button>(R.id.main_joinButton)
        val checkVerificationButton = findViewById<LoadingButton>(R.id.main_checkVerification)

        setPhoneEnable()
        setVerificationEnable()
        joinButton.isEnabled = true
        checkVerificationButton.loadingSuccessful()
        Toast.makeText(this, "인증 성공! 다음 화면으로 넘어가세요.", Toast.LENGTH_SHORT).show()

        userDB = Firebase.database.reference.child(DB_PROFILE).child(auth.uid.toString())
        profile["tel"] = phoneNumber
        userDB.updateChildren(profile)
    }

    private fun failVerification() {
        val checkVerificationButton = findViewById<LoadingButton>(R.id.main_checkVerification)

        setPhoneEnable()
        setVerificationEnable()
        checkVerificationButton.loadingFailed()
        Toast.makeText(this, "인증 실패! 인증 번호를 다시 확인하세요", Toast.LENGTH_SHORT).show()
    }

    private fun checkVerification() {
        val verificationEditText = findViewById<EditText>(R.id.main_editVerificationNum)
        val checkVerificationButton = findViewById<LoadingButton>(R.id.main_checkVerification)
        var myVerification : String

        checkVerificationButton.setOnClickListener {
            setPhoneDisable()
            setVerificationDisable()
            checkVerificationButton.startLoading()
            myVerification = verificationEditText.text.toString()
            phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, myVerification)
            auth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        successVerification()
                    } else {
                        failVerification()
                    }
                }
        }
    }

    private fun returnActivity() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }

    private fun completeVerification() {
        val joinButton = findViewById<Button>(R.id.main_joinButton)
        val progressBar = findViewById<DotProgressBar>(R.id.main_progressbar)

        joinButton.setOnClickListener {
            setPhoneDisable()
            setVerificationDisable()
            progressBar.visibility = View.VISIBLE
            Thread(Runnable {
                Thread.sleep(2000)
                Handler(Looper.getMainLooper()).post {
                    returnActivity()
                }
            }).start()
        }
    }
}