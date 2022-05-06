package com.example.ffu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    private lateinit var userDB: DatabaseReference
    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        auth = Firebase.auth

        setProfile()
        recommend()
        editProfile()
    }

    private fun setProfile() {
        var userValueDB: DatabaseReference
        val introMe = findViewById<TextView>(R.id.profile_introduce)
        val nickname = findViewById<TextView>(R.id.profile_nickname_text)

        userDB = Firebase.database.getReference("profile").child(auth?.uid.toString())
        userDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    when (ds.key.toString()) {
                        "nickname" -> nickname.setText(ds.value.toString())
                        "introMe" -> introMe.setText(ds.value.toString())
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun recommend() {
        val recommendButton = findViewById<Button>(R.id.home_recommend)

        recommendButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun editProfile() {
        val editProfileButton = findViewById<Button>(R.id.profile_profileedit_button)

        editProfileButton.setOnClickListener {
            val intent = Intent(this, ProfileSettingActivity::class.java)
            startActivity(intent)
        }
    }
}