package com.example.ffu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ffu.chatting.ChattingFragment
import com.example.ffu.matching.MatchingFragment
import com.example.ffu.profile.ProfileFragment
import com.example.ffu.recommend.RecommendFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.example.ffu.utils.DBKey.Companion.DB_ANIMATION
import com.example.ffu.utils.DBKey.Companion.DB_PROFILE
import com.example.ffu.UserInformation.Companion.CURRENT_USERID

class BackgroundActivity : AppCompatActivity() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userDB: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.background)

        val bottomNavigationView = findViewById<ChipNavigationBar>(R.id.bottomNavigationView)
        val recommendFragment = RecommendFragment()
        val matchingFragment = MatchingFragment()
        val profileFragment = ProfileFragment()
        val chattingFragment = ChattingFragment()

        checkSetProfile()
        //처음 시작화면
        replaceFragment(profileFragment)

        bottomNavigationView.setItemSelected(R.id.profile)
        bottomNavigationView.setOnItemSelectedListener {
            when(it){
                R.id.recommend-> replaceFragment(recommendFragment)
                R.id.matching-> replaceFragment(matchingFragment)
                R.id.profile-> replaceFragment(profileFragment)
                R.id.chatting->replaceFragment(chattingFragment)
            }
            true
        }
    }

    // 프로필 편집을 하게 되는 경우
    private fun checkSetProfile() {
        var animationFlag = 0
        var profileFlag = 0

        userDB = Firebase.database.reference.child(DB_ANIMATION).child(CURRENT_USERID)
        userDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                animationFlag++
                if (animationFlag > 1) {
                    finish()
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        userDB = Firebase.database.reference.child(DB_PROFILE).child(CURRENT_USERID)
        userDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                profileFlag++
                if (profileFlag > 1) {
                    finish()
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun replaceFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction()
            .apply{
                replace(R.id.fragmentContainer,fragment)
                commit()
            }
    }

}