package com.example.ffu.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.ffu.join.WelcomeActivity
import com.example.ffu.R
import com.example.ffu.join.CheckLoginActivity
import com.example.ffu.recommend.RecommendData
import com.google.firebase.auth.FirebaseAuth

class SettingActivity :AppCompatActivity(){

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initSignOutButton()
    }

    private fun initSignOutButton(){
        val signOutButton = findViewById<Button>(R.id.activity_setting_signOutButton)

        signOutButton.setOnClickListener {
//            RecommendData.myRadius = 500.0
//            RecommendData.MBTIList.clear()
//            RecommendData.personalityList.clear()
//            RecommendData.hobbyList.clear()
//            RecommendData.smokingCheck = true
            auth.signOut()
            ActivityCompat.finishAffinity(this)
            startActivity(Intent(this, CheckLoginActivity::class.java))
            finish()
        }
    }
}