package com.example.ffu

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UserInformation {

    private lateinit var userDB: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val pathReference = FirebaseStorage.getInstance().reference

    companion object {
        // 현재 유저의 회원가입 여부
        var JOIN : Boolean = false

        // 현재 유저 및 매칭된 유저들의 profile 정보
        var NICKNAME = mutableMapOf<String, String>()
        var INTROME = mutableMapOf<String, String>()
        var AGE = mutableMapOf<String, String>()
        var BIRTH = mutableMapOf<String, String>()
        var DRINKING = mutableMapOf<String, String>()
        var GENDER = mutableMapOf<String, String>()
        var JOB = mutableMapOf<String, String>()
        var MBTI = mutableMapOf<String, String>()
        var RELIGION = mutableMapOf<String, String>()
        var SMOKE = mutableMapOf<String, String>()
        var TEL = mutableMapOf<String, String>()
        var PERSONALITIES = mutableMapOf<String, ArrayList<String>>()
        var HOBBIES = mutableMapOf<String, ArrayList<String>>()

        // 현재 유저 및 매칭된 유저들의 animation 사진 접근 권한
        var PERMISSION = mutableMapOf<String, Boolean>()

        // 매칭된 유저들의 uid(key)를 통해 imageUri(value) 저장
        var URI = mutableMapOf<String, String>()

        // 매칭된 유저들의 uid 값을 저장
        var MATCH_USER = ArrayList<String>()

        // 등록된 모든 Listener 정보
        var ALL_LISTENER_INFO = ArrayList<ListenerInfo>()

        // 지도 위치 권한을 등록한 모든 유저들의 정보 (위도 + 경도)
        var MAP_USER = ArrayList<String>()
        var LATITUDE = mutableMapOf<String, Double>()
        val LONGITUDE = mutableMapOf<String, Double>()
    }

    init {
        // 지도 위치 권한을 등록한 모든 유저들에 대해 단 한번만 리스너가 등록되도록 함
        // 리스너를 등록함으로써 실시간으로 변경되는 위도 및 경도를 감지할 수 있음
        // TEL.size 값이 0인 경우 => 객체가 최초로 생성된 경우
        if (TEL.size == 0) {
            addAllUserLocation()
        } else {
            // 등록된 모든 정보를 초기화
            initializeAllInformation()
        }
        // 현재 로그인한 유저에 대한 auth 변경
        authSetting()
        // 현재 로그인한 유저의 정보 저장
        addCurrentUserInformation()
        // 현재 로그인한 유저와 매칭된 모든 유저들의 정보 저장
        addAllMatchUserInformation()
    }

    // 해당 userId 에 대한 map 의 key 값을 제거
    private fun removeInformation(userId : String) {
        NICKNAME.remove(userId)
        INTROME.remove(userId)
        AGE.remove(userId)
        BIRTH.remove(userId)
        DRINKING.remove(userId)
        GENDER.remove(userId)
        JOB.remove(userId)
        MBTI.remove(userId)
        RELIGION.remove(userId)
        SMOKE.remove(userId)
        TEL.remove(userId)
        PERSONALITIES.remove(userId)
        HOBBIES.remove(userId)
        PERMISSION.remove(userId)
        HOBBIES.remove(userId)
        PERMISSION.remove(userId)
        URI.remove(userId)
    }

    // 해당 userId 에 대한 리스너를 제거 (특정 매칭된 유저를 제거하고 싶을때)
    private fun removeListener(userId : String) {
        var reference : DatabaseReference
        var listener : ValueEventListener

        removeInformation(userId)
        for (listenerInfo in ALL_LISTENER_INFO) {
            if (userId.equals(listenerInfo.userId)) {
                reference = listenerInfo.reference
                listener = listenerInfo.listener
                reference.removeEventListener(listener)
                ALL_LISTENER_INFO.remove(listenerInfo)
            }
        }
    }

    // 이전에 등록된 모든 리스너를 지워서 리스너 오버헤드를 줄이도록 함
    private fun removeAllListener() {
        var reference : DatabaseReference
        var listener : ValueEventListener

        for (listenerInfo in ALL_LISTENER_INFO) {
            reference = listenerInfo.reference
            listener = listenerInfo.listener
            reference.removeEventListener(listener)
        }
    }

    // 재로그인 or 다른 번호로 로그인한 경우 데이터 중첩을 방지하기 위함
    private fun initializeAllInformation() {
        removeAllListener()
        JOIN = false
        NICKNAME.clear()
        INTROME.clear()
        AGE.clear()
        BIRTH.clear()
        DRINKING.clear()
        GENDER.clear()
        JOB.clear()
        MBTI.clear()
        RELIGION.clear()
        SMOKE.clear()
        TEL.clear()
        PERSONALITIES.clear()
        HOBBIES.clear()
        PERMISSION.clear()
        HOBBIES.clear()
        PERMISSION.clear()
        URI.clear()
        MATCH_USER.clear()
        ALL_LISTENER_INFO.clear()
    }

    // 로그인한 유저의 auth 세팅
    private fun authSetting() {
        auth = Firebase.auth
    }

    // 해당 유저가 회원가입이 된 경우(join = true)
    private fun setUserJoin(join : String) :Boolean {
        return join.equals("true")
    }

    // 취미를 '/' 으로 split 하여 리스트 형태로 반환
    private fun setHobbies(hobbies : String) : ArrayList<String> {
        val resultList = ArrayList<String>()
        val hobbyList = hobbies.split("/")

        for (hobby in hobbyList) {
            resultList.add(hobby)
        }
        return resultList
    }

    // 성격을 '/' 으로 split 하여 리스트 형태로 반환
    private fun setPersonalities(personalities : String) : ArrayList<String> {
        val resultList = ArrayList<String>()
        val personalityList = personalities.split("/")

        for (personality in personalityList) {
            resultList.add(personality)
        }
        return resultList
    }

    private fun addUserLocation(userId : String) {
        userDB = Firebase.database.reference.child("recommend").child(userId)
        userDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    if (ds.key.toString().equals("latitude")) {
                        LATITUDE[userId] = ds.value.toString().toDouble()
                    } else if (ds.key.toString().equals("longitude")) {
                        LONGITUDE[userId] = ds.value.toString().toDouble()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // 유저들의 위도, 경도의 realtime 변경을 리스너를 등록하여 반영되도록 함
    private fun addAllUserLocation() {
        userDB = Firebase.database.reference.child("recommend")
        userDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val userId = ds.key.toString()
                    if (LONGITUDE[userId] == null) {
                        MAP_USER.add(userId)
                        addUserLocation(userId)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // 로그인한 유저 및 매칭 유저들의 profile 세팅
    private fun addUserProfile(userId : String) {
        userDB = Firebase.database.reference.child("profile").child(userId)
        val profileListener = userDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    when(ds.key.toString()) {
                        "age" -> AGE[userId] = ds.value.toString()
                        "birth" -> BIRTH[userId] = ds.value.toString()
                        "drinking" -> DRINKING[userId] = ds.value.toString()
                        "gender" -> GENDER[userId] = ds.value.toString()
                        "introMe" -> INTROME[userId] = ds.value.toString()
                        "job" -> JOB[userId] = ds.value.toString()
                        "mbti" -> MBTI[userId] = ds.value.toString()
                        "nickname" -> NICKNAME[userId] = ds.value.toString()
                        "religion" -> RELIGION[userId] = ds.value.toString()
                        "smoke" -> SMOKE[userId] = ds.value.toString()
                        "tel" -> TEL[userId] = ds.value.toString()
                        "join" -> JOIN = setUserJoin(ds.value.toString())
                        "hobby" -> HOBBIES[userId] = setHobbies(ds.value.toString())
                        "personality" -> PERSONALITIES[userId] = setPersonalities(ds.value.toString())
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        ALL_LISTENER_INFO.add(ListenerInfo(userId, userDB, profileListener))
    }

    // 로그인한 유저 및 매칭 유저들의 animation 세팅
    private fun addUserAnimation(userId : String) {
        userDB = Firebase.database.reference.child("animation").child(userId)
        val animationListener = userDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                PERMISSION[userId] = false
                for (ds in snapshot.children) {
                    if (ds.key.toString().equals("permission") && ds.value.toString().equals("true")) {
                        PERMISSION[userId] = true
                    }
                }
                pathReference.child("photo/$userId/real.jpg").downloadUrl.addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        URI[userId] = task.result.toString()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        ALL_LISTENER_INFO.add(ListenerInfo(userId, userDB, animationListener))
    }

    // 특정 매칭 유저의 정보를 제거
    private fun deleteMatchUserInformation(userId : String) {
        for (user in MATCH_USER) {
            if (userId.equals(user)) {
                removeListener(userId)
                MATCH_USER.remove(userId)
                break
            }
        }
    }

    // 매칭된 모든 유저들의 정보를 제거
    private fun deleteAllMatchUserInformation() {
        for (user in MATCH_USER) {
            removeListener(user)
        }
        MATCH_USER.clear()
    }

    // 현재 로그인한 유저의 profile 및 animation 세팅
    private fun addCurrentUserInformation() {
        val currentUserId = auth.uid.toString()
        MATCH_USER.add(currentUserId)
        addUserProfile(currentUserId)
        addUserAnimation(currentUserId)
    }

    // 현재 로그인한 유저의 매칭 유저들의 profile 및 animation 세팅
    private fun addAllMatchUserInformation() {
        val currentUserId = auth.uid.toString()
        userDB = Firebase.database.reference.child("likedBy").child(currentUserId).child("match")
        val likedByListener = userDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val matchUserId = ds.key.toString()
                    if (TEL[matchUserId] == null) {
                        MATCH_USER.add(matchUserId)
                        addUserProfile(matchUserId)
                        addUserAnimation(matchUserId)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        ALL_LISTENER_INFO.add(ListenerInfo("", userDB, likedByListener))
    }
}