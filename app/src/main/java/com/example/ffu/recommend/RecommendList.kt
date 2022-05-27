package com.example.ffu.recommend

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ffu.DBKey.Companion.DB_PROFILE
import com.example.ffu.R
import com.example.ffu.databinding.FragmentBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
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