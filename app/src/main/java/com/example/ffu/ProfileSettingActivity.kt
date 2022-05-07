package com.example.ffu

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException
import java.util.logging.Logger

class ProfileSettingActivity : AppCompatActivity() {

    private var auth : FirebaseAuth? = null
    private var storage: FirebaseStorage ?= null
    private var selectedMBTI: String ?= null
    private var selectedReligion: String ?= null
    private val selectedPersonality = ArrayList<String>()
    private val selectedHobby = ArrayList<String>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profilesetting)

        auth = Firebase.auth
        storage = FirebaseStorage.getInstance()

        setMbti()
        setPersonality()
        setHobby()
        setReligion()
        setPhoto()
        saveProfile()
    }

    private fun setMbti() {
        val mbtiButton = findViewById<Button>(R.id.profilesetting_mbtiButton)

        mbtiButton.setOnClickListener {
            val items = arrayOf("ESTJ", "ESFJ", "ENFJ", "ENTJ",
                "ENTP", "ENFP", "ESFP", "ESTP",
                "INTP", "INFP", "ISFP", "ISTP",
                "ISTJ", "ISFJ", "INFJ", "INTJ")
            val builder = AlertDialog.Builder(this)
                .setTitle("자신의 MBTI를 하나 선택해주세요")
                .setSingleChoiceItems(items, -1) { dialog, which ->
                    selectedMBTI = items[which]
                }
                .setPositiveButton("선택") {dialog, which ->
                    Toast.makeText(this, "${selectedMBTI}가 선택되었습니다.", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
    }

    private fun setPersonality() {
        val personality = findViewById<Button>(R.id.profilesetting_personalButton)

        personality.setOnClickListener {
            val items = arrayOf("활발한", "조용한", "엉뚱한", "진지한",
                "자유로운", "즉흥적인", "꼼꼼한", "감성적인", "성실한",
                "논리적인", "침착한", "자신감이 넘치는", "애교가 넘치는")
            val selectedItemIndex = ArrayList<Int>()
            val builder = AlertDialog.Builder(this)
                .setTitle("자신의 성격을 여러개 선택해주세요")
                .setMultiChoiceItems(items, null){ dialogInterface: DialogInterface, i: Int, b: Boolean ->
                    if(b){
                        selectedItemIndex.add(i)
                    } else if(selectedItemIndex.contains(i)){
                        selectedItemIndex.remove(i)
                    }
                }.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
                    for(j in selectedItemIndex) {
                        selectedPersonality.add(items[j])
                    }
                    Toast.makeText(this, "${selectedPersonality}가 선택되었습니다.", Toast.LENGTH_SHORT)
                }
                .show()
        }
    }

    private fun setReligion() {
        val religion = findViewById<Button>(R.id.profilesetting_religionButton)

        religion.setOnClickListener {
            val items = arrayOf("무교", "기독교", "불교", "천주교",
                "이슬람교", "힌두교", "개신교", "기타")
            val builder = AlertDialog.Builder(this)
                .setTitle("자신의 종교를 하나 선택해주세요")
                .setSingleChoiceItems(items, -1) { dialog, which ->
                    selectedReligion = items[which]
                }
                .setPositiveButton("선택") {dialog, which ->
                    Toast.makeText(this, "${selectedReligion}가 선택되었습니다.", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
    }

    private fun setHobby() {
        val hobby = findViewById<Button>(R.id.profilesetting_hobbyButton)

        hobby.setOnClickListener {
            val items = arrayOf("영화보기", "독서하기", "맛집탐방", "운동하기",
                "캠핑하기", "운동하기", "카페가기", "등산하기", "춤추기",
                "여행하기","쇼핑하기","산책하기","수다떨기","잠자기",
                "바둑하기", "수영하기", "악기연주", "그림그리기",
                "글쓰기", "노래하기", "요리하기", "게임하기")
            val selectedItemIndex = ArrayList<Int>()
            val builder = AlertDialog.Builder(this)
                .setTitle("자신의 취미를 여러개 선택해주세요")
                .setMultiChoiceItems(items, null){ dialogInterface: DialogInterface, i: Int, b: Boolean ->
                    if(b){
                        selectedItemIndex.add(i)
                    } else if(selectedItemIndex.contains(i)){
                        selectedItemIndex.remove(i)
                    }
                }.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
                    for(j in selectedItemIndex) {
                        selectedHobby.add(items[j])
                    }
                    Toast.makeText(this, "${selectedHobby}가 선택되었습니다.", Toast.LENGTH_SHORT)
                }
                .show()
        }
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
                        Toast.makeText(this, "사진 등록을 성공했습니다.", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(this, "사진 등록을 실패했습니 " +
                                ".", Toast.LENGTH_SHORT).show()
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

    private fun saveProfile() {
        val saveButton = findViewById<Button>(R.id.profilesetting_saveButton)

        saveButton.setOnClickListener {
            finish()
        }
    }
}


