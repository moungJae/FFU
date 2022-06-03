package com.example.ffu.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ffu.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.example.ffu.UserInformation.Companion.PROFILE
import com.example.ffu.UserInformation.Companion.ANIMATION
import com.example.ffu.UserInformation.Companion.HISTORY
import com.example.ffu.UserInformation.Companion.URI
import com.example.ffu.chatting.HistoryAdapter
import com.example.ffu.UserInformation.Companion.CURRENT_USERID
import com.example.ffu.recommend.RecommendData

class ProfileFragment :Fragment(R.layout.fragment_profile) {

    private lateinit var auth : FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var pathReference : StorageReference
    private val historyAdapter = HistoryAdapter()
    private lateinit var historyRecyclerView : RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        storage = FirebaseStorage.getInstance()
        pathReference = storage.reference

        historyRecyclerView = view.findViewById<RecyclerView>(R.id.fragment_profile_historyRecyclerView)

        setProfile(view)
        setHistory(view)
        editProfile(view)
        settingButton(view)
    }

    fun setProfile(view: View) {
        val introMe = view.findViewById<TextView>(R.id.profile_introduce)
        val nickname = view.findViewById<TextView>(R.id.profile_nickname_text)
        val image = view.findViewById<ImageView>(R.id.profile_profile_image)

        if (ANIMATION[CURRENT_USERID]?.permission == true) {
            Glide.with(this)
                .load(URI[CURRENT_USERID])
                .into(image)
            nickname.setText(PROFILE[CURRENT_USERID]?.nickname)
            introMe.setText(PROFILE[CURRENT_USERID]?.introMe)

        } else {
            nickname.setText(PROFILE[CURRENT_USERID]?.nickname)
            introMe.setText(PROFILE[CURRENT_USERID]?.introMe)
            Toast.makeText(context, "프로필을 변경해주세요!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editProfile(view: View) {
        val editProfileButton = view.findViewById<Button>(R.id.profile_profileedit_button)

        editProfileButton.setOnClickListener {
            activity?.let {
                val intent = Intent(context, ProfileSettingActivity::class.java)
                startActivity(intent)

            }
        }
    }

    private fun settingButton(view: View){
        val settingButton = view.findViewById<Button>(R.id.settingButton)

        settingButton.setOnClickListener {
            activity?.let {
                val intent = Intent(context, SettingActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setHistory(view : View){
        addHistoryList()
        historyRecyclerView .adapter = historyAdapter
        historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun addHistoryList(){
        historyAdapter.submitList(HISTORY)
        historyAdapter.notifyDataSetChanged()
        historyRecyclerView.scrollToPosition(historyAdapter.itemCount - 1)
    }
}