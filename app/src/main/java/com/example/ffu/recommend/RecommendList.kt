package com.example.ffu.recommend

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ffu.DBKey.Companion.DB_PROFILE
import com.example.ffu.R
import com.example.ffu.databinding.FragmentBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RecommendList(recommendUsersUid: ArrayList<String>) : BottomSheetDialogFragment() {

    private val users = recommendUsersUid // 거리에 매치되는 user 리스트
    // firebase
    private lateinit var userDB: DatabaseReference
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val userList = mutableListOf<RecommendArticleModel>()

    private val listener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val recommendArticleModel = snapshot.getValue(RecommendArticleModel::class.java)
            recommendArticleModel ?: return
            userList.add(recommendArticleModel)
            recommendAdapter.submitList(userList)
        }
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}
    }
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
            userDB.removeEventListener(listener)
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
        // test
//        recommendAdapter.submitList(mutableListOf<RecommendArticleModel>().apply {
////            add(RecommendArticleModel("test1", 22, "mbti1", ""))
////            add(RecommendArticleModel("test2", 23, "mbti2", ""))
////            add(RecommendArticleModel("test3", 24, "mbti3", ""))
//        })
        fragmentBottomsheetBinding.recommendedUsersView.layoutManager = LinearLayoutManager(context)
        fragmentBottomsheetBinding.recommendedUsersView.adapter = recommendAdapter

        userDB.addChildEventListener(listener)

    }

    override fun onDestroy() {
        super.onDestroy()
        userDB.removeEventListener(listener)
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