package com.example.ffu.profile

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ffu.BackgroundActivity
import com.example.ffu.R
import com.example.ffu.UserInformation
import com.example.ffu.UserInformation.Companion.ANIMATION
import com.example.ffu.UserInformation.Companion.PROFILE
import com.example.ffu.UserInformation.Companion.URI
import com.example.ffu.utils.Animation
import com.example.ffu.utils.DBKey.Companion.DB_ANIMATION
import com.example.ffu.utils.DBKey.Companion.DB_PROFILE
import com.example.ffu.utils.Profile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.metagalactic.dotprogressbar.DotProgressBar
import de.hdodenhof.circleimageview.CircleImageView
import java.io.*
import java.net.Socket
import com.example.ffu.UserInformation.Companion.CURRENT_USERID
import java.text.SimpleDateFormat


class ProfileSettingActivity : AppCompatActivity() {

    private lateinit var userDB: DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var storage: FirebaseStorage

    private val ip = "59.9.212.155"
    private val port = 30000
    private lateinit var socket: Socket
    private lateinit var dos: DataOutputStream

    private var personalities = ArrayList<String>()
    private var hobbies = ArrayList<String>()
    private var mbti: String = ""
    private var religion: String = ""
    private var birth: String = ""
    private var gender: String = ""

    private lateinit var nickname : String
    private lateinit var job : String
    private lateinit var introMe : String
    private lateinit var smoke : String
    private lateinit var drinking : String
    private lateinit var progressBar : DotProgressBar

