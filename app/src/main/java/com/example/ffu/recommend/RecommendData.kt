package com.example.ffu.recommend

import android.annotation.SuppressLint
import android.widget.Button
import com.naver.maps.map.NaverMap

class RecommendData {

    companion object {
        var myRadius: Double = 500.0
        var distanceStr: String = "500m"

        @SuppressLint("StaticFieldLeak")
        lateinit var distanceButton: Button
        var curLatitude: Double = 0.0
        var curLongitude: Double = 0.0
        lateinit var naverMap : NaverMap
        var pastZoom : Int = 0
    }
}