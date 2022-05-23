package com.example.ffu.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.ffu.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileFragment :Fragment(R.layout.fragment_profile) {

    private lateinit var userDB: DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var userId : String
    private lateinit var storage: FirebaseStorage
    private lateinit var pathReference : StorageReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        userId = getCurrentUserID(view)
        storage = FirebaseStorage.getInstance()
        pathReference = storage.reference

        setToast()
        setProfile(view)
        editProfile(view)
    }

    // 한번만 실행되도록
    private fun setToast() {
        userDB = Firebase.database.getReference("animation").child(userId)
        userDB.get().addOnSuccessListener {
            if (it.child("permission").value.toString().equals("false")) {
                Toast.makeText(context, "프로필 사진을 변환해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setProfile(view: View) {
        val introMe = view.findViewById<TextView>(R.id.profile_introduce)
        val nickname = view.findViewById<TextView>(R.id.profile_nickname_text)
        val image = view.findViewById<ImageView>(R.id.profile_profileimage)

        userDB = Firebase.database.getReference("animation").child(userId)
        userDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    if(ds.key.toString().equals("permission") && ds.value.toString().equals("true")) {
                        pathReference.child("photo/$userId/real.jpg").downloadUrl.addOnCompleteListener{ task ->
                            if (task.isSuccessful) {
                                Glide.with(activity!!)
                                    .load(task.result)
                                    .into(image)
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        userDB = Firebase.database.getReference("profile").child(userId)
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
}