    private lateinit var editTextArray : Array<EditText>
    private lateinit var radioButtonArray : Array<RadioButton>
    private lateinit var buttonArray : Array<Button>


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_setting)

        initializeInformation()
        setEditableArray()
        setMbti()
        setPersonality()
        setHobby()
        setReligion()
        setPhoto()
        saveProfile()
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

    // 이전에 기입했던 정보들이 editText 에 남도록 설정
    private fun initializeInformation() {
        val image = findViewById<ImageView>(R.id.profile_setting_imageAddButton)

        auth = Firebase.auth
        storage = FirebaseStorage.getInstance()
        progressBar = findViewById<DotProgressBar>(R.id.profile_setting_progressbar)

        if (intent.hasExtra("birth")) {
            birth = intent.getStringExtra("birth").toString()
            gender = intent.getStringExtra("gender").toString()
        }

        findViewById<EditText>(R.id.profile_setting_inputNickname).setText(PROFILE[CURRENT_USERID]?.nickname)
        findViewById<EditText>(R.id.profile_setting_inputJob).setText(PROFILE[CURRENT_USERID]?.job)
        findViewById<EditText>(R.id.profile_setting_inputIntroduce).setText(PROFILE[CURRENT_USERID]?.introMe)

        when(PROFILE[CURRENT_USERID]?.smoke) {
            "비흡연" -> findViewById<RadioButton>(R.id.profile_setting_smoke_radioButton1).isChecked = true
            "흡연" -> findViewById<RadioButton>(R.id.profile_setting_smoke_radioButton2).isChecked = true
        }
        when(PROFILE[CURRENT_USERID]?.drinking) {
            "비음주" -> findViewById<RadioButton>(R.id.profile_setting_drink_radioButton1).isChecked = true
            "음주" -> findViewById<RadioButton>(R.id.profile_setting_drink_radioButton2).isChecked = true
        }

        mbti = PROFILE[CURRENT_USERID]?.mbti ?: ""
        religion = PROFILE[CURRENT_USERID]?.religion ?: ""
        personalities = setPersonalities(PROFILE[CURRENT_USERID]?.personality ?: "")
        hobbies = setHobbies(PROFILE[CURRENT_USERID]?.hobby ?: "")

        if (URI[CURRENT_USERID] != null) {
            Glide.with(this@ProfileSettingActivity)
                .load(URI[CURRENT_USERID])
                .into(image)
        }
    }

    private fun setEditableArray() {
        editTextArray = arrayOf(findViewById<EditText>(R.id.profile_setting_inputNickname),
            findViewById<EditText>(R.id.profile_setting_inputJob),
            findViewById<EditText>(R.id.profile_setting_inputIntroduce),
            findViewById<EditText>(R.id.profile_setting_inputNickname))

        radioButtonArray = arrayOf(findViewById<RadioButton>(R.id.profile_setting_smoke_radioButton1),
            findViewById<RadioButton>(R.id.profile_setting_smoke_radioButton2),
            findViewById<RadioButton>(R.id.profile_setting_drink_radioButton1),
            findViewById<RadioButton>(R.id.profile_setting_drink_radioButton2))


        buttonArray = arrayOf(findViewById<Button>(R.id.profile_setting_mbtiButton),
            findViewById<Button>(R.id.profile_setting_personalButton),
            findViewById<Button>(R.id.profile_setting_religionButton),
            findViewById<Button>(R.id.profile_setting_hobbyButton),
            findViewById<Button>(R.id.profile_setting_saveButton))
    }

    private fun setMbti() {
        val mbtiButton = findViewById<Button>(R.id.profile_setting_mbtiButton)
        val mbtiTextView = findViewById<TextView>(R.id.mbti_textView)

        mbtiTextView.setText(mbti.toString())
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

            AlertDialog.Builder(this)
                .setTitle("자신의 MBTI를 하나 선택해주세요")
                .setSingleChoiceItems(items, checkedItem) { dialog, which ->
                    mbti = items[which]
                    mbtiTextView.setText(mbti.toString())
                }
                .setPositiveButton("선택") {dialog, which -> }
                .show()
        }
    }

    // 3개 이상 출력 시 줄바꿈 처리
    private fun changeLine(List : List<String>) : String{
        val splitString = List.joinToString(",", "", "", 6).split(",")
        var result = ""
        var i = 0

        for (tmp in splitString) {
            if (i == 3) {
                if (tmp.equals(splitString.last())) {
                    result += "\n" + tmp
                } else {
                    result += "\n" + tmp + ","
                }
            } else {
                if (tmp.equals(splitString.last())) {
                    result += tmp
                } else {
                    result += tmp + ","
                }
            }
            i++
        }
        return result
    }

    private fun setPersonality() {
        val personalityButton = findViewById<Button>(R.id.profile_setting_personalButton)
        val personalityTextView = findViewById<TextView>(R.id.personality_textView)
        val personality = changeLine(personalities)

        personalityTextView.setText(personality)
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

            AlertDialog.Builder(this)
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
                    val personality = changeLine(personalities)
                    personalityTextView.setText(personality)

                }
                .show()
        }

    }

    private fun setReligion() {
        val religionButton = findViewById<Button>(R.id.profile_setting_religionButton)
        val religionTextView = findViewById<TextView>(R.id.religion_textView)

        religionTextView.setText(religion.toString())

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

            AlertDialog.Builder(this)
                .setTitle("자신의 종교를 하나 선택해주세요")
                .setSingleChoiceItems(items, checkedItem) { dialog, which ->
                    religion = items[which]
                    religionTextView.setText(religion.toString())
                }
                .setPositiveButton("선택") {dialog, which -> }
                .show()
        }
    }

    private fun setHobby() {
        val hobbyButton = findViewById<Button>(R.id.profile_setting_hobbyButton)
        val hobbyTextView = findViewById<TextView>(R.id.hobby_textView)
        var hobby = changeLine(hobbies)

        hobbyTextView.setText(hobby)

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

            AlertDialog.Builder(this)
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
                    // 3개 이상 출력되면 줄 바꿈
                    var hobby = changeLine(hobbies)
                    hobbyTextView.setText(hobby)
                    //
                }
                .show()
        }
    }

    // 프로필 변환이 끝날 경우
    private fun setAllEnable() {
        for (editText in editTextArray) {
            editText.isEnabled = true
        }
        for (radioButton in radioButtonArray) {
            radioButton.isEnabled = true
        }
        for (button in buttonArray) {
            button.isEnabled = true
        }
    }

    // 프로필 변환이 시작될 때
    private fun setAllDisable() {
        for (editText in editTextArray) {
            editText.isEnabled = false
        }
        for (radioButton in radioButtonArray) {
            radioButton.isEnabled = false
        }
        for (button in buttonArray) {
            button.isEnabled = false
        }
    }

    // animation 사진으로 변환해주는 python server 와 socket communication 을 한다.
    // animation 의 총 3가지 flag 를 통해 response 한다.
    // (1) request : server 에게 요청하기 전 false, server 측에게 응답받으면 true
    // (2) person : server 에서 인물 사진임을 판단하면 true, 그 외엔 false
    // (3) permission : 프로필을 변환하고 완벽하게 저장할 경우 true, 그 외엔 false
    //     프로필을 변환을 한 후에 비정상적으로 program 을 종료하거나 뒤로 이동할 경우
    //     permission 값이 false 가 되어 의도치 않은 사진(실물 사진)이 나타나는 것을 방지
    private fun characterization() {
        val transformText = findViewById<TextView>(R.id.profile_setting_transform_text)
        val image = findViewById<CircleImageView>(R.id.profile_setting_imageAddButton)

        transformText.text = "사진 변환중..."
        Thread {
            val animation = mutableMapOf<String, Any>()

            userDB = Firebase.database.reference.child(DB_ANIMATION).child(CURRENT_USERID)
            animation["request"] = false
            animation["person"] = false
            animation["permission"] = false
            userDB.updateChildren(animation)

            // 1. server 에게 connection request 및 buffer write
            try {
                socket = Socket(ip, port)
                dos = DataOutputStream(socket.getOutputStream())
                dos.writeUTF(CURRENT_USERID)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            // 2. server 에서 요청(request = true)이 올 때 까지 대기
            while (!ANIMATION[CURRENT_USERID]!!.request) {
                Thread.sleep(100)
            }

            // 3. server 에서 요청이 온 경우, person flag 에 따라 변환이 되었는지 판단
            Handler(Looper.getMainLooper()).post {
                setAllEnable()
                transformText.text = ""
                progressBar.visibility = View.INVISIBLE
                Glide.with(this@ProfileSettingActivity)
                    .load(URI[CURRENT_USERID])
                    .into(image)
                if (ANIMATION[CURRENT_USERID]!!.person) {
                    Toast.makeText(this@ProfileSettingActivity,"애니메이션 변환 완료!",Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProfileSettingActivity,"인물사진을 넣어주세요!",Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setPhoto() {
        val photoButton = findViewById<Button>(R.id.profile_setting_image_change_button)
        val imagesRef = storage.reference
            .child("photo/" + CURRENT_USERID + "/real.jpg")

        val getFromAlbumResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            setAllDisable()
            progressBar.visibility = View.VISIBLE
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
                    }
                }
            } else {
                setAllEnable()
                progressBar.visibility = View.INVISIBLE
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
        val inputStream = contentResolver.openInputStream(uri)
        val exif: ExifInterface? = try {
            ExifInterface(inputStream!!)
        } catch (e: IOException) {
            e.printStackTrace()
            return -1
        }
        inputStream.close()

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

    // 모든 정보를 입력했는지를 확인
    private fun checkInformation() : Boolean {
        val smokeGroup = findViewById<RadioGroup>(R.id.profile_setting_smoke_radioGroup)
        val drinkingGroup = findViewById<RadioGroup>(R.id.profile_setting_drink_radioGroup)

        nickname = findViewById<EditText>(R.id.profile_setting_inputNickname).text.toString()
        job = findViewById<EditText>(R.id.profile_setting_inputJob).text.toString()
        introMe = findViewById<EditText>(R.id.profile_setting_inputIntroduce).text.toString()


        smoke = when (smokeGroup.checkedRadioButtonId) {
            R.id.profile_setting_smoke_radioButton1 -> "비흡연"
            R.id.profile_setting_smoke_radioButton2 -> "흡연"
            else -> ""
        }
        drinking = when (drinkingGroup.checkedRadioButtonId) {
            R.id.profile_setting_drink_radioButton1 -> "비음주"
            R.id.profile_setting_drink_radioButton2 -> "음주"
            else -> ""
        }

        if (nickname.isEmpty() || job.isEmpty() || introMe.isEmpty() || smoke.isEmpty()
            || drinking.isEmpty() || mbti.isEmpty() || religion.isEmpty()
            || personalities.size == 0 || hobbies.size == 0 || !ANIMATION[CURRENT_USERID]!!.person)
            return false
        return true
    }

    // firebase realtime database 에 입력한 모든 정보들을 insertion
    private fun insertProfileInformation() {
        val profile : Profile
        val animation : Animation
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

        if (birth.length > 0) {
            val age = SimpleDateFormat("yyyy-MM-dd-hh-mm")
                .format(System.currentTimeMillis())
                .split("-")[0].toInt() - birth.split("/")[0].toInt() + 1
            profile = Profile(age.toString(), birth, drinking, gender,
                hobby, introMe, job, mbti, nickname, personality,
                religion, smoke, PROFILE[CURRENT_USERID]?.tel, true)
        } else {
            profile = Profile(PROFILE[CURRENT_USERID]?.age, PROFILE[CURRENT_USERID]?.birth, drinking,
                PROFILE[CURRENT_USERID]?.gender, hobby, introMe, job, mbti, nickname, personality,
                religion, smoke, PROFILE[CURRENT_USERID]?.tel, true)
        }
        animation = Animation(permission = true, person = true, request = true)

        userDB = Firebase.database.reference.child(DB_PROFILE).child(CURRENT_USERID)
        userDB.setValue(profile)

        userDB = Firebase.database.reference.child(DB_ANIMATION).child(CURRENT_USERID)
        userDB.setValue(animation)
    }

    private fun completeProfileSetting() {
        insertProfileInformation()
        Thread {
            Thread.sleep(2000)
            Handler(Looper.getMainLooper()).post {
                startActivity(Intent(this@ProfileSettingActivity, BackgroundActivity::class.java))
                finish()
            }
        }.start()
    }

    private fun showAnimationPhoto() {
        val dialog = AlertDialog.Builder(this).create()
        val edialog : LayoutInflater = LayoutInflater.from(this)
        val mView : View = edialog.inflate(R.layout.dialog_check_animation,null)
        val image : CircleImageView = mView.findViewById(R.id.dialog_check_animation_photo)
        val cancel : Button = mView.findViewById(R.id.dialog_check_animation_cancel)
        val save : Button = mView.findViewById(R.id.dialog_check_animation_save)
        val dialog_progressBar : DotProgressBar = mView.findViewById(R.id.dialog_check_animation_progressbar)
        val nicknameTextView = mView.findViewById<TextView>(R.id.nickname_TextView)

        Glide.with(this)
            .load(UserInformation.URI[CURRENT_USERID])
            .into(image)

        cancel.setOnClickListener {
            dialog.dismiss()
            dialog.cancel()
        }

        save.setOnClickListener {
            dialog_progressBar.visibility = View.VISIBLE
            completeProfileSetting()
        }

        dialog.setView(mView)
        nicknameTextView.setText(nickname)
        dialog.create()
        dialog.show()

    }

    private fun saveProfile() {
        val saveButton = findViewById<Button>(R.id.profile_setting_saveButton)

        saveButton.setOnClickListener {
            if (checkInformation()) {
                showAnimationPhoto()
            }
            else {
                if (!ANIMATION[CURRENT_USERID]!!.person) {
                    Toast.makeText(this, "인물 사진을 넣어주세요!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "정보를 완벽하게 입력해주세요!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
