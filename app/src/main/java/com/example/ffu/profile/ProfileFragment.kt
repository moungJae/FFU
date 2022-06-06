package com.example.ffu.profile

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
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
import com.example.ffu.utils.DBKey
import com.google.firebase.database.ktx.database
import com.example.ffu.join.CheckLoginActivity
import com.example.ffu.recommend.RecommendData
import com.google.firebase.database.*

class ProfileFragment :Fragment(R.layout.fragment_profile) {

    private lateinit var auth : FirebaseAuth
    private lateinit var userDB: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var pathReference : StorageReference
    private val historyAdapter = HistoryAdapter()
    private lateinit var historyRecyclerView : RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeInformation(view)
        historyListener()
        setProfile(view)
        setHistory(view)
        editProfile(view)
        settingButton(view)
        checkSetProfile(view)
    }

    private fun initializeInformation(view: View) {
        auth = Firebase.auth
        storage = FirebaseStorage.getInstance()
        pathReference = storage.reference
        historyRecyclerView = view.findViewById<RecyclerView>(R.id.fragment_profile_historyRecyclerView)
    }

    private fun historyListener() {
        userDB = Firebase.database.reference.child("history").child(CURRENT_USERID)
        userDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                addHistoryList()
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                addHistoryList()
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
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
            settingDialog()
        }
    }

    private fun setHistory(view : View){
        historyRecyclerView .adapter = historyAdapter
        val manager = LinearLayoutManager(requireContext())
        manager.reverseLayout=true
        manager.stackFromEnd=true
        historyRecyclerView.layoutManager =manager
        addHistoryList()
    }

    private fun addHistoryList(){
        historyAdapter.submitList(HISTORY)
        historyAdapter.notifyDataSetChanged()
        historyRecyclerView.scrollToPosition(historyAdapter.itemCount - 1)
    }

    private fun checkSetProfile(view : View) {
        userDB = Firebase.database.reference.child(DBKey.DB_ANIMATION).child(CURRENT_USERID)
        userDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                setProfile(view)
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        userDB = Firebase.database.reference.child(DBKey.DB_PROFILE).child(CURRENT_USERID)
        userDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                setProfile(view)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun settingDialog() {
        val dialog = AlertDialog.Builder(requireActivity()).create()
        val edialog : LayoutInflater = LayoutInflater.from(requireActivity())
        val mView : View = edialog.inflate(R.layout.dialog_setting,null)
        val logout : Button = mView.findViewById(R.id.dialog_setting_logout)
        val back : ImageButton = mView.findViewById(R.id.dialog_setting_back)

        back.setOnClickListener{
            dialog.dismiss()
            dialog.cancel()
        }

        logout.setOnClickListener{
            requestDialog(dialog)
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setView(mView)
        dialog.create()
        dialog.show()
    }

    private fun requestDialog(beforeDialog: AlertDialog){
        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setTitle("로그아웃")
            .setMessage("로그아웃 하시겠습니까?")
            .setNegativeButton("예",
                DialogInterface.OnClickListener{ dialog,id->
                    val act = context as Activity
                    auth.signOut()
                    ActivityCompat.finishAffinity(act)
                    val intent = Intent(context,  CheckLoginActivity::class.java)
                    startActivity(intent)
                    dialog.dismiss()
                    dialog.cancel()
                    beforeDialog.dismiss()
                    beforeDialog.cancel()
                })
            .setPositiveButton("아니오",
                DialogInterface.OnClickListener{dialog,id->
                    dialog.dismiss()
                    dialog.cancel()
                })
        dialog.show()
    }
}