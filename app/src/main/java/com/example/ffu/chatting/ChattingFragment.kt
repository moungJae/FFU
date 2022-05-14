package com.example.ffu.chatting

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ffu.DBKey.Companion.CHILD_CHAT
import com.example.ffu.DBKey.Companion.DB_USERS
import com.example.ffu.R
import com.example.ffu.chatdetail.ChatRoomActivity
import com.example.ffu.databinding.FragmentChattingBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ChattingFragment: Fragment(R.layout.fragment_chatting) {
    //private lateinit var articleDB: DatabaseReference
    private lateinit var userDB: DatabaseReference
    private lateinit var articleAdapter: ArticleAdapter


    private val articleList = mutableListOf<ArticleModel>()
    /*
    private val listener = object: ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

            val articleModel = snapshot.getValue(ArticleModel::class.java)
            articleModel ?: return

            articleList.add(articleModel)
            articleAdapter.submitList(articleList)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onCancelled(error: DatabaseError) {}


    }*/

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


        //initMatchedUserRecyclerView(view)
        getMatchUsers(view)
        //articleDB = Firebase.database.reference.child(DB_ARTICLES)

        articleAdapter = ArticleAdapter(onItemClicked = { articleModel ->
            if (auth.currentUser != null) {

                // 로그인을 한 상태
                if (auth.currentUser!!.uid != articleModel.Id) {
                    /*
                    val chatRoom = ChatListItem(
                        senderId = auth.currentUser!!.uid,
                        receiverId = articleModel.Id,
                        key = System.currentTimeMillis()
                    )

                    userDB.child(CHILD_CHAT)
                        .child(auth.currentUser!!.uid)
                        .push()
                        .setValue(chatRoom)

                    userDB.child(CHILD_CHAT)
                        .child(articleModel.Id)
                        .push()
                        .setValue(chatRoom)
                    */
                    // 채팅방으로 이동 하는 코드
                    context?.let {
                        val intent = Intent(it, ChatRoomActivity::class.java)
                        //chatKey 수정해야됨
                        intent.putExtra("chatKey", "123123")
                        startActivity(intent)
                    }
                    Snackbar.make(view, "채팅방이 생성되었습니다. 채팅탭에서 확인해주세요.", Snackbar.LENGTH_LONG).show()

                }
                Snackbar.make(view, "눌렸습니다", Snackbar.LENGTH_LONG).show()
            } else {
                // 로그인을 안한 상태
                Snackbar.make(view, "로그인 후 사용해주세요", Snackbar.LENGTH_LONG).show()
            }

        })
        //articleAdapter = ArticleAdapter ()
        fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding.articleRecyclerView.adapter = articleAdapter



        /*
        fragmentHomeBinding.addFloatingButton.setOnClickListener {
            context?.let {
                if (auth.currentUser != null) {
                    val intent = Intent(it, AddArticleActivity::class.java)
                    startActivity(intent)
                } else {
                    Snackbar.make(view, "로그인 후 사용해주세요", Snackbar.LENGTH_LONG).show()
                }
            }
        }*/
       // articleDB.addChildEventListener(listener)
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
        userDB.child(DB_USERS).child(userId).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val name = snapshot.child("name").value.toString()
                val gender = snapshot.child("gender").value.toString()
                val birth = snapshot.child("birth").value.toString()
                val imageUrl ="https://firebasestorage.googleapis.com/v0/b/friends-for-u-5f03a.appspot.com/o/photo%2FPIV7JHlkOYPCsujktM2JDHmLTi92%2Freal.jpg?alt=media&token=b2af19fb-2fd7-45f6-85c9-5f15786d23ad"
                //Log.d("이름",name)
                Snackbar.make(view, name, Snackbar.LENGTH_LONG).show()
                articleList.add(ArticleModel(userId,name,gender,birth,imageUrl))
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
    /*
    override fun onDestroyView() {
        super.onDestroyView()
        articleDB.removeEventListener(listener)
    }*/
}