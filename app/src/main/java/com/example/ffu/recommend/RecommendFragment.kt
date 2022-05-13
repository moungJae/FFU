package com.example.ffu.recommend

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ffu.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RecommendFragment : Fragment(R.layout.fragment_recommend) {

    private var auth : FirebaseAuth? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<Button>(R.id.fragment_home_checkprofile)

        auth = Firebase.auth
        button.setOnClickListener {
             auth?.signOut()
        }
    }
}