package com.example.ffu.join

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ffu.BackgroundActivity
import com.example.ffu.R
import com.example.ffu.UserInformation
import com.example.ffu.UserInformation.Companion.CURRENT_USERID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.ffu.UserInformation.Companion.PROFILE
import com.google.android.gms.location.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.naver.maps.map.util.FusedLocationSource

class WelcomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String

    private lateinit var mLastLocation: Location // 현재 위치 가지고 있는 객체

    private lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개 변수 저장
    private lateinit var fusedLocationClient: FusedLocationProviderClient // 현재 위치 가져오기 위한 변수
    private lateinit var userDB: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading)

        requestPermission()         // 위치 권한 요청
        startLocationUpdates()      // 사용자 좌표 가져오기
        setAuth()
        checkUser()
    }

    private fun setAuth() {
        auth = Firebase.auth
        userId = auth.uid.toString()
    }

    private fun checkUser() {
        if (auth.currentUser != null) { // 로그인 상태
            UserInformation()
            processLogin()
        } else { // 로그아웃 상태 => 핸드폰 인증 화면으로 즉시 이동
            processLogout()
        }
    }

    private fun checkJoin() {
        val progressBar = findViewById<ProgressBar>(R.id.loading_progressBar)

        progressBar.visibility = View.INVISIBLE
        if (PROFILE[userId]?.join == true) {
            startActivity(Intent(this, BackgroundActivity::class.java))
        } else {
            startActivity(Intent(this, JoinActivity::class.java))
        }
    }

    private fun processLogin() {
        val loadButton = findViewById<Button>(R.id.loading_button)
        val progressBar = findViewById<ProgressBar>(R.id.loading_progressBar)
        val loading_textview = findViewById<TextView>(R.id.loading_textview)

        loadButton.text = "FFU 시작하기"
        loading_textview.setText("시작하면 서비스 이용약관, 개인 정보 보호 약관, \n 그리고 위치정보 이용약관에 동의하게 됩니다.")

        loadButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            loadButton.isEnabled = false
            Thread(Runnable {
                Thread.sleep(3000)
                Handler(Looper.getMainLooper()).post {
                    loadButton.isEnabled = true
                    checkJoin()
                }
            }).start()
        }
    }

    private fun processLogout() {
        val loadButton = findViewById<Button>(R.id.loading_button)

        loadButton.text = "핸드폰 인증"
        loadButton.setOnClickListener {
            startActivity(Intent(this, PhoneVerificationActivity::class.java))
            finish()
        }
    }

    /* ========================권한 요청======================== */
    private fun backgroundPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            ), 2
        )
    }

    private fun backgroundDeniedPermission() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Error").setMessage(
            "서비스 사용에 제약이 있을 수 있습니다. " +
                    "설정 -> 위치 -> 사용 중인 앱 -> (등등 경로 알려주기)" +
                    " 권한을 항상 허용으로 설정해주세요."
        )

        var listener = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE -> finish()
            }
        }
        builder.setPositiveButton("넹", listener)
        builder.show()
    }

    private fun permissionDialog(context: Context) {
        var builder = AlertDialog.Builder(context)
        builder.setTitle("Alert").setMessage(
            "원할한 서비스를 위해 위치 권한을 항상 허용으로 설정해주세요." +
                    "(사용에 제약이 있을 수 있습니다!)"
        )

        var listener = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE ->
                    backgroundPermission()
            }
        }
        builder.setPositiveButton("확인", listener)
        builder.show()
    }


    private fun requestPermission() {
        // 이미 권한이 있으면 그냥 리턴
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), 1
                )
                permissionDialog(this)
            }
            // API 23 미만 버전에서는 ACCESS_BACKGROUND_LOCATION X
            else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), 1
                )
            }
        }
    }

    /* ========================사용자 위치 받기======================== */
    private fun startLocationUpdates() {

        mLocationRequest = LocationRequest.create().apply {
            interval = 10 * 1000 // 업데이트 간격 단위, 1000밀리초 단위 (1초)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 정확성
            fastestInterval = 10 * 1000
            maxWaitTime = 30 * 1000 // 위치 갱신 요청 최대 대기 시간 (1000 -> 1초)
        }
        // FuesdLocationProviderClient의 인스턴스 생성
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient!!.requestLocationUpdates(
                mLocationRequest, mLocationCallback, Looper.myLooper()!!
            )
        }
    }

    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                if (location != null) {
                    // 파이어베이스에 현재 위치 넣기
                    addMyLocation(location)
                }
            }
        }
    }

    private fun addMyLocation(location: Location) {
        val locationToFirebase = mutableMapOf<String, Any>()

        mLastLocation = location
        locationToFirebase["latitude"] = mLastLocation.latitude
        locationToFirebase["longitude"] = mLastLocation.longitude
        if (auth.uid != null) {
            userDB = Firebase.database.reference.child("recommend").child(auth.uid.toString())
            userDB.updateChildren(locationToFirebase)
        }
    }
}