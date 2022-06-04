package com.example.ffu

import android.util.Log
import android.widget.Toast
import com.example.ffu.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.example.ffu.utils.DBKey.Companion.DB_ANIMATION
import com.example.ffu.utils.DBKey.Companion.DB_PROFILE
import com.example.ffu.utils.DBKey.Companion.DB_RECOMMEND

class UserInformation {

    private lateinit var userDB: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val pathReference = FirebaseStorage.getInstance().reference

    companion object {
        // 객체가 처음으로 생성되었는지 확인
        var CREATE_FLAG : Boolean = false

        // 현재 유저의 uid
        var CURRENT_USERID : String = ""

        // 회원가입한 모든 유저들의 profile, animation, recommend 정보
        val PROFILE = mutableMapOf<String, Profile>()
        val ANIMATION = mutableMapOf<String, Animation>()
        val RECOMMEND = mutableMapOf<String, Recommend>()

        // 회원가입한 모든 유저들의 uid(key)를 통해 imageUri(value) 저장
        val URI = mutableMapOf<String, String>()

        // 지도 위치 권한을 등록한 모든 유저들의 정보 (위도 + 경도)
        val MAP_USER = ArrayList<String>()

        // 매칭된 모든 유저들의 uid 값을 저장
        val MATCH_USER = mutableMapOf<String, Boolean>()

        // 현재 유저가 Like 를 받은 유저들의 uid 값을 저장
        val RECEIVED_LIKE_USER = mutableMapOf<String, Boolean>()

        // 현재 유저가 Like 를 보낸 유저들의 uid 값을 저장
        val SEND_LIKE_USER = mutableMapOf<String, Boolean>()

        // 현재 유저의 history 정보를 저장
        val HISTORY = ArrayList<History>()

        // 기존에 등록된 리스너를 제거
        val LISTENER_INFO = ArrayList<Listener>()
    }

    init {
        // 현재 로그인한 유저에 대한 auth 변경
        authSetting()

        // 지도 위치 권한을 등록한 모든 유저들에 대해 단 한번만 리스너가 등록되도록 함
        // 리스너를 등록함으로써 실시간으로 변경되는 정보를 가져올 수 있음
        // CREATE_FLAG = false => 객체가 최초로 생성된 경우
        if (!CREATE_FLAG) {
            CREATE_FLAG = true
            addAllUserInformation()
        } else { // 기존에 존재하던 매칭된 유저들의 정보들 초기화
            initializeInfo()
        }
        addAllMatchUsers()
        addAllLikeUsers()
        addUserHistory()
    }

    // 로그인한 유저의 auth 세팅
    private fun authSetting() {
        auth = Firebase.auth
        CURRENT_USERID = auth.uid.toString()
    }

    // 이전에 등록된 리스너 및 map, arraylist 초기화
    private fun initializeInfo() {
        var reference : DatabaseReference
        var listener : ChildEventListener

        for (listenerInfo in LISTENER_INFO) {
            reference = listenerInfo.reference
            listener = listenerInfo.listener
            reference.removeEventListener(listener)
        }
        MATCH_USER.clear()
        RECEIVED_LIKE_USER.clear()
        SEND_LIKE_USER.clear()
        HISTORY.clear()
        LISTENER_INFO.clear()
    }

    // 지도 위치 권한을 등록한 유저의 profile 세팅
    private fun addUserProfile(userId : String) {
        userDB = Firebase.database.reference.child(DB_PROFILE).child(userId)
        userDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.getValue(Profile::class.java) != null) {
                    PROFILE[userId] = snapshot.getValue(Profile::class.java) as Profile
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // 지도 위치 권한을 등록한 유저의 animation 세팅
    private fun addUserAnimation(userId : String) {
        userDB = Firebase.database.reference.child(DB_ANIMATION).child(userId)
        userDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.getValue(Animation::class.java) != null) {
                    ANIMATION[userId] = snapshot.getValue(Animation::class.java) as Animation
                    pathReference.child("photo/$userId/real.jpg").downloadUrl.addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            URI[userId] = task.result.toString()
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // 지도 위치 권한을 등록한 유저의 위도, 경도 저장
    private fun addUserLocation(userId : String) {
        userDB = Firebase.database.reference.child(DB_RECOMMEND).child(userId)
        userDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.getValue(Recommend::class.java) != null) {
                    RECOMMEND[userId] = snapshot.getValue(Recommend::class.java) as Recommend
                }
            }
            override fun onCancelled(error: DatabaseError) { }
        })
    }

    // 지도 위치 권한을 등록한 모든 유저들에 대한 profile, animation, location 리스너를 등록
    private fun addAllUserInformation() {
        userDB = Firebase.database.reference.child(DB_PROFILE)
        userDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val userId = snapshot.key.toString()
                addUserProfile(userId)
                addUserAnimation(userId)
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val userId = snapshot.key.toString()
                PROFILE.remove(userId)
                ANIMATION.remove(userId)
                URI.remove(userId)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })

        userDB = Firebase.database.reference.child(DB_RECOMMEND)
        userDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val userId = snapshot.key.toString()
                MAP_USER.add(userId)
                addUserLocation(userId)
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val userId = snapshot.key.toString()
                MAP_USER.remove(userId)
                RECOMMEND.remove(userId)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // 현재 로그인한 유저의 매칭된 유저들의 uid 저장
    private fun addAllMatchUsers() {
        userDB = Firebase.database.reference.child("likeInfo").child(CURRENT_USERID).child("match")
        val matchListener = userDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val matchUserId = snapshot.key.toString()
                MATCH_USER[matchUserId]=true
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val matchUserId = snapshot.key.toString()
                MATCH_USER.remove(matchUserId)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                //채팅방을 나간 경우
                val matchUserId = snapshot.key.toString()
                MATCH_USER[matchUserId]=false
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
        LISTENER_INFO.add(Listener(userDB, matchListener))
    }

    // 현재 로그인한 유저에게 like 를 받거나 보낸 유저들의 uid 저장
    private fun addAllLikeUsers() {
        userDB = Firebase.database.reference.child("likeInfo").child(CURRENT_USERID).child("receivedLike")
        val receiveListener = userDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val receivedUserId = snapshot.key.toString()
                val receivedUserValue = snapshot.value
                Log.d("Id",receivedUserId)
                Log.d("value",receivedUserValue.toString())
                RECEIVED_LIKE_USER[receivedUserId] = (receivedUserValue == true)
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val receivedUserId= snapshot.key.toString()
                RECEIVED_LIKE_USER.remove(receivedUserId)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
        LISTENER_INFO.add(Listener(userDB, receiveListener))

        userDB = Firebase.database.reference.child("likeInfo").child(CURRENT_USERID).child("sendLike")
        val sendListener = userDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val sendUserId = snapshot.key.toString()
                val sendUserValue = snapshot.value
                SEND_LIKE_USER[sendUserId] = (sendUserValue == true)
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val sendUserId = snapshot.key.toString()
                SEND_LIKE_USER.remove(sendUserId)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
        LISTENER_INFO.add(Listener(userDB, sendListener))
    }

    // 현재 유저의 history 를 저장
    private fun addUserHistory() {
        userDB = Firebase.database.reference.child("history").child(CURRENT_USERID)
        val historyListener = userDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.getValue(History::class.java) != null) {
                    HISTORY.add(snapshot.getValue(History::class.java) as History)
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
        LISTENER_INFO.add(Listener(userDB, historyListener))
    }
}
