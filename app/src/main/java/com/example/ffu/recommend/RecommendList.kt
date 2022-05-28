package com.example.ffu.recommend

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ffu.DBKey.Companion.DB_PROFILE
import com.example.ffu.R
import com.example.ffu.UserInformation
import com.example.ffu.UserInformation.*
import com.example.ffu.UserInformation.Companion.AGE
import com.example.ffu.UserInformation.Companion.BIRTH
import com.example.ffu.UserInformation.Companion.GENDER
import com.example.ffu.UserInformation.Companion.MATCH_USER
import com.example.ffu.UserInformation.Companion.MBTI
import com.example.ffu.UserInformation.Companion.NICKNAME
import com.example.ffu.UserInformation.Companion.URI
import com.example.ffu.chatting.ArticleModel
import com.example.ffu.databinding.FragmentBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RecommendList(recommendUsersUid: ArrayList<String>) : BottomSheetDialogFragment() {

    private val usersUid = recommendUsersUid // 거리에 매치되는 user 리스트
    // firebase
    private lateinit var userDB: DatabaseReference
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val userList = mutableListOf<RecommendArticleModel>()

//    val profileListener = userDB.addValueEventListener(object : ValueEventListener {
//        override fun onDataChange(snapshot: DataSnapshot) {
//            for (ds in snapshot.children) {
//                when(ds.key.toString()) {
//                }
//            }
//        }
//        override fun onCancelled(error: DatabaseError) {}
//    })
//    private val listener = object : ChildEventListener {
//        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
////            Log.d("snapshot.key", "${snapshot.key}")
////            val recommendArticleModel = snapshot.getValue(RecommendArticleModel::class.java)
////            Log.d("recommendArticleModel", "$recommendArticleModel")
////            recommendArticleModel ?: return
//            if (!usersUid.contains(snapshot.key)) return
//            val id = snapshot.key.toString()
//            val mbti = snapshot.child("profile").child("mbti").value.toString()
//            val nickname = snapshot.child("profile").child("nickname").value.toString()
//            val age = snapshot.child("profile").child("age").value.toString()
//            userList.add(RecommendArticleModel(id, nickname, age, mbti))
//            recommendAdapter.submitList(userList)
//        }
//        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
//        override fun onChildRemoved(snapshot: DataSnapshot) {}
//        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
//        override fun onCancelled(error: DatabaseError) {}
//    }
    // user View
    private var binding: FragmentBottomsheetBinding? = null
    private lateinit var recommendAdapter: RecommendAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_bottomsheet, container, false)
        view.findViewById<Button>(R.id.returnToMap).setOnClickListener {
            //userDB.removeEventListener(listener)
            dismiss()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userList.clear()
        userDB = Firebase.database.reference.child(DB_PROFILE)

        // bottomsheet view
        val fragmentBottomsheetBinding = FragmentBottomsheetBinding.bind(view)
        binding = fragmentBottomsheetBinding

        recommendAdapter = RecommendAdapter()

        addArticleList()
        fragmentBottomsheetBinding.recommendedUsersView.layoutManager = LinearLayoutManager(context)
        fragmentBottomsheetBinding.recommendedUsersView.adapter = recommendAdapter

        //userDB.addChildEventListener(listener)
    }
    private fun addArticleList(){
        for(uid in usersUid){
            Log.d("uid", "$uid")
            val nickname = NICKNAME[uid].toString()
            val age = AGE[uid].toString()
            val mbti = MBTI[uid].toString()
            val imageUri = URI[uid].toString()
            Log.d("data", "$nickname, $age, $mbti, $imageUri")

            userList.add(RecommendArticleModel(uid, nickname, age, mbti, imageUri))
            recommendAdapter.submitList(userList)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener{ dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupRatio(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        // 위 수치는 기기 높이 대비 80%로 다이얼로그 높이를 설정
        return getWindowHeight() * 100 / 100
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onDestroy() {
        super.onDestroy()
        //userDB.removeEventListener(listener)
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        recommendAdapter.notifyDataSetChanged()
    }
    companion object {
        const val TAG = "RecommendList"
    }
}