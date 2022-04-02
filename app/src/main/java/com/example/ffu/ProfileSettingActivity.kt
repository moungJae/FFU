package com.example.ffu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ProfileSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profilesetting)

        val mbtiButton = findViewById<Button>(R.id.profilesetting_mbtiButton)
        val personality = findViewById<Button>(R.id.profilesetting_personalButton)
        val hobby = findViewById<Button>(R.id.profilesetting_hobbyButton)
        val religion = findViewById<Button>(R.id.profilesetting_religionButton)
        val exitButton = findViewById<Button>(R.id.profilesetting_saveButton)

        mbtiButton.setOnClickListener {
            val intent1 = Intent(this, MbtiActivity::class.java)
            startActivity(intent1)
            finish()
        }
        personality.setOnClickListener {
            val intent2 = Intent(this, PersonalActivity::class.java)
            startActivity(intent2)
            finish()
        }
        hobby.setOnClickListener {
            val intent3 = Intent(this, HobbyActivity::class.java)
            startActivity(intent3)
            finish()
        }
        religion.setOnClickListener {
            val intent4 = Intent(this, ReligionActivity::class.java)
            startActivity(intent4)
            finish()
        }

        exitButton.setOnClickListener {
            val intent5 = Intent(this, ProfileActivity::class.java)
            startActivity(intent5)
            finish()
        }


    }

}


