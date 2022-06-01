package com.example.ffu.recommend

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.*
import android.location.*
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ffu.R
import com.example.ffu.UserInformation
import com.naver.maps.geometry.*
import com.naver.maps.map.*
import com.naver.maps.map.overlay.*
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Math.*
import kotlin.math.pow
import com.example.ffu.UserInformation.Companion.RECOMMEND
import com.example.ffu.UserInformation.Companion.CURRENT_USERID

class RecommendFragment : Fragment(), OnMapReadyCallback {

    // 지도 조작
    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView

    // 좌표 조작
    private val radius = 6372.8 * 1000
    private var recommendLatitude : Double = 0.0
    private var recommendLongitude : Double = 0.0
    private lateinit var mLastLocation: Location // 현재 위치 가지고 있는 객체

    private lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개 변수 저장
    private lateinit var locationSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient // 현재 위치 가져오기 위한 변수
    private lateinit var button:Button
    private lateinit var locationMsg : TextView
    // firebase
    private lateinit var userDB: DatabaseReference
    private var auth : FirebaseAuth = Firebase.auth

    // bottomSheet
    private lateinit var bottomSheet: ConstraintLayout
    lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // bottomsheet v2
//        bottomSheet = view.findViewById<ConstraintLayout>(R.id.fragmentBottomSheet)
//        sheetBehavior = BottomSheetBehavior.from(bottomSheet)
//        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                when (newState) {
//                    BottomSheetBehavior.STATE_COLLAPSED -> {}
//                    BottomSheetBehavior.STATE_DRAGGING -> {}
//                    BottomSheetBehavior.STATE_EXPANDED -> {}
//                    BottomSheetBehavior.STATE_HIDDEN -> {}
//                    BottomSheetBehavior.STATE_SETTLING -> {}
//                }
//            }
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//            }
//        }) // 여기까지 위에다 옮겨도됨.

        /* 위치 권한 요청 */
        requestPermission() // 최초 요청
        /* 좌표 가져오기 */
        startLocationUpdates()
        button = view.findViewById(R.id.btn_confirm)
        locationMsg = view.findViewById(R.id.locationMsg)
        button.setOnClickListener { // 클릭하면 추천 리스트 띄우는데 그때 거리 계산하고 띄우기.
            // 파이어베이스에서 user 좌표 가져와서 계산하여 uid vector에 넣기.
            val usersUid : ArrayList<String> = UserInformation.MAP_USER
            val myRadius : Int = 10 // EditText 또는 numberdialog로 거리 설정해야함.
            val recommendUsersUid = ArrayList<String>()
            // 리스트에 거리 내의 사용자의 uid 넣기.
            for (uid in usersUid) {
                if (uid == CURRENT_USERID) continue
                val distance : Int =
                    getDistance(RECOMMEND[uid]?.latitude, RECOMMEND[uid]?.longitude,
                    recommendLatitude, recommendLongitude) / 1000
                if (distance < myRadius) {
                    recommendUsersUid.add(uid)
                }
            }
            // button 누르면 bottomSheet (추천 List) 띄우기.
            // bottomsheet v1
            val bottomSheet = RecommendList(recommendUsersUid)
            bottomSheet.show(childFragmentManager, RecommendList.TAG)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // map 생성
        var rootview = inflater.inflate(R.layout.fragment_recommend, container, false)

        mapView = rootview.findViewById(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return rootview
    }

    /* ======================== 사용자 거리 계산 하여 일치하는 사용자 넣기========================*/

    fun getDistance(lat1: Double?, lon1: Double?, lat2: Double, lon2: Double): Int {
        val dLat = Math.toRadians(lat2 - lat1!!)
        val dLon = Math.toRadians(lon2 - lon1!!)
        val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
        val c = 2 * asin(sqrt(a))
        return (radius * c).toInt()
    }
    /* ========================권한 요청======================== */
    private fun backgroundPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            ), 2
        )
    }

    private fun backgroundDeniedPermission() {
        var builder = AlertDialog.Builder(context)
        builder.setTitle("Error").setMessage(
            "서비스 사용에 제약이 있을 수 있습니다. " +
                    "설정 -> 위치 -> 사용 중인 앱 -> (등등 경로 알려주기)" +
                    " 권한을 항상 허용으로 설정해주세요."
        )

        var listener = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE ->
                    backgroundPermission()
            }
        }
        builder.setPositiveButton("넹", null)
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
                DialogInterface.BUTTON_NEGATIVE -> {
                    backgroundDeniedPermission()
                }
            }
        }
        builder.setPositiveButton("네", listener)
        builder.setNegativeButton("아니오", listener)

        builder.show()
    }

    // 위치 권한 제대로 설정 안됐을 때
    private fun checkPermission() : Boolean{
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    /**
     * 1. 위치 권한 요청 -> deny -> 백그라운드 요청 -> yes -> 백그라운드는 되는데 포그라운드 X
     * 2. 위치 권한 요청 -> yes -> 백그라운드 요청 -> no -> 백그라운드 안되므로 에러 메시지 띄우고
     *
     * 최초에 -> 위치 권한 요청 -> deny -> 백그라운드 요청 -> yes -> 백그라운드 X, 포그라운드 X
     * 근데 백그라운드만 되면 포그라운드는 자동으로 되는건데..?
     *
     * 회의.
     * 사용자에게 위치 권한 항상 허용하라고 메시지를 띄울지? 지금 이대로,
     * 또는, 위치 권한 경로를 알려주고 일부 서비스 사용 제약이 있을 수 있다고 띄울까?
     */
    private fun requestPermission() {
        // 이미 권한이 있으면 그냥 리턴
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), 1
                )
                permissionDialog(requireContext())
                //checkPermission()
            }
            // API 23 미만 버전에서는 ACCESS_BACKGROUND_LOCATION X
            else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
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
        // init my location
