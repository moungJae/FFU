package com.example.ffu

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import java.util.*
import java.util.regex.Pattern

class JoinActivity : AppCompatActivity() {

    private lateinit var userDB: DatabaseReference
    private lateinit var emailVector: Vector<String>
    private lateinit var tel: String
    private lateinit var auth: FirebaseAuth
    private lateinit var emailValidation: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join)

        tel = getIntent().getStringExtra("tel").toString()
        auth = Firebase.auth

        setUserEmails()
        initBackButton()
        initJoinButton()
        initCheckEmail()
    }

    private fun initBackButton() {
        val backButton = findViewById<Button>(R.id.join_backButton)

        backButton.setOnClickListener {
            val intent = Intent(this, CheckPhoneNumActivity::class.java)
            startActivity(intent)
        }
    }

    fun getBirth() : String {
        val editYear = findViewById<EditText>(R.id.join_editYear)
        val editMonth = findViewById<EditText>(R.id.join_editMonth)
        val editDate = findViewById<EditText>(R.id.join_editDate)

        return editYear.text.toString() + "-" + editMonth.text.toString() + "-" + editDate.text.toString()
    }

    private fun initJoinButton() {
        val joinButton = findViewById<Button>(R.id.join_joinButton)
        val genderRadio = findViewById<RadioGroup>(R.id.join_genderRadio)

        joinButton.setOnClickListener {
            var validationBool: Boolean = true
            var genderCheck: Int = 0 // 1 : 남자, 2 : 여자
            var gender: String = ""
            val name: String = findViewById<EditText>(R.id.join_editName).text.toString()
            val email: String = findViewById<EditText>(R.id.join_editEmail).text.toString()
            val passwd: String = findViewById<EditText>(R.id.join_editPW).text.toString()
            val checkPW: String = findViewById<EditText>(R.id.join_editCheckPW).text.toString()
            val birth: String = getBirth()

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
                if (!passwd.equals(checkPW)) {
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
                    auth.createUserWithEmailAndPassword(email, passwd)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                signUpUser(email, passwd, birth, gender, name, tel)
                                finish()
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

    fun signUpUser(email: String, passwd: String, birth: String, gender: String, name: String, tel: String) {
        val user = mutableMapOf<String, Any>()
        val userName = email.split("@")[0]

        userDB = Firebase.database.reference.child("users").child(userName)
        user["email"] = email
        user["passwd"] = passwd
        user["birth"] = birth
        user["gender"] = gender
        user["name"] = name
        user["tel"] = tel
        userDB.updateChildren(user)
    }

    fun setUserEmail(userValueDB: DatabaseReference) {
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

    fun setUserEmails() {
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

    fun initCheckEmail() {
        val editEmail = findViewById<EditText>(R.id.join_editEmail)

        editEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkEmail()
            }
        })
    }

    fun checkEmail(): Boolean {
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
