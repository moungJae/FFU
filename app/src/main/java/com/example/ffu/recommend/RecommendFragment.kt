package com.example.ffu.recommend

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.ffu.R
import com.example.ffu.UserInformation
import com.example.ffu.UserInformation.Companion.CURRENT_USERID
import com.example.ffu.UserInformation.Companion.PROFILE
import com.example.ffu.UserInformation.Companion.RECOMMEND
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import java.lang.Math.*
import kotlin.math.pow




class RecommendFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView

    private val radius = 6372.8 * 1000
    private var recommendLatitude : Double = 0.0
    private var recommendLongitude : Double = 0.0

    private lateinit var recommendButton:Button
    private lateinit var locationMsg : TextView

    // 사용자 선택
    private lateinit var MBTIButton : Button
    private lateinit var hobbyButton : Button
    private lateinit var personalityButton : Button
    private lateinit var smokingButton : Button

    private var auth : FirebaseAuth = Firebase.auth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeButtons(view)     // button 초기화
        bottomSheetListener()
        getMatchedUsers()           // 일치하는 사용자 가져오기
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
    private fun getMatchedUsers() {

        recommendButton.setOnClickListener { // 클릭하면 추천 리스트 띄우는데 그때 거리 계산하고 띄우기.
            val usersUid : ArrayList<String> = UserInformation.MAP_USER
            val myRadius = RecommendData.myRadius / 1000.0
            var recommendUsersUid = ArrayList<String>()

            for (uid in usersUid) {
                if (uid == auth.uid || uid == "null") continue
                val distance : Double =
                    getDistance(RECOMMEND[uid]?.latitude, RECOMMEND[uid]?.longitude,
                        recommendLatitude, recommendLongitude) / 1000.0

                if (distance < myRadius) {
                    recommendUsersUid.add(uid)
                    RecommendData.distanceUsers[uid] = distance * 1000.0
                }
                Log.d("distance", "$distance")
            }

            val mbtiMatched = ArrayList<String>()
            val hobbyMatched =  mutableMapOf<String, Int>()
            val personalityMatched = mutableMapOf<String, Int>()
            val finalMatched = mutableMapOf<String, Int>()

            for (mbti in RecommendData.MBTIList) {
                for (uid in recommendUsersUid) {
                    if (PROFILE[uid]?.mbti?.contains(mbti) == true) {
                        mbtiMatched.add(uid)
                        hobbyMatched[uid] = 0
                        Log.d("mbti true", "$uid, $mbti")
                    }
                }
            }

            for (hobby in RecommendData.hobbyList) {
                for (uid in mbtiMatched) {
                    if (PROFILE[uid]?.hobby?.contains(hobby) == true) {
                        hobbyMatched[uid] = hobbyMatched[uid]!! + 1
                        personalityMatched[uid] = hobbyMatched[uid]!!
                        Log.d("hobby true", "$uid, $hobby")
                    }
                }
            }

            for (personality in RecommendData.personalityList) {
                Log.d("personality list", personality)
                for (uid in hobbyMatched.keys) {
                    if (PROFILE[uid]?.personality?.contains(personality) == true) {
                        personalityMatched[uid] = personalityMatched[uid]!! + 1
                        Log.d("personality true", "$uid, $personality")
                    }
                }
            }

            if (RecommendData.smokingCheck) {
                for (uid in personalityMatched.keys) {
                    finalMatched[uid] = personalityMatched[uid]!!
                }
            } else {
                for (uid in personalityMatched.keys) {
                    if (PROFILE[uid]?.smoke?.equals("흡연") == false) {
                        finalMatched[uid] = personalityMatched[uid]!!
                    }
                }
            }

            for (mbti in mbtiMatched)
                Log.d("mbitMatched", "$mbti")
//            for (hobby in hobbyMatched.keys)
//                Log.d("hobbyMatched", "$hobby, ${hobbyMatched[hobby]}")
            for (personality in personalityMatched.keys)
                Log.d("personalityMatched", "$personality, ${personalityMatched[personality]}")

            if (finalMatched.isEmpty()) {
                Toast.makeText(requireContext(), "추천할 대상이 없습니다.", Toast.LENGTH_SHORT).show()
            }else {
                val bottomSheet = RecommendList(finalMatched)
                bottomSheet.show(childFragmentManager, RecommendList.TAG)
            }
        }
    }

    private fun initializeButtons(view : View) {
        recommendButton = view.findViewById(R.id.recommendButton)
        locationMsg = view.findViewById(R.id.locationMsg)
        RecommendData.distanceButton = view.findViewById(R.id.selectKm)
        MBTIButton  = view.findViewById(R.id.selectMBTI)
        hobbyButton  = view.findViewById(R.id.selectHobby)
        personalityButton  = view.findViewById(R.id.selectPersonality)
        smokingButton  = view.findViewById(R.id.selectSmoking)
    }

    private fun bottomSheetListener() {
        val distanceFrag = BottomSheetDistance()
        val MBTIFrag = BottomSheetMBTI()
        val hobbyFrag = BottomSheetHobby()
        val personalityFrag = BottomSheetPersonality()
        val SmokingFrag = BottomSheetSmoking()

        RecommendData.distanceButton.setOnClickListener{
            distanceFrag.show(childFragmentManager, distanceFrag.tag )
        }

        MBTIButton.setOnClickListener{
            MBTIFrag.show(childFragmentManager, MBTIFrag.tag)
        }

        hobbyButton.setOnClickListener {
            hobbyFrag.show(childFragmentManager, hobbyFrag.tag)
        }

        personalityButton.setOnClickListener {
            personalityFrag.show(childFragmentManager, personalityFrag.tag)
        }

        smokingButton.setOnClickListener {
            SmokingFrag.show(childFragmentManager, SmokingFrag.tag)
        }
    }


    fun getDistance(lat1: Double?, lon1: Double?, lat2: Double, lon2: Double): Double {
        val dLat = Math.toRadians(lat2 - lat1!!)
        val dLon = Math.toRadians(lon2 - lon1!!)
        val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
        val c = 2 * asin(sqrt(a))
        return (radius * c)
    }

    override fun onMapReady(nMap: NaverMap) {
        RecommendData.naverMap = nMap
        val circle = CircleOverlay()
        // 카메라 영역 제한

//        val northWest = LatLng(38.788658, 127.572789) //서북단
//        val southEast = LatLng(32.91011932632188, 126.36296186814593) //동남단
//        RecommendData.naverMap.extent = LatLngBounds(northWest, southEast)
        // 최대, 최소 줌

        RecommendData.naverMap.minZoom = 7.0
        RecommendData.naverMap.maxZoom = 14.0

//        Log.d("location", "${RECOMMEND[CURRENT_USERID]!!.latitude.toString()}, ${RECOMMEND[CURRENT_USERID]!!.longitude.toString()}")
//        RecommendData.naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(RECOMMEND[CURRENT_USERID]!!.latitude, RECOMMEND[CURRENT_USERID]!!.longitude)))

        RecommendData.naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(RECOMMEND[CURRENT_USERID]!!.latitude, RECOMMEND[CURRENT_USERID]!!.longitude)))
        // 현재 위치 설정
        RecommendData.naverMap.uiSettings.isLocationButtonEnabled = true

        // 내장 위치 추적 기능 사용
        RecommendData.naverMap.locationSource =
            FusedLocationSource(this@RecommendFragment, REQUEST_ACCESS_LOCATION_PERMISSIONS)

        // 마커 가운데 표시,좌표 받기.

        val marker = Marker()
        marker.position = LatLng(37.497885, 127.027512)
        marker.map = RecommendData.naverMap
        // 마커 아이콘 속성
        marker.icon = MarkerIcons.LIGHTBLUE

        marker.iconTintColor = Color.rgb(159, 214, 253)

        marker.isHideCollidedSymbols = true

        // 카메라 움직임에 대한 이벤트
        RecommendData.naverMap.addOnCameraChangeListener { reason, animated ->
            Log.i("RecommendData.naverMap", "카메라 변경 - reason:$reason, animated: $animated")

            marker.position = LatLng(
                RecommendData.naverMap.cameraPosition.target.latitude,
                RecommendData.naverMap.cameraPosition.target.longitude
            )

            circle.map = null
            recommendButton.visibility = View.INVISIBLE
            locationMsg.visibility = View.INVISIBLE
        }

        // 카메라 움직임 종료에 대한 이벤트
        RecommendData.naverMap.addOnCameraIdleListener {
            Log.i("RecommendData.naverMap", "카메라 종료")
            recommendButton.visibility = View.VISIBLE
            locationMsg.visibility = View.VISIBLE
            marker.position = LatLng(
                RecommendData.naverMap.cameraPosition.target.latitude,
                RecommendData.naverMap.cameraPosition.target.longitude
            )

            circle.center = LatLng(
                RecommendData.naverMap.cameraPosition.target.latitude,
                RecommendData.naverMap.cameraPosition.target.longitude
            )

            circle.radius = RecommendData.myRadius * 1.0
            circle.map = RecommendData.naverMap
            circle.outlineWidth = 8
            circle.color = Color.argb(40, 159, 214, 253)
            circle.outlineColor = Color.rgb(159, 214, 253)

            recommendLatitude = RecommendData.naverMap.cameraPosition.target.latitude
            recommendLongitude = RecommendData.naverMap.cameraPosition.target.longitude
            RecommendData.curLatitude = recommendLatitude
            RecommendData.curLongitude = recommendLongitude
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

    }
}
