package com.example.ffu

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ffu.chatting.ChattingFragment
import com.example.ffu.profile.ProfileFragment
import com.example.ffu.recommend.RecommendFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class BackgroundActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.background)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val recommendFragment = RecommendFragment()
        val profileFragment = ProfileFragment()
        val chattingFragment = ChattingFragment()

        //처음 시작화면
        replaceFragment(recommendFragment)


        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.recommend-> replaceFragment(recommendFragment)
                R.id.profile-> replaceFragment(profileFragment)
                R.id.chatting->replaceFragment(chattingFragment)
            }
            true
        }

    }

    private fun replaceFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction()
            .apply{
                replace(R.id.fragmentContainer,fragment)
                commit()
            }
    }
}