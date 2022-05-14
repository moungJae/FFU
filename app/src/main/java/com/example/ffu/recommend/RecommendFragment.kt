package com.example.ffu.recommend

import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ffu.R
import com.google.android.gms.location.*
import com.naver.maps.map.*
import com.naver.maps.geometry.*
import com.naver.maps.map.overlay.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import java.util.jar.*
import android.location.*
import android.text.style.BackgroundColorSpan
import com.naver.maps.map.style.layers.CircleLayer


class RecommendFragment : Fragment(), OnMapReadyCallback {

    // 지도 조작
    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView
    //private lateinit var fusedLocationClient: FusedLocationProviderClient
    // 좌표 조작
    private lateinit var locationSource: FusedLocationSource

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

    // 사용자의 현재 위치를 받을 때 요청한다.
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                // 권한 승인됨.
                if (locationSource.onRequestPermissionsResult(
                        requestCode, permissions, grantResults
                    )
                ) {
                    // 권한 승인 안될때
                    // 지도 받아올 수 없을 때 에러.
                    if (!locationSource.isActivated) {
                        naverMap.locationTrackingMode = LocationTrackingMode.None
                    }
                }
            }
        }
    }


    override fun onMapReady(nMap: NaverMap) {
        naverMap = nMap
        val circle = CircleOverlay()
        // 최대, 최소 줌
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 8.0
        // 초기 위치 설정
        naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(37.5509, 126.9410)))

        // 현재 위치 설정
        naverMap.uiSettings.isLocationButtonEnabled = true

        //locationSource = FusedLocationSource(this@RecommendFragment, LOCATION_PERMISSION_REQUEST_CODE)
        // 내장 위치 추적 기능 사용
        naverMap.locationSource = FusedLocationSource(this@RecommendFragment, LOCATION_PERMISSION_REQUEST_CODE)

        // 마커 가운데 표시,좌표 받기.
        val marker = Marker()
        marker.position = LatLng(37.497885, 127.027512) // marker 위치
        marker.map = naverMap //marker가 지도에 찍힌다.
        // 마커 아이콘 속성
        marker.icon = MarkerIcons.LIGHTBLUE
        marker.iconTintColor = Color.rgb(159,214,253)

        // 카메라 움직임에 대한 이벤트
        naverMap.addOnCameraChangeListener{ reason, animated ->
            Log.i("NaverMap", "카메라 변경 - reason:$reason, animated: $animated")
            // 현재 보이는 지도의 가운데로 마커 이동
            marker.position = LatLng(
                naverMap.cameraPosition.target.latitude,
                naverMap.cameraPosition.target.longitude
            )
            // 원 안보이게 하기
            circle.map = null
            // 텍스트 세팅, 확인 버튼 비활성화 -> run 사용하여 text 교체하면될듯?

        }

        // 카메라 움직임 종료에 대한 이벤트
        naverMap.addOnCameraIdleListener {
            marker.position = LatLng(
                naverMap.cameraPosition.target.latitude,
                naverMap.cameraPosition.target.longitude
            )
            // 원 그리기
            circle.center = LatLng(
                naverMap.cameraPosition.target.latitude,
                naverMap.cameraPosition.target.longitude
            )
            circle.radius = 500.0 // m단위.
            circle.map = naverMap
            circle.outlineWidth = 8
            circle.color = Color.argb(30,159,214,253)
            circle.outlineColor = Color.rgb(159,214,253)

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
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}