package com.example.ffu

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.ffu.profile.ProfileSettingActivity
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
    private var requestFlag: Boolean = false

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
        auth.setLanguageCode("kr")
        progressBar = findViewById<ProgressBar>(R.id.main_progressBar)
    }

    private fun setPhoneEnable() {
        val phoneEditText = findViewById<EditText>(R.id.main_editPhone)
        val requestButton = findViewById<Button>(R.id.main_requestButton)

        phoneEditText.isEnabled = true
        requestButton.isEnabled = true
        progressBar.visibility = View.INVISIBLE
    }

    private fun setPhoneDisable() {
        val phoneEditText = findViewById<EditText>(R.id.main_editPhone)
        val requestButton = findViewById<Button>(R.id.main_requestButton)

        requestFlag = true
        phoneEditText.isEnabled = false
        requestButton.isEnabled = false
        progressBar.visibility = View.VISIBLE
    }

    private fun setVerificationEnable() {
        val verificationEditText = findViewById<EditText>(R.id.main_editVerificationNum)
        val checkVerificationButton = findViewById<Button>(R.id.main_checkVerification)

        verificationEditText.isEnabled = true
        checkVerificationButton.isEnabled = true
        progressBar.visibility = View.INVISIBLE
    }

    private fun setVerificationDisable() {
        val verificationEditText = findViewById<EditText>(R.id.main_editVerificationNum)
        val checkVerificationButton = findViewById<Button>(R.id.main_checkVerification)


        verificationEditText.isEnabled = false
        checkVerificationButton.isEnabled = false
        progressBar.visibility = View.VISIBLE
    }

    private fun requestVerification() {
        val phoneEditText = findViewById<EditText>(R.id.main_editPhone)
        val requestButton = findViewById<Button>(R.id.main_requestButton)

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                //
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(this@MainActivity, "잘못된 전화번호입니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.INVISIBLE
                setPhoneEnable()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                this@MainActivity.verificationId = verificationId
                setPhoneEnable()
                setVerificationEnable()
                progressBar.visibility = View.INVISIBLE
            }
        }

        requestButton.setOnClickListener {
            var phoneNum = phoneEditText.text.toString()

            if (phoneNum.length == 11) {
                if (!requestFlag) {
                    setPhoneDisable()
                    phoneNumber = phoneNum
                    phoneNum = "+82" + phoneNum.substring(1, phoneNum.length)
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNum,
                        90,
                        TimeUnit.SECONDS,
                        this,
                        callbacks )
                } else {
                    Toast.makeText(this, "이미 인증 요청을 하셨습니다.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "휴대전화번호를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkVerification() {
        val verificationEditText = findViewById<EditText>(R.id.main_editVerificationNum)
        val checkVerificationButton = findViewById<Button>(R.id.main_checkVerification)
        val joinButton = findViewById<Button>(R.id.main_joinButton)
        var myVerification : String

        checkVerificationButton.setOnClickListener {
            setPhoneDisable()
            setVerificationDisable()
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
                        userDB.updateChildren(profile)
                    } else {
                        Toast.makeText(this, "인증 실패! 인증 번호를 다시 확인하세요", Toast.LENGTH_SHORT).show()
                    }
                    setPhoneEnable()
                    setVerificationEnable()
                }
        }
    }

    private fun completeVerification() {
        val joinButton = findViewById<Button>(R.id.main_joinButton)

        joinButton.setOnClickListener {
            setPhoneDisable()
            setVerificationDisable()
            Thread(Runnable {
                Thread.sleep(2000)
                Handler(Looper.getMainLooper()).post {
                    val intent = Intent(this@MainActivity, CheckJoinActivity::class.java)
                    progressBar.visibility = View.INVISIBLE
                    startActivity(intent)
                    finish()
                }
            }).start()
        }
    }
}