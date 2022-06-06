package com.example.ffu.chatdetail
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import android.view.animation.ScaleAnimation
import android.widget.*

import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ffu.R
import com.example.ffu.UserInformation
import com.example.ffu.UserInformation.Companion.CURRENT_USERID
import com.example.ffu.UserInformation.Companion.MATCH_USER
import com.example.ffu.utils.DBKey.Companion.DB_CHATS
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
import com.example.ffu.UserInformation.Companion.PROFILE
import com.example.ffu.UserInformation.Companion.RECEIVED_LIKE_USER
import com.example.ffu.UserInformation.Companion.SEND_LIKE_USER
import com.example.ffu.utils.ChatItem
import com.example.ffu.utils.History
import com.example.ffu.utils.RecommendArticle
import de.hdodenhof.circleimageview.CircleImageView

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
    private lateinit var pathReference: StorageReference
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

        binding.activityChatroomName.text = OtherName

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
            val tmpId = CURRENT_USERID
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val formatted = current.format(formatter)
            val tmpMessage = binding.messageEditText.text.toString()
            if (tmpMessage != "") { //""이면 보내지 않는다.
                val leftChatItem = ChatItem(
                    senderId = tmpId,
                    senderName = Name,
                    sendDate = formatted.toString(),
                    message = tmpMessage,
                    type = ChatItem.LEFT_TYPE
                )

                val rightChatItem = ChatItem(
                    senderId = tmpId,
                    senderName = Name,
                    sendDate = formatted.toString(),
                    message = tmpMessage,
                    type = ChatItem.RIGHT_TYPE
                )

                myChatDB?.push()?.setValue(rightChatItem)
                if(MATCH_USER[otherID]==true){
                    otherChatDB?.push()?.setValue(leftChatItem)
                }

                //전송 후 빈칸 만들기
                binding.messageEditText.text = null
                //binding.chatRecyclerView.setStackFromEnd()
            }

        }

        binding.activityChatroomBack.setOnClickListener {
            finish()
        }

        binding.activityChatroomMenu.setOnClickListener {
            showMenuDialog(otherID)
        }
    }

    private fun getCurrentUserID(): String {
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showMenuDialog(otherUserId : String) {
        val dialog = AlertDialog.Builder(this).create()
        val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_chattingmenu, null)
        val exitButton: Button = view.findViewById<Button>(R.id.dialog_chattingmenu_exit)
        val backButton: ImageButton = view.findViewById<ImageButton>(R.id.dialog_chattingmenu_back)

        backButton.setOnClickListener {
            dialog.dismiss()
            dialog.cancel()
        }

        exitButton.setOnClickListener{
            val matchDB = Firebase.database.reference.child("likeInfo").child(CURRENT_USERID).child("match").child(otherUserId)
            val receivedDB = Firebase.database.reference.child("likeInfo").child(CURRENT_USERID).child("receivedLike").child(otherUserId)
            val sendDB = Firebase.database.reference.child("likeInfo").child(CURRENT_USERID).child("sendLike").child(otherUserId)
            val chatDB = Firebase.database.reference.child("Chats").child(CURRENT_USERID).child(otherUserId)
            val otherMatchDB = Firebase.database.reference.child("likeInfo").child(otherUserId).child("match").child(CURRENT_USERID)

            //다른 사람이랑 match되있고 true인 경우 채팅창에 나갔다고 전송
            if(MATCH_USER[otherUserId]==true){
                val centerChatItem = ChatItem(
                    senderId = CURRENT_USERID,
                    senderName = "",
                    sendDate = "",
                    message = PROFILE[CURRENT_USERID]?.nickname+"님이 나갔습니다.",
                    type = ChatItem.CENTER_TYPE
                )
                otherChatDB?.push()?.setValue(centerChatItem)

                //val otherMatchMap = mutableMapOf<String, Boolean>()

                //otherMatchMap[CURRENT_USERID] = false
                otherMatchDB.setValue(false)
            }
            //test
            //DB제거
            matchDB.removeValue()
            receivedDB.removeValue()
            sendDB.removeValue()
            chatDB.removeValue()

            //배열에서 제거

            RECEIVED_LIKE_USER.remove(otherUserId)
            MATCH_USER.remove(otherUserId)
            SEND_LIKE_USER.remove(otherUserId)
            dialog.dismiss()
            dialog.cancel()
            finish()
        }

        //  완료 버튼 클릭 시
        /*
            like.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, isChecked ->
                compoundButton.startAnimation(
                    scaleAnimation
                )
                val receivedLikeDB = Firebase.database.reference.child("likeInfo").child(userId).child("receivedLike").child(
                    UserInformation.CURRENT_USERID
                )
                val sendLikeDB = Firebase.database.reference.child("likeInfo").child(UserInformation.CURRENT_USERID).child("sendLike").child(userId)
                receivedLikeDB.setValue("")
                sendLikeDB.setValue("")

                val userHistoryDB = Firebase.database.reference.child("history").child(UserInformation.CURRENT_USERID)
                val otherHistoryDB = Firebase.database.reference.child("history").child(userId)
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                val formatted = current.format(formatter).toString()

                val sendHistoryItem = History(
                    name = PROFILE[userId]?.nickname!!,
                    time = formatted!!,
                    type = History.SEND_TYPE
                )

                val receiveHistoryItem = History(
                    name = PROFILE[UserInformation.CURRENT_USERID]?.nickname!!,
                    time = formatted!!,
                    type = History.RECEIVE_TYPE
                )

                userHistoryDB.push().setValue(sendHistoryItem)
                otherHistoryDB.push().setValue(receiveHistoryItem)


                //dialog.dismiss()
                //dialog.cancel()

                //dialog.dismiss()
                //dialog.cancel()
            })*/

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setView(view)
        dialog.create()
        dialog.show()
    }
}
