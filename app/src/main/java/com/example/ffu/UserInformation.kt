package com.example.ffu

import com.example.ffu.chatdetail.ChatItem
import com.example.ffu.profile.HistoryModel
import com.example.ffu.utils.Animation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.example.ffu.utils.DBKey.Companion.DB_ANIMATION
import com.example.ffu.utils.DBKey.Companion.DB_PROFILE
import com.example.ffu.utils.DBKey.Companion.DB_RECOMMEND
import com.example.ffu.utils.Profile
import com.example.ffu.utils.Recommend

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
        var PROFILE = mutableMapOf<String, Profile>()
        var ANIMATION = mutableMapOf<String, Animation>()
        var RECOMMEND = mutableMapOf<String, Recommend>()

        // 회원가입한 모든 유저들의 uid(key)를 통해 imageUri(value) 저장
        var URI = mutableMapOf<String, String>()

        // 지도 위치 권한을 등록한 모든 유저들의 정보 (위도 + 경도)
        var MAP_USER = ArrayList<String>()

        // 매칭된 모든 유저들의 uid 값을 저장
        var MATCH_USER =  mutableMapOf<String, Boolean>()

        // 현재 유저가 Like를 받은 유저들의 uid 값을 저장
        var RECEIVEDLIKE_USER = mutableMapOf<String, Boolean>()

        // 현재 유저가 Like를 보낸 유저들의 uid 값을 저장
        var SENDLIKE_USER = mutableMapOf<String, Boolean>()

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
            addAllMatchUsers()
            addAllReceivedLikeUsers()
            addAllSendLikeUsers()
        } else {
            MATCH_USER.clear()
            RECEIVEDLIKE_USER.clear()
            addAllMatchUsers()
            addAllReceivedLikeUsers()
        }
    }

    // 로그인한 유저의 auth 세팅
    private fun authSetting() {
        auth = Firebase.auth
        CURRENT_USERID = auth.uid.toString()
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
            override fun onChildRemoved(snapshot: DataSnapshot) {}
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
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // 현재 로그인한 유저의 매칭된 유저들의 uid 저장
    private fun addAllMatchUsers() {
        userDB = Firebase.database.reference.child("likeInfo").child(CURRENT_USERID).child("match")

        userDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val matchUserId = snapshot.key.toString()
                MATCH_USER[matchUserId]=true
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val matchUserId = snapshot.key.toString()
                MATCH_USER.remove(matchUserId)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // 현재 로그인한 유저에게 like를 보낸 유저의 uid 저장
    private fun addAllReceivedLikeUsers() {
        userDB = Firebase.database.reference.child("likeInfo").child(CURRENT_USERID).child("receivedLike")
        userDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val receivedUserId = snapshot.key.toString()
                RECEIVEDLIKE_USER[receivedUserId]=true
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val receivedUserId= snapshot.key.toString()
                RECEIVEDLIKE_USER.remove(receivedUserId)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // 현재 로그인한 유저에게 like를 보낸 유저의 uid 저장
    private fun addAllSendLikeUsers() {
        userDB = Firebase.database.reference.child("likeInfo").child(CURRENT_USERID).child("sendLike")
        userDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val sendUserId = snapshot.key.toString()
                SENDLIKE_USER[sendUserId]=true
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val sendUserId = snapshot.key.toString()
                SENDLIKE_USER.remove(sendUserId)
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        })
    }


}
