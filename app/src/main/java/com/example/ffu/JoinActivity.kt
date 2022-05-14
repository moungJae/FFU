package com.example.ffu

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class JoinActivity : AppCompatActivity() {

    private lateinit var userDB: DatabaseReference
    private lateinit var emailVector: Vector<String>
    private lateinit var emailValidation: String
    private lateinit var auth : FirebaseAuth
    private lateinit var progressBar : ProgressBar
    private lateinit var handler : Handler
    private lateinit var emailInfo: String
    private lateinit var passwdInfo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join)

        setting()
        setUserEmails()
        goHome()
        initJoin()
        initCheckEmail()
    }

    private fun setting() {
        auth = Firebase.auth
        progressBar = findViewById<ProgressBar>(R.id.join_progressBar)
        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                val intent = Intent(this@JoinActivity, CheckPhoneNumActivity::class.java)
                progressBar?.visibility = View.INVISIBLE
                intent.putExtra("email", emailInfo)
                intent.putExtra("passwd", passwdInfo)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun goHome() {
        val backButton = findViewById<Button>(R.id.join_backButton)

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun getBirth() : String {
        val editYear = findViewById<EditText>(R.id.join_editYear)
        val editMonth = findViewById<EditText>(R.id.join_editMonth)
        val editDate = findViewById<EditText>(R.id.join_editDate)

        return editYear.text.toString() + "-" + editMonth.text.toString() + "-" + editDate.text.toString()
    }

    private fun initJoin() {
        val joinButton = findViewById<Button>(R.id.join_joinButton)
        val genderRadio = findViewById<RadioGroup>(R.id.join_genderRadio)

        joinButton.setOnClickListener {
            var validationBool: Boolean = true
            var genderCheck: Int = 0 // 1 : 남자, 2 : 여자
            var gender: String = ""
            val name: String = findViewById<EditText>(R.id.join_editName).text.toString()
            val checkPW: String = findViewById<EditText>(R.id.join_editCheckPW).text.toString()
            val birth: String = getBirth()
            val email: String = findViewById<EditText>(R.id.join_editEmail).text.toString()
            val passwd: String = findViewById<EditText>(R.id.join_editPW).text.toString()

            emailInfo = email
            passwdInfo = passwd

            // 0. email 형식 체크
            if (!checkEmail()) { //틀린 경우
                Toast.makeText(applicationContext, "이메일 형식에 맞게 입력하세요!", Toast.LENGTH_LONG).show()
            } else { //맞는 경우
                // 0. 이름 입력했는지 체크
                if (name.equals("")){
                    validationBool = false
                }
                // 1. email 중복 check
                for (otherEmail in emailVector) {
                    if (otherEmail.equals(email)) {
                        validationBool = false
                        findViewById<EditText>(R.id.join_editEmail).setText("")
                        break
                    }
                }
                // 2. 패스워드 check
                if (!passwd.equals(checkPW) || passwd.length < 6) {
                    findViewById<EditText>(R.id.join_editCheckPW).setText("")
                    validationBool = false
                }
                // 3. radio
                genderCheck = when (genderRadio.checkedRadioButtonId) {
                    R.id.join_female -> 2
                    R.id.join_male -> 1
                    else -> 0
                }
                if (genderCheck == 0) {
                    validationBool = false
                } else if (genderCheck == 1) {
                    gender = "남자"
                } else {
                    gender = "여자"
                }
                // 4. 전부 유효한지 아닌지 체크
                if (validationBool) {
                    auth?.createUserWithEmailAndPassword(email, passwd)
                        ?.addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                progressBar?.visibility = View.VISIBLE
                                signUpUser(email, passwd, birth, gender, name)
                                Thread (Runnable {
                                    Thread.sleep(3000)
                                    handler?.handleMessage(Message())
                                }).start()
                            } else {
                                Toast.makeText(this, "이메일을 제대로 입력해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "정보를 다시 입력해주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun signUpUser(email: String, passwd: String, birth: String, gender: String, name: String) {
        val user = mutableMapOf<String, Any>()
        val profile = mutableMapOf<String, Any>()
        val currentTime = System.currentTimeMillis()
        val age = SimpleDateFormat("yyyy-MM-dd-hh-mm")
            .format(currentTime).split("-")[0].toInt() -
                birth.split("-")[0].toInt() + 1

        auth?.signInWithEmailAndPassword(email, passwd)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    userDB = Firebase.database.reference.child("users").child(auth?.uid.toString())
                    user["email"] = email
                    user["passwd"] = passwd
                    user["birth"] = birth
                    user["gender"] = gender
                    user["name"] = name
                    userDB.updateChildren(user)

                    userDB = Firebase.database.reference.child("profile").child(auth?.uid.toString())
                    profile["nickname"] = name
                    profile["age"] = age.toString()
                    profile["job"] = ""
                    profile["introMe"] = "자신을 소개하세요"
                    profile["smoke"] = ""
                    profile["drinking"] = ""
                    profile["mbti"] = ""
                    profile["personality"] = ""
                    profile["religion"] = ""
                    profile["hobby"] = ""
                    profile["photo"] = "false"
                    userDB.updateChildren(profile)
                }
            }
    }

    private fun setUserEmail(userValueDB: DatabaseReference) {
        userValueDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    if (ds.key.toString() == "email") {
                        emailVector.add(ds.value.toString())
                        break
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setUserEmails() {
        var userValueDB: DatabaseReference

        emailVector = Vector<String>()
        userDB = Firebase.database.getReference("users")
        userDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    userValueDB =
                        Firebase.database.reference.child("users").child(ds.key.toString())
                    setUserEmail(userValueDB)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun initCheckEmail() {
        val editEmail = findViewById<EditText>(R.id.join_editEmail)

        editEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkEmail()
            }
        })
    }

    private fun checkEmail(): Boolean {
        emailValidation =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        val email = findViewById<EditText>(R.id.join_editEmail).text.toString().trim() //공백제거
        val p = Pattern.matches(emailValidation, email) // 이메일 패턴이 유효한지

        if (p) {
            findViewById<EditText>(R.id.join_editEmail).setTextColor(R.color.black.toInt())
            return true
        } else {
            findViewById<EditText>(R.id.join_editEmail).setTextColor(-65536)
            return false
        }
    }
}
