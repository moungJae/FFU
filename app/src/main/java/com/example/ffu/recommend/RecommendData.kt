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

        var distanceUsers = mutableMapOf<String, Double>()
        var MBTIList = ArrayList<String>()
        var hobbyList = ArrayList<String>()
        var personalityList = ArrayList<String>()
        var smokingCheck : Boolean = true
//        var MBTIList : ArrayList<String> = arrayListOf()
//        var hobbyList : ArrayList<String> = arrayListOf()
//        var personalityList : ArrayList<String> = arrayListOf()

    }
}