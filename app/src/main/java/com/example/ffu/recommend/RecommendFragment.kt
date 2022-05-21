package com.example.ffu.recommend

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ffu.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.naver.maps.map.*
import com.naver.maps.geometry.*
import com.naver.maps.map.overlay.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons


class RecommendFragment : Fragment(), OnMapReadyCallback {

    // 지도 조작
    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView

    // 좌표 조작
    private lateinit var locationSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 위치 권한 요청
        requestPermission() // 최초 요청
        //checkPermission() // 권한 확인 후 재 요청 또는 서비스 불가 알림 메시지 띄우기
        // map 생성
        var rootview = inflater.inflate(R.layout.fragment_recommend, container, false)

        mapView = rootview.findViewById(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return rootview
    }

    // 위치 권한 거부 시
    // 위치 권한 요청 백그라운드 포그라운드
    private fun backgroundPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            ), 2)
    }
    private fun backgroundDeniedPermission() {
        var builder = AlertDialog.Builder(context)
        builder.setTitle("Error").setMessage("서비스 사용에 제약이 있을 수 있습니다. " +
                "설정 -> 위치 -> 사용 중인 앱 -> (등등 경로 알려주기)" +
                " 권한을 항상 허용으로 설정해주세요.")

        var listener = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE ->
                    backgroundPermission()
            }
        }
        builder.setPositiveButton("넹", null)
        builder.show()
    }

    private fun permissionDialog(context : Context){
        var builder = AlertDialog.Builder(context)
        builder.setTitle("Alert").
        setMessage("원할한 서비스를 위해 위치 권한을 항상 허용으로 설정해주세요." +
                "(사용에 제약이 있을 수 있습니다!)")

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
    private fun checkPermission() {
        if(ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            == PackageManager.PERMISSION_DENIED) {
            //permissionDialog(requireContext())
            backgroundDeniedPermission()
            return
        }
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
    private fun requestPermission(){
        // 이미 권한이 있으면 그냥 리턴
        if(ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            return
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), 1)
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
                    ), 1)
            }
        }
    }

    // 지도 화면에 생성
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
        naverMap.locationSource = FusedLocationSource(this@RecommendFragment, REQUEST_ACCESS_LOCATION_PERMISSIONS)

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
        private const val REQUEST_ACCESS_LOCATION_PERMISSIONS = 100
        private const val REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS = 101
    }
}