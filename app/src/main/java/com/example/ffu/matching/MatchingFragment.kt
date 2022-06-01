package com.example.ffu.matching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ffu.R
import com.example.ffu.UserInformation
import com.example.ffu.UserInformation.Companion.CURRENT_USERID
import com.example.ffu.UserInformation.Companion.MATCH_USER
import com.example.ffu.UserInformation.Companion.PROFILE
import com.example.ffu.UserInformation.Companion.RECEIVEDLIKE_USER
import com.example.ffu.UserInformation.Companion.URI
import com.example.ffu.databinding.FragmentMatchingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView


class MatchingFragment: Fragment(R.layout.fragment_matching) {

    private lateinit var likeArticleAdapter: LikeArticleAdapter


    private val likeArticleList = mutableListOf<LikeArticleModel>()

    private var binding: FragmentMatchingBinding? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentMatchingBinding = FragmentMatchingBinding.bind(view)
        binding = fragmentMatchingBinding
        likeArticleList.clear()

        likeArticleAdapter = LikeArticleAdapter(onItemClicked = { likeArticleModel ->
            showUserInformation(likeArticleModel)
        })

        addReceivedLikeArticleList()

        fragmentMatchingBinding.matchingRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentMatchingBinding.matchingRecyclerView.adapter = likeArticleAdapter
    }

    private fun addReceivedLikeArticleList(){

        for(likeId in RECEIVEDLIKE_USER.keys){
            if(MATCH_USER[likeId]!=true){
                val name = PROFILE[likeId]?.nickname ?: ""
                val gender = PROFILE[likeId]?.gender ?: ""
                val birth = PROFILE[likeId]?.birth ?: ""
                val imageUri = URI[likeId]?:""
                //Log.d("name",name)
                likeArticleList.add(LikeArticleModel(likeId,name,gender,birth,imageUri))
                likeArticleAdapter.submitList(likeArticleList)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        likeArticleAdapter.notifyDataSetChanged()
    }

    private fun showUserInformation(likeArticleModel: LikeArticleModel) {
        val userId = likeArticleModel.Id
        val dialog = AlertDialog.Builder(requireActivity()).create()
        val edialog : LayoutInflater = LayoutInflater.from(requireActivity())
        val mView : View = edialog.inflate(R.layout.dialog_userinformation,null)
        val image : CircleImageView = mView.findViewById(R.id.dialog_userinformation_photo)
        val cancel : ImageButton = mView.findViewById(R.id.dialog_userinformation_cancel)
        val age : TextView = mView.findViewById(R.id.dialog_userinformation_age)
        val birth : TextView = mView.findViewById(R.id.dialog_userinformation_birth)
        val drinking : TextView = mView.findViewById(R.id.dialog_userinformation_drinking)
        val hobby : TextView = mView.findViewById(R.id.dialog_userinformation_hobby)
        val job : TextView = mView.findViewById(R.id.dialog_userinformation_job)
        val mbti : TextView = mView.findViewById(R.id.dialog_userinformation_mbti)
        val personality : TextView = mView.findViewById(R.id.dialog_userinformation_personality)
        val smoke: TextView = mView.findViewById(R.id.dialog_userinformation_smoke)
        val like : ToggleButton = mView.findViewById(R.id.dialog_userinformation_like)

        age.text="나이 : "+UserInformation.PROFILE[userId]?.age
        birth.text="생일 : "+UserInformation.PROFILE[userId]?.birth
        drinking.text="음주여부 : "+UserInformation.PROFILE[userId]?.drinking
        hobby.text="취미 : "+UserInformation.PROFILE[userId]?.hobby
        job.text="직업 : "+UserInformation.PROFILE[userId]?.job
        mbti.text="mbti : "+UserInformation.PROFILE[userId]?.mbti
        personality.text="성격 : "+UserInformation.PROFILE[userId]?.personality
        smoke.text="흡연여부 : "+UserInformation.PROFILE[userId]?.smoke

        Glide.with(this)
            .load(likeArticleModel.imageUrl)
            .into(image)
        //  취소 버튼 클릭 시
        cancel.setOnClickListener {
            dialog.dismiss()
            dialog.cancel()
        }
        //  완료 버튼 클릭 시

        like.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
            //상대방꺼에 나를 저장
            val otherDB = Firebase.database.reference.child("likeInfo").child(userId).child("match").child(CURRENT_USERID)
            otherDB.setValue("")

            //나에 상대방꺼 저장
            val myDB = Firebase.database.reference.child("likeInfo").child(CURRENT_USERID).child("match").child(userId)
            myDB.setValue("")
            //val myDB = Firebase.database.reference.child("likeInfo").child(CURRENT_USERID).child("match").child(userId)
            //dialog.dismiss()
            //dialog.cancel()
        })

        dialog.setView(mView)
        dialog.create()
        dialog.show()
    }
}