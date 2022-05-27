package com.example.ffu.chatting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ffu.DBKey.Companion.DB_PROFILE
import com.example.ffu.R
import com.example.ffu.chatdetail.ChatRoomActivity
import com.example.ffu.databinding.FragmentChattingBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class ChattingFragment: Fragment(R.layout.fragment_chatting) {
    private lateinit var userDB: DatabaseReference
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var storage: FirebaseStorage
    private lateinit var pathReference : StorageReference
    private lateinit var userId : String
    private lateinit var otherId : String


    private val articleList = mutableListOf<ArticleModel>()

    private var binding: FragmentChattingBinding? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomeBinding = FragmentChattingBinding.bind(view)
        binding = fragmentHomeBinding
        articleList.clear()
        userDB = Firebase.database.reference
        userId = getCurrentUserID(view)
        storage = FirebaseStorage.getInstance()
        pathReference = storage.reference

        getMatchUsers(view)

        articleAdapter = ArticleAdapter(onItemClicked = { articleModel ->
            if (auth.currentUser != null) {
                // 로그인을 한 상태
                if (userId != articleModel.Id) {
                    // 채팅방으로 이동 하는 코드
                    context?.let {
                        val intent = Intent(it, ChatRoomActivity::class.java)
                        //chatKey 수정해야됨
                        intent.putExtra("OtherName", articleModel.Name)
                        intent.putExtra("OtherId", articleModel.Id)
                        startActivity(intent)
                    }
                    //Snackbar.make(view, "채팅방이 생성되었습니다. 채팅탭에서 확인해주세요.", Snackbar.LENGTH_LONG).show()

                }
                Snackbar.make(view, "눌렸습니다", Snackbar.LENGTH_LONG).show()
            } else {
                // 로그인을 안한 상태
                Snackbar.make(view, "로그인 후 사용해주세요", Snackbar.LENGTH_LONG).show()
            }

        })
        fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding.articleRecyclerView.adapter = articleAdapter
    }


    private fun getMatchUsers(view: View){

        val matchedDB = userDB.child("likedBy").child(getCurrentUserID(view)).child("match")
        //Snackbar.make(view, getCurrentUserID(view), Snackbar.LENGTH_SHORT).show()
        matchedDB.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.key?.isNotEmpty()==true){
                    getUserByKey(snapshot.key.orEmpty(),view)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun getUserByKey(userId: String,view: View){
        userDB.child(DB_PROFILE).child(userId).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("nickname").value.toString()
                val gender = snapshot.child("gender").value.toString()
                val birth = snapshot.child("birth").value.toString()
                //var imageUrl : Uri? = null
                //var imageUrl : String

                /*
                pathReference.child("photo/$userId/real.jpg").downloadUrl.addOnSuccessListener {uri->
                    imageUrl = uri.toString()
                }.addOnFailureListener{uri->imageUrl=""}*/

                /*
                 pathReference.child("photo/$userId/real.jpg").downloadUrl.addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        imageUrl = task.result.toString()
                    } else {
                        Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
                        imageUrl = ""
                    }
                }*/
                //Snackbar.make(view, imageUrl, Snackbar.LENGTH_SHORT).show()
                //articleList.add(ArticleModel(userId,name,gender,birth,imageUrl))
                articleList.add(ArticleModel(userId,name,gender,birth))
                articleAdapter.submitList(articleList)

            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun getCurrentUserID(view: View): String{
        if(auth.currentUser==null){
            Snackbar.make(view, "로그인되지 않았습니다", Snackbar.LENGTH_LONG).show()
        }
        return auth.currentUser?.uid.orEmpty()
    }
    override fun onResume() {
        super.onResume()
        articleAdapter.notifyDataSetChanged()
    }
}