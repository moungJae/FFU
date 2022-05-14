package com.example.ffu.profile

import android.app.Activity
import android.app.job.JobInfo
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ffu.CheckPhoneNumActivity
import com.example.ffu.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.protobuf.Internal
import java.io.*
import java.net.Socket
import java.text.SimpleDateFormat

class ProfileSettingActivity : AppCompatActivity() {

    private lateinit var userDB: DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var storage: FirebaseStorage

    private val ip = "59.9.212.155"
    private val port = 30000
    private lateinit var socket: Socket
    private lateinit var dos: DataOutputStream

    private var personalitiesAdd = false
    private var hobbiesAdd = false
    private val personalities = ArrayList<String>()
    private val hobbies = ArrayList<String>()
    private var mbti: String = ""
    private var religion: String = ""

    private lateinit var nickname : String
    private lateinit var job : String
    private lateinit var introMe : String
    private lateinit var smoke : String
    private lateinit var drinking : String
    private var photoCheck : Boolean = false

    private lateinit var progressBar : ProgressBar
    private lateinit var handler : Handler

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profilesetting)

        initializeInformation()
        setMbti()
        setPersonality()
        setHobby()
        setReligion()
        setPhoto()
        saveProfile()
    }

    private fun initializeInformation() {
        val profile = mutableMapOf<String, Any>()

        auth = Firebase.auth
        storage = FirebaseStorage.getInstance()
        progressBar = findViewById<ProgressBar>(R.id.profilesetting_progressBar)
        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                progressBar.visibility = View.INVISIBLE
            }
        }

        userDB = Firebase.database.reference.child("profile").child(auth.uid.toString())
        profile["photo"] = "false"
        userDB.updateChildren(profile)
        userDB.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    when (ds.key.toString()) {
                        "nickname" -> findViewById<EditText>(R.id.profilesetting_inputnickname).setText(ds.value.toString())
                        "job" -> findViewById<EditText>(R.id.profilesetting_inputjob).setText(ds.value.toString())
                        "introMe" -> findViewById<EditText>(R.id.profilesetting_inputIntroduce).setText(ds.value.toString())
                        "smoke" -> when(ds.value.toString()) {
                            "비흡연" -> findViewById<RadioButton>(R.id.profilesetting_smoke_radioButton1).isChecked = true
                            "흡연" -> findViewById<RadioButton>(R.id.profilesetting_smoke_radioButton2).isChecked = true
                            "가끔" -> findViewById<RadioButton>(R.id.profilesetting_smoke_radioButton3).isChecked = true
                        }
                        "drinking" -> when(ds.value.toString()) {
                            "안함" -> findViewById<RadioButton>(R.id.profilesetting_drink_radioButton1).isChecked = true
                            "자주" -> findViewById<RadioButton>(R.id.profilesetting_drink_radioButton2).isChecked = true
                            "가끔" -> findViewById<RadioButton>(R.id.profilesetting_drink_radioButton3).isChecked = true
                        }
                        "mbti" -> mbti = ds.value.toString()
                        "religion" -> religion = ds.value.toString()
                        "personality" -> {
                            val personalityList = ds.value.toString().split("/")
                            for (personality in personalityList) {
                                if (!personalitiesAdd)
                                    personalities.add(personality)
                            }
                            personalitiesAdd = true
                        }
                        "hobby" -> {
                            val hobbyList = ds.value.toString().split("/")
                            for (hobby in hobbyList) {
                                if (!hobbiesAdd)
                                    hobbies.add(hobby)
                            }
                            hobbiesAdd = true
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setMbti() {
        val mbtiButton = findViewById<Button>(R.id.profilesetting_mbtiButton)

        mbtiButton.setOnClickListener {
            val items = arrayOf("ESTJ", "ESFJ", "ENFJ", "ENTJ",
                "ENTP", "ENFP", "ESFP", "ESTP",
                "INTP", "INFP", "ISFP", "ISTP",
                "ISTJ", "ISFJ", "INFJ", "INTJ")
            var checkedItem = -1
            var i = 0

            for (item in items) {
                if (mbti.equals(item)) {
                   checkedItem = i
                   break
                }
                i++
            }

            val builder = AlertDialog.Builder(this)
                .setTitle("자신의 MBTI를 하나 선택해주세요")
                .setSingleChoiceItems(items, checkedItem) { dialog, which ->
                    mbti = items[which]
                }
                .setPositiveButton("선택") {dialog, which -> }
                .show()
        }
    }

    private fun setPersonality() {
        val personalityButton = findViewById<Button>(R.id.profilesetting_personalButton)

        personalityButton.setOnClickListener {
            val items = arrayOf("활발한", "조용한", "엉뚱한", "진지한",
                "자유로운", "즉흥적인", "꼼꼼한", "감성적인", "성실한",
                "논리적인", "침착한", "자신감이 넘치는", "애교가 넘치는")
            val checkedItems = booleanArrayOf(false, false, false, false,
                false, false, false, false, false,
                false, false, false, false)
            val selectedItemIndex = ArrayList<Int>()

            for (personality in personalities) {
                var i = 0
                for (item in items) {
                    if (personality.equals(item)) {
                        checkedItems[i] = true
                        selectedItemIndex.add(i)
                        break
                    }
                    i++
                }
            }
            val builder = AlertDialog.Builder(this)
                .setTitle("자신의 성격을 여러개 선택해주세요")
                .setMultiChoiceItems(items, checkedItems){ dialogInterface: DialogInterface, i: Int, b: Boolean ->
                    if(b){
                        checkedItems[i] = true
                        selectedItemIndex.add(i)
                    } else if(selectedItemIndex.contains(i)){
                        checkedItems[i] = false
                        selectedItemIndex.remove(i)
                    }
                }.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
                    personalities.clear()
                    for(j in selectedItemIndex) {
                        personalities.add(items[j])
                    }
                }
                .show()
        }
    }

    private fun setReligion() {
        val religionButton = findViewById<Button>(R.id.profilesetting_religionButton)

        religionButton.setOnClickListener {
            val items = arrayOf("무교", "기독교", "불교", "천주교",
                "이슬람교", "힌두교", "개신교", "기타")
            var checkedItem = -1
            var i = 0

            for (item in items) {
                if (religion.equals(item)) {
                    checkedItem = i
                    break
                }
                i++
            }
            val builder = AlertDialog.Builder(this)
                .setTitle("자신의 종교를 하나 선택해주세요")
                .setSingleChoiceItems(items, checkedItem) { dialog, which ->
                    religion = items[which]
                }
                .setPositiveButton("선택") {dialog, which -> }
                .show()
        }
    }

    private fun setHobby() {
        val hobbyButton = findViewById<Button>(R.id.profilesetting_hobbyButton)

        hobbyButton.setOnClickListener {
            val items = arrayOf("영화보기", "독서하기", "맛집탐방", "운동하기",
                "캠핑하기", "코딩하기", "카페가기", "등산하기", "춤추기",
                "여행하기","쇼핑하기","산책하기","수다떨기","잠자기",
                "바둑하기", "수영하기", "악기연주", "그림그리기",
                "글쓰기", "노래하기", "요리하기", "게임하기")
            val checkedItems = booleanArrayOf(false, false, false, false, false,
                false, false, false, false, false, false,
                false, false, false, false, false, false,
                false, false, false, false, false)
            val selectedItemIndex = ArrayList<Int>()

            for (hobby in hobbies) {
                var i = 0
                for (item in items) {
                    if (hobby.equals(item)) {
                        checkedItems[i] = true
                        selectedItemIndex.add(i)
                        break
                    }
                    i++
                }
            }
            val builder = AlertDialog.Builder(this)
                .setTitle("자신의 취미를 여러개 선택해주세요")
                .setMultiChoiceItems(items, checkedItems){ dialogInterface: DialogInterface, i: Int, b: Boolean ->
                    if(b){
                        checkedItems[i] = true
                        selectedItemIndex.add(i)
                    } else if(selectedItemIndex.contains(i)) {
                        checkedItems[i] = false
                        selectedItemIndex.remove(i)
                    }
                }.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
                    hobbies.clear()
                    for(j in selectedItemIndex) {
                        hobbies.add(items[j])
                    }
                }
                .show()
        }
    }

    private fun characterization() {
        val thread : Thread = object : Thread() {
            override fun run() {
                // connection request to Server
                try {
                    socket = Socket(ip, port)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                // Write Buffer
                try {
                    dos = DataOutputStream(socket!!.getOutputStream())
                    dos.writeUTF(auth?.uid.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        thread.start()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setPhoto() {
        val photoButton = findViewById<Button>(R.id.profilesetting_imageAddButton)
        val imagesRef = storage!!.reference
            .child("photo/" + auth?.uid.toString() + "/real.jpg")

        val getFromAlbumResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data // 선택한 이미지의 주소
                // 이미지 파일 읽어와서 설정하기
                if (uri != null) {
                    // 사진 가져오기
                    val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                    // 사진의 회전 정보 가져오기
                    val orientation = getOrientationOfImage(uri).toFloat()
                    // 이미지 회전하기
                    val newBitmap = getRotatedBitmap(bitmap, orientation)
                    imagesRef.putFile(uri).addOnSuccessListener {
                        characterization()
                        photoCheck = true
                        Toast.makeText(this, "사진 등록을 성공했습니다.", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "사진 등록을 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        photoButton.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            getFromAlbumResultLauncher.launch(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getOrientationOfImage(uri: Uri): Int {
        // uri -> inputStream
        val inputStream = contentResolver.openInputStream(uri)
        val exif: ExifInterface? = try {
            ExifInterface(inputStream!!)
        } catch (e: IOException) {
            e.printStackTrace()
            return -1
        }
        inputStream.close()

        // 회전된 각도 알아내기
        val orientation = exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        if (orientation != -1) {
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> return 90
                ExifInterface.ORIENTATION_ROTATE_180 -> return 180
                ExifInterface.ORIENTATION_ROTATE_270 -> return 270
            }
        }
        return 0
    }

    @Throws(Exception::class)
    private fun getRotatedBitmap(bitmap: Bitmap?, degrees: Float): Bitmap? {
        if (bitmap == null) return null
        if (degrees == 0F) return bitmap
        val m = Matrix()
        m.setRotate(degrees, bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
    }

    private fun checkInformation() : Boolean {
        val smokeGroup = findViewById<RadioGroup>(R.id.profilesetting_smoke_radioGroup)
        val drinkingGroup = findViewById<RadioGroup>(R.id.profilesetting_drink_radioGroup)

        nickname = findViewById<EditText>(R.id.profilesetting_inputnickname).text.toString()
        job = findViewById<EditText>(R.id.profilesetting_inputjob).text.toString()
        introMe = findViewById<EditText>(R.id.profilesetting_inputIntroduce).text.toString()

        smoke = when (smokeGroup.checkedRadioButtonId) {
            R.id.profilesetting_smoke_radioButton1 -> "비흡연"
            R.id.profilesetting_smoke_radioButton2 -> "흡연"
            R.id.profilesetting_smoke_radioButton3 -> "가끔"
            else -> ""
        }
        drinking = when (drinkingGroup.checkedRadioButtonId) {
            R.id.profilesetting_drink_radioButton1 -> "안함"
            R.id.profilesetting_drink_radioButton2 -> "자주"
            R.id.profilesetting_drink_radioButton3 -> "가끔"
            else -> ""
        }

        if (nickname.isEmpty() || job.isEmpty() || introMe.isEmpty() || smoke.isEmpty()
            || drinking.isEmpty() || mbti.isEmpty() || religion.isEmpty()
            || personalities.size == 0 || hobbies.size == 0 || !photoCheck)
            return false
        return true
    }

    private fun insertProfileInformation() {
        val profile = mutableMapOf<String, Any>()
        var personality : String =  ""
        var hobby : String = ""

        for (data in personalities) {
            personality += data
            if (!data.equals(personalities.last())) {
                personality += "/"
            }
        }
        for (data in hobbies) {
            hobby += data
            if (!data.equals(hobbies.last())) {
                hobby += "/"
            }
        }

        userDB = Firebase.database.reference.child("profile").child(auth?.uid.toString())
        profile["nickname"] = nickname
        profile["job"] = job
        profile["introMe"] = introMe
        profile["smoke"] = smoke
        profile["drinking"] = drinking
        profile["mbti"] = mbti
        profile["religion"] = religion
        profile["personality"] = personality
        profile["hobby"] = hobby
        userDB.updateChildren(profile)
    }

    private fun saveProfile() {
        val saveButton = findViewById<Button>(R.id.profilesetting_saveButton)
        var insertCheck : Boolean = false
        var i = 0

        saveButton.setOnClickListener {
            if (checkInformation()) {
                progressBar.visibility = View.VISIBLE
                insertProfileInformation()
                Thread (Runnable {
                    while (i < 30) {
                        userDB = Firebase.database.getReference("profile").child(auth?.uid.toString())
                        userDB.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (ds in snapshot.children) {
                                    if (ds.key.toString().equals("photo") && ds.value.toString().equals("true")) {
                                        insertCheck = true
                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
                        Thread.sleep(1000)
                        if (insertCheck)
                            break
                        i++
                    }
                    if (insertCheck) {
                        finish()
                    }
                    else {
                        handler.handleMessage(Message())
                    }
                }).start()
            }
            else {
                Toast.makeText(this, "정보를 완벽하게 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


