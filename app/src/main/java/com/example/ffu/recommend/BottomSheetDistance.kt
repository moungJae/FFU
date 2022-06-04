package com.example.ffu.recommend

import android.graphics.Camera
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import com.example.ffu.R
import com.example.ffu.UserInformation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.CircleOverlay

class BottomSheetDistance(): BottomSheetDialogFragment() {

    lateinit var seekbar : SeekBar
    lateinit var distanceText : TextView
    var pos = 0
    var distance = 500

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.control_distance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seekbar = view.findViewById(R.id.seekBar)
        distanceText = view.findViewById(R.id.distance_text)
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                when (progress) {
                    0 -> distanceText.text = "500m"
                    1 -> distanceText.text = "1km"
                    2 -> distanceText.text = "3km"
                    3 -> distanceText.text = "5km"
                    4 -> distanceText.text = "10km"
                    5 -> distanceText.text = "20km"
                    6 -> distanceText.text = "50km"
                    else -> distanceText.text = "100km"
                }
                pos = progress
                changeDistance(pos)
            }

            override fun onStartTrackingTouch(seekbar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekbar: SeekBar?) {

                RecommendData.distanceButton.text = RecommendData.distanceStr
                RecommendData.naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(RecommendData.curLatitude,RecommendData.curLongitude)))
                val zoom = CameraUpdate.zoomTo((14 - pos).toDouble()).animate(CameraAnimation.Linear, 500)
                RecommendData.naverMap.moveCamera(zoom)
                dismiss()
            }
        })
    }
    fun changeDistance(pos: Int) {
        distance = when (pos) {
            0 -> 500
            1 -> 1000
            2 -> 3000
            3 -> 5000
            4 -> 10000
            5 -> 20000
            6 -> 50000
            else -> 100000
        }
        var distanceStr = ""
        if (distance < 1000) {
            distanceStr = "${distance}m"
        } else {
            distanceStr = "${distance/1000}km"
        }

        RecommendData.myRadius = distance.toDouble()
        RecommendData.distanceStr = distanceStr
    }

}