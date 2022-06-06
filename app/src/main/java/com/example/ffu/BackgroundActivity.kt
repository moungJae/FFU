package com.example.ffu

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.ffu.chatting.ChattingFragment
import com.example.ffu.matching.MatchingFragment
import com.example.ffu.profile.ProfileFragment
import com.example.ffu.recommend.RecommendFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.example.ffu.utils.DBKey.Companion.DB_ANIMATION
import com.example.ffu.utils.DBKey.Companion.DB_PROFILE
import com.example.ffu.UserInformation.Companion.CURRENT_USERID
import com.example.ffu.databinding.BackgroundBinding
import com.example.ffu.recommend.RecommendData
import com.example.ffu.utils.Recommend
import com.google.android.material.bottomnavigation.BottomNavigationMenuView

private const val TAG_RECOMMEND = "fragment_recommend"
private const val TAG_MATCHING = "fragment_matching"
private const val TAG_PROFILE = "fragment_profile"
private const val TAG_CHATTING = "fragment_chatting"

class BackgroundActivity : AppCompatActivity() {

    private lateinit var binding: BackgroundBinding
    private lateinit var userDB: DatabaseReference

//    override fun onDestroy() {
//        super.onDestroy()
//        Toast.makeText(this, "종료!!!", Toast.LENGTH_SHORT).show()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = BackgroundBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)
        setFragment(TAG_RECOMMEND, RecommendFragment())

        RecommendData.MBTISet.clear()
        RecommendData.hobbySet.clear()
        RecommendData.personalitySet.clear()
        RecommendData.myRadius = 500.0
        RecommendData.smokingCheck = true
        RecommendData.MBTISet = mutableSetOf(
            "ESTJ", "ESFJ", "ENFJ", "ENTJ",
            "ENTP", "ENFP", "ESFP", "ESTP",
            "INTP", "INFP", "ISFP", "ISTP",
            "ISTJ", "ISFJ", "INFJ", "INTJ")
        RecommendData.hobbySet = mutableSetOf(
            "영화", "독서", "맛집 탐방", "운동",
            "캠핑", "코딩", "카페", "등산",
            "맥주", "여행","쇼핑","산책",
            "수다", "야구 보기", "러닝", "클라이밍",
            "악기 연주", "드라이브", "재테크", "사진 찍기",
            "요리", "게임", "코인노래방", "라이딩")
        RecommendData.personalitySet = mutableSetOf(
            "적극적인", "조용한", "엉뚱한", "진지한",
            "자유로운", "즉흥적인", "꼼꼼한", "감성적인",
            "성실한", "논리적인", "침착한", "자신감있는",
            "애교있는", "어른스러운", "예의 바른", "유머러스한",
            "허세 없는", "지적인", "소심한", "쿨한",
            "또라이같은", "친절한", "계획적인", "당당한")
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.recommend -> setFragment(TAG_RECOMMEND, RecommendFragment())
                R.id.matching -> setFragment(TAG_MATCHING, MatchingFragment())
                R.id.chatting -> setFragment(TAG_CHATTING, ChattingFragment())
                R.id.profile -> setFragment(TAG_PROFILE, ProfileFragment())
            }
            true
        }
    }

    /* Fragment State 유지 함수 */
    fun setFragment(tag: String, fragment: Fragment){
        val manager: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = manager.beginTransaction()

        //트랜잭션에 tag로 전달된 fragment가 없을 경우 add
        if(manager.findFragmentByTag(tag) == null){
            ft.add(R.id.fragmentContainer, fragment, tag)
        }

        //작업이 수월하도록 manager에 add되어있는 fragment들을 변수로 할당해둠
        val recommend = manager.findFragmentByTag(TAG_RECOMMEND)
        val matching = manager.findFragmentByTag(TAG_MATCHING)
        val chatting = manager.findFragmentByTag(TAG_CHATTING)
        val profile = manager.findFragmentByTag(TAG_PROFILE)

        //모든 프래그먼트 hide
        if(recommend!=null){
            ft.hide(recommend)
        }
        if(matching!=null){
            ft.hide(matching)
        }
        if(chatting!=null){
            ft.hide(chatting)
        }
        if(profile!=null){
            ft.hide(profile)
        }

        //선택한 항목에 따라 그에 맞는 프래그먼트만 show
        if(tag == TAG_RECOMMEND){
            if(recommend!=null){
                ft.show(recommend)
            }
        }
        else if(tag == TAG_MATCHING){
            if(matching!=null){
                ft.show(matching)
            }
        }
        else if(tag == TAG_CHATTING){
            if(chatting!=null){
                ft.show(chatting)
            }
        }
        else if(tag == TAG_PROFILE){
            if(profile!=null){
                ft.show(profile)
            }
        }
        ft.commitAllowingStateLoss()
        //ft.commit()
    }

    // 프로필 편집을 하게 되는 경우
    private fun checkSetProfile() {
        var animationFlag = 0
        var profileFlag = 0

        userDB = Firebase.database.reference.child(DB_ANIMATION).child(CURRENT_USERID)
        userDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                animationFlag++
                if (animationFlag > 1) {
                    finish()
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        userDB = Firebase.database.reference.child(DB_PROFILE).child(CURRENT_USERID)
        userDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                profileFlag++
                if (profileFlag > 1) {
                    finish()
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}