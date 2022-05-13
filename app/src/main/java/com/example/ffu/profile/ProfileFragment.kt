package com.example.ffu.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ffu.R
import com.example.ffu.recommend.RecommendFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileFragment :Fragment(R.layout.fragment_profile) {

    private lateinit var userDB: DatabaseReference
    private var auth : FirebaseAuth? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        setProfile(view)
        editProfile(view)
    }


    private fun setProfile(view: View) {
        var userValueDB: DatabaseReference
        var introMe = view.findViewById<TextView>(R.id.profile_introduce)
        val nickname = view.findViewById<TextView>(R.id.profile_nickname_text)

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
    /*
    private fun recommend(view: View) {
        val recommendButton = view.findViewById<Button>(R.id.home_recommend)

        recommendButton.setOnClickListener {
            val intent = Intent(this, RecommendFragment::class.java)
            startActivity(intent)
        }
    }*/

    private fun editProfile(view: View) {
        val editProfileButton = view.findViewById<Button>(R.id.profile_profileedit_button)

        editProfileButton.setOnClickListener {
            activity?.let{
                val intent = Intent(context, ProfileSettingActivity::class.java)
                startActivity(intent)
            }
        }
    }
}