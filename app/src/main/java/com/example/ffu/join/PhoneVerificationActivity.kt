package com.example.ffu.join

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
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
                Toast.makeText(this@PhoneVerificationActivity, "인증 요청이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                this@PhoneVerificationActivity.verificationId = verificationId
                requestButton.loadingSuccessful()
                setVerificationEnable()
                setTimer()
            }
        }
    }

    private fun makeTime(minute : Int, second : Int) : String {
        var result = ""

        result += "0" + minute.toString() + "분 "
        result += if (second < 10) {
            "0" + second.toString() + "초"
        } else {
            second.toString() + "초"
        }
        return result
    }

    private fun setTimer() {
        val timerText = findViewById<TextView>(R.id.main_timer_text)
        var minute = 2
        var second = 0

        Thread {
            while (minute != 0 || second != 0) {
                Handler(Looper.getMainLooper()).post {
                    timerText.text = makeTime(minute, second)
                }
                if (second == 0) {
                    minute--
                    second = 59
                } else {
                    second--
                }
                Thread.sleep(1000)
            }
        }.start()
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

        verificationEditText.isEnabled = true
    }

    private fun setVerificationDisable() {
        val verificationEditText = findViewById<EditText>(R.id.main_editVerificationNum)

        verificationEditText.isEnabled = false
    }

    private fun requestPhone() {
        val phoneEditText = findViewById<EditText>(R.id.main_editPhone)
        var phoneNum = phoneEditText.text.toString()

        phoneNumber = phoneNum
        phoneNum = "+82" + phoneNum.substring(1, phoneNum.length)
        setPhoneDisable()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNum,
            120,
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
                    Toast.makeText(this, "이미 인증요청을 하셨습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                requestButton.loadingFailed()
                Toast.makeText(this, "휴대전화번호를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkVerification() {
        val verificationEditText = findViewById<EditText>(R.id.main_editVerificationNum)
        val progressBar = findViewById<DotProgressBar>(R.id.main_progressbar)
        var myVerification : String

        verificationEditText.addTextChangedListener {
            myVerification = verificationEditText.text.toString()
            if (myVerification.length == 6) {
                setVerificationDisable()
                progressBar.visibility = View.VISIBLE
                phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, myVerification)
                Thread {
                    Thread.sleep(1500)
                    Handler(Looper.getMainLooper()).post {
                        auth.signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    successVerification()
                                } else {
                                    failVerification()
                                }
                                progressBar.visibility = View.INVISIBLE
                            }
                    }
                }.start()
            }
        }
    }

    private fun successVerification() {
        val profile = mutableMapOf<String, Any>()

        userDB = Firebase.database.reference.child(DB_PROFILE).child(auth.uid.toString())
        profile["tel"] = phoneNumber
        userDB.updateChildren(profile)

        returnActivity()
    }

    private fun failVerification() {
        val verificationEditText = findViewById<EditText>(R.id.main_editVerificationNum)

        verificationEditText.setText("")
        Toast.makeText(this, "인증 실패! 인증번호를 다시 확인하세요", Toast.LENGTH_SHORT).show()
        setVerificationEnable()
    }

    private fun returnActivity() {
        ActivityCompat.finishAffinity(this)
        startActivity(Intent(this,  WelcomeActivity::class.java))
    }
}