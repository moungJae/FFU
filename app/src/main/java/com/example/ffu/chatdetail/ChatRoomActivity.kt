package com.example.ffu.chatdetail
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ffu.DBKey.Companion.DB_CHATS
import com.example.ffu.databinding.ActivityChatroomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.jar.Attributes
import com.example.ffu.UserInformation.Companion.PROFILE
import com.example.ffu.UserInformation.Companion.ANIMATION

class ChatRoomActivity : AppCompatActivity() {

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val binding by lazy {
        ActivityChatroomBinding.inflate(layoutInflater)
    }

    private val chatList = mutableListOf<ChatItem>()
    private val adapter = ChatItemAdapter()
    private var myChatDB: DatabaseReference? = null
    private var otherChatDB: DatabaseReference? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var pathReference : StorageReference
    private var isOpen = false // 키보드 올라왔는지 확인

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //Log.d("otherName",otherName)
        val currentID = getCurrentUserID()
        val otherID = getOtherUserID()
        val Name = PROFILE[currentID]?.nickname ?: ""
        val OtherName = getOtherUserName()
        myChatDB = Firebase.database.reference.child(DB_CHATS).child(currentID).child(otherID)
        otherChatDB = Firebase.database.reference.child(DB_CHATS).child(otherID).child(currentID)
        storage = FirebaseStorage.getInstance()
        pathReference = storage.reference

        //chatDB = Firebase.database.refer3ence.child(DB_CHATS).child("$chatKey")

        binding.activityChatroomName.text=OtherName

        setupView()
        setupAdapter()

        myChatDB?.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue(ChatItem::class.java)
                chatItem ?: return

                chatList.add(chatItem)
                adapter.submitList(chatList)
                adapter.notifyDataSetChanged()
                binding.chatRecyclerView.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })

        binding.sendButton.setOnClickListener {
            val tmpId = auth.currentUser!!.uid
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val formatted = current.format(formatter)
            val tmpMessage = binding.messageEditText.text.toString()
            if(tmpMessage!=""){
                val leftChatItem = ChatItem(
                    senderId = tmpId,
                    senderName = Name,
                    sendDate =  formatted.toString(),
                    message = tmpMessage,
                    type = ChatItem.LEFT_TYPE
                )

                val rightChatItem = ChatItem(
                    senderId = tmpId,
                    senderName =  Name,
                    sendDate =  formatted.toString(),
                    message = tmpMessage,
                    type = ChatItem.RIGHT_TYPE
                )

                myChatDB?.push()?.setValue(rightChatItem)
                otherChatDB?.push()?.setValue(leftChatItem)
                //전송 후 빈칸 만들기
                binding.messageEditText.text = null
                //binding.chatRecyclerView.setStackFromEnd()
            }

        }

        binding.activityChatroomBack.setOnClickListener{
            finish()
        }

    }

    private fun getCurrentUserID(): String{
        return auth.currentUser?.uid.orEmpty()
    }

    private fun getOtherUserID(): String {
        return intent.getStringExtra("OtherId")!!
    }
    private fun getOtherUserName(): String {
        return intent.getStringExtra("OtherName")!!
    }
    /*
    private fun getOtherUserName(): String {
        return intent.getStringExtra("OtherName").toString()
    }*/

    private fun setupView() {
        // 키보드 Open/Close 체크
        binding.activityChatroomRoot.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.activityChatroomRoot.getWindowVisibleDisplayFrame(rect)

            val rootViewHeight = binding.activityChatroomRoot.rootView.height
            val heightDiff = rootViewHeight - rect.height()
            isOpen = heightDiff > rootViewHeight * 0.25 // true == 키보드 올라감
        }
    }

    private fun setupAdapter() {
        binding.chatRecyclerView.adapter = adapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.chatRecyclerView.apply {
            viewTreeObserver.addOnScrollChangedListener {
                if (isScrollable() && !isOpen) { // 스크롤이 가능하면서 키보드가 닫힌 상태일 떄만
                    setStackFromEnd()
                    //removeOnLayoutChangeListener(onLayoutChangeListener)
                }
            }
        }
    }

}