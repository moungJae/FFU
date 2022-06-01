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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.example.ffu.UserInformation.Companion.PROFILE
import com.example.ffu.UserInformation.Companion.ANIMATION
import com.example.ffu.UserInformation.Companion.CURRENT_USERID
import com.example.ffu.UserInformation.Companion.URI
import com.example.ffu.chatting.HistoryAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database

class ProfileFragment :Fragment(R.layout.fragment_profile) {

    private lateinit var auth : FirebaseAuth
    private lateinit var userId : String
    private lateinit var storage: FirebaseStorage
    private lateinit var pathReference : StorageReference
    private val historyAdapter = HistoryAdapter()
    private val historyList = mutableListOf<HistoryModel>()
    private lateinit var historyRecyclerView : RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        userId = getCurrentUserID(view)
        storage = FirebaseStorage.getInstance()
        pathReference = storage.reference

        historyRecyclerView = view.findViewById<RecyclerView>(R.id.fragment_profile_historyRecyclerView)
        historyList.clear()

        setProfile(view)
        setHistory(view)
        editProfile(view)
        settingButton(view)
    }

    fun setProfile(view: View) {
        val introMe = view.findViewById<TextView>(R.id.profile_introduce)
        val nickname = view.findViewById<TextView>(R.id.profile_nickname_text)
        val image = view.findViewById<ImageView>(R.id.profile_profile_image)

        if (ANIMATION[userId]?.permission == true) {
            Glide.with(this)
                .load(URI[userId])
                .into(image)
            nickname.setText(PROFILE[userId]?.nickname)
            introMe.setText(PROFILE[userId]?.introMe)

        } else {
            nickname.setText(PROFILE[userId]?.nickname)
            introMe.setText(PROFILE[userId]?.introMe)
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

    private fun getCurrentUserID(view: View): String{
        if(auth.currentUser==null){
            Snackbar.make(view, "로그인되지 않았습니다", Snackbar.LENGTH_LONG).show()
        }
        return auth.currentUser?.uid.orEmpty()
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
        //private fun addUserHistory() {
            val userDB = Firebase.database.reference.child("history").child(CURRENT_USERID)

            userDB.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.getValue(HistoryModel::class.java) != null) {
                        historyList.add(snapshot.getValue(HistoryModel::class.java) as HistoryModel)
                        historyAdapter.submitList(historyList)
                        historyAdapter.notifyDataSetChanged()
                        historyRecyclerView.scrollToPosition(historyAdapter.itemCount - 1)
                        //HISTORY_LIST.add(snapshot.getValue(HistoryModel::class.java) as HistoryModel)
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
            /*
        for(historyModel in HISTORY_LIST){

        }*/
    }
}