//        mLastLocation.latitude = 0.0
//        mLastLocation.longitude = 0.0

        mLocationRequest = LocationRequest.create().apply {
            interval = 60 * 1000 // 업데이트 간격 단위, 1000밀리초 단위 (1초)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 정확성
            fastestInterval = 60 * 1000
            maxWaitTime = 60 * 1000 // 위치 갱신 요청 최대 대기 시간 (1000 -> 1초)
        }
        // FuesdLocationProviderClient의 인스턴스 생성
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 위치 권한 설정 안되어 있는 경우.
            Toast.makeText(requireContext(), "위치 권한 거부", Toast.LENGTH_SHORT).show()
            return
        }
        fusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback, Looper.myLooper()!!
        )
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
//            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
//            mLastLocation = locationResult.lastLocation
//            //addLocationToFirebase(locationResult.lastLocation)
        }
    }

    private fun addMyLocation(location: Location) {
        mLastLocation = location
        val locationToFirebase = mutableMapOf<String, Any>()
        locationToFirebase["latitude"] = mLastLocation.latitude
        locationToFirebase["longitude"] = mLastLocation.longitude
        userDB = Firebase.database.reference.child("recommend").child(CURRENT_USERID)
        userDB.updateChildren(locationToFirebase)
        Log.d("loc", "${mLastLocation.latitude}, ${mLastLocation.longitude}")
    }

    /* ========================지도 생성======================== */
    // 지도 화면에 생성
    override fun onMapReady(nMap: NaverMap) {
        naverMap = nMap
        val circle = CircleOverlay()
        // 최대, 최소 줌
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 8.0
        // 초기 위치 설정
        naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(37.5509, 126.9410)))
//        naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(mLastLocation.latitude, mLastLocation.longitude)))
        // 현재 위치 설정
        naverMap.uiSettings.isLocationButtonEnabled = true

        // 내장 위치 추적 기능 사용
        naverMap.locationSource =
            FusedLocationSource(this@RecommendFragment, REQUEST_ACCESS_LOCATION_PERMISSIONS)

        // 마커 가운데 표시,좌표 받기.
        val marker = Marker()
        marker.position = LatLng(37.497885, 127.027512) // marker 위치
        marker.map = naverMap //marker가 지도에 찍힌다.
        // 마커 아이콘 속성
        marker.icon = MarkerIcons.LIGHTBLUE
        marker.iconTintColor = Color.rgb(159, 214, 253)

        marker.isHideCollidedSymbols = true
        // 카메라 움직임에 대한 이벤트
        naverMap.addOnCameraChangeListener { reason, animated ->
            Log.i("NaverMap", "카메라 변경 - reason:$reason, animated: $animated")
            // 현재 보이는 지도의 가운데로 마커 이동
            marker.position = LatLng(
                naverMap.cameraPosition.target.latitude,
                naverMap.cameraPosition.target.longitude
            )
            // 원 안보이게 하기
            circle.map = null
            // 텍스트 세팅, 확인 버튼 비활성화 -> run 사용하여 text 교체하면될듯?
            button.visibility = View.INVISIBLE
            locationMsg.visibility = View.INVISIBLE
        }

        // 카메라 움직임 종료에 대한 이벤트
        naverMap.addOnCameraIdleListener {
            Log.i("NaverMap", "카메라 종료")
            button.visibility = View.VISIBLE
            locationMsg.visibility = View.VISIBLE
            marker.position = LatLng(
                naverMap.cameraPosition.target.latitude,
                naverMap.cameraPosition.target.longitude
            )
            // 원 그리기
            circle.center = LatLng(
                naverMap.cameraPosition.target.latitude,
                naverMap.cameraPosition.target.longitude
            )
            circle.radius = 5000.0 // m단위.
            circle.map = naverMap
            circle.outlineWidth = 8
            circle.color = Color.argb(30, 159, 214, 253)
            circle.outlineColor = Color.rgb(159, 214, 253)

            recommendLatitude = naverMap.cameraPosition.target.latitude
            recommendLongitude = naverMap.cameraPosition.target.longitude
            Log.d("recommendLocation", "${recommendLatitude}, ${recommendLongitude}")
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()

    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        private const val REQUEST_ACCESS_LOCATION_PERMISSIONS = 100
        private const val REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS = 101
    }
}