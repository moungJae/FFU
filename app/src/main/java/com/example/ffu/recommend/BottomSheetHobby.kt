package com.example.ffu.recommend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.example.ffu.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetHobby : BottomSheetDialogFragment() {

    private lateinit var listener : CompoundButton.OnCheckedChangeListener

    private lateinit var checkAllHobby : CompoundButton
    private lateinit var hobbyMovie : CompoundButton
    private lateinit var hobbyReading : CompoundButton
    private lateinit var hobbyEating : CompoundButton
    private lateinit var hobbyWorkOut : CompoundButton
    private lateinit var hobbyCamping : CompoundButton
    private lateinit var hobbyCoding : CompoundButton
    private lateinit var hobbyCafe : CompoundButton
    private lateinit var hobbyHiking : CompoundButton
    private lateinit var hobbyBeer : CompoundButton
    private lateinit var hobbyTrip : CompoundButton
    private lateinit var hobbyShopping : CompoundButton
    private lateinit var hobbyWalking : CompoundButton
    private lateinit var hobbyTalking : CompoundButton
    private lateinit var hobbyBaseball : CompoundButton
    private lateinit var hobbyRunning : CompoundButton
    private lateinit var hobbyClimbing : CompoundButton
    private lateinit var hobbyInstrument : CompoundButton
    private lateinit var hobbyDriving : CompoundButton
    private lateinit var hobbyInvest : CompoundButton
    private lateinit var hobbyPhoto : CompoundButton
    private lateinit var hobbyCook : CompoundButton
    private lateinit var hobbyGame : CompoundButton
    private lateinit var hobbyCoinSinging : CompoundButton
    private lateinit var hobbyRiding : CompoundButton
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.control_hobby, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeListner()
        checkBoxInit(view)
        checkAllHobby.isChecked = true
    }
    private fun checkBoxInit(view : View) {
        checkAllHobby = view.findViewById<CompoundButton>(R.id.checkAllHobby)

        hobbyMovie = view.findViewById<CompoundButton>(R.id.hobbyMovie)
        hobbyReading = view.findViewById<CompoundButton>(R.id.hobbyReading)
        hobbyEating = view.findViewById<CompoundButton>(R.id.hobbyEating)
        hobbyWorkOut = view.findViewById<CompoundButton>(R.id.hobbyWorkOut)
        hobbyCamping = view.findViewById<CompoundButton>(R.id.hobbyCamping)
        hobbyCoding = view.findViewById<CompoundButton>(R.id.hobbyCoding)
        hobbyCafe = view.findViewById<CompoundButton>(R.id.hobbyCafe)
        hobbyHiking = view.findViewById<CompoundButton>(R.id.hobbyHiking)
        hobbyBeer = view.findViewById<CompoundButton>(R.id.hobbyBeer)
        hobbyTrip = view.findViewById<CompoundButton>(R.id.hobbyTrip)
        hobbyShopping = view.findViewById<CompoundButton>(R.id.hobbyShopping)
        hobbyWalking = view.findViewById<CompoundButton>(R.id.hobbyWalking)
        hobbyTalking = view.findViewById<CompoundButton>(R.id.hobbyTalking)
        hobbyBaseball = view.findViewById<CompoundButton>(R.id.hobbyBaseball)
        hobbyRunning = view.findViewById<CompoundButton>(R.id.hobbyRunning)
        hobbyClimbing = view.findViewById<CompoundButton>(R.id.hobbyClimbing)
        hobbyInstrument = view.findViewById<CompoundButton>(R.id.hobbyInstrument)
        hobbyDriving = view.findViewById<CompoundButton>(R.id.hobbyDriving)
        hobbyInvest = view.findViewById<CompoundButton>(R.id.hobbyInvest)
        hobbyPhoto = view.findViewById<CompoundButton>(R.id.hobbyPhoto)
        hobbyCook = view.findViewById<CompoundButton>(R.id.hobbyCook)
        hobbyGame = view.findViewById<CompoundButton>(R.id.hobbyGame)
        hobbyCoinSinging = view.findViewById<CompoundButton>(R.id.hobbyCoinSinging)
        hobbyRiding = view.findViewById<CompoundButton>(R.id.hobbyRiding)

        checkAllHobby.setOnCheckedChangeListener(listener)
        hobbyMovie.setOnCheckedChangeListener(listener)
        hobbyReading.setOnCheckedChangeListener(listener)
        hobbyEating.setOnCheckedChangeListener(listener)
        hobbyWorkOut.setOnCheckedChangeListener(listener)
        hobbyCamping.setOnCheckedChangeListener(listener)
        hobbyCoding.setOnCheckedChangeListener(listener)
        hobbyCafe.setOnCheckedChangeListener(listener)
        hobbyHiking.setOnCheckedChangeListener(listener)
        hobbyBeer.setOnCheckedChangeListener(listener)
        hobbyTrip.setOnCheckedChangeListener(listener)
        hobbyShopping.setOnCheckedChangeListener(listener)
        hobbyWalking.setOnCheckedChangeListener(listener)
        hobbyTalking.setOnCheckedChangeListener(listener)
        hobbyBaseball.setOnCheckedChangeListener(listener)
        hobbyRunning.setOnCheckedChangeListener(listener)
        hobbyClimbing.setOnCheckedChangeListener(listener)
        hobbyInstrument.setOnCheckedChangeListener(listener)
        hobbyDriving.setOnCheckedChangeListener(listener)
        hobbyInvest.setOnCheckedChangeListener(listener)
        hobbyPhoto.setOnCheckedChangeListener(listener)
        hobbyCook.setOnCheckedChangeListener(listener)
        hobbyGame.setOnCheckedChangeListener(listener)
        hobbyCoinSinging.setOnCheckedChangeListener(listener)
        hobbyRiding.setOnCheckedChangeListener(listener)
    }
    private fun makeListner() {
        listener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                when (buttonView.id) {
                    R.id.checkAllHobby -> {
                        checkAllHobby.text = "전체 해제"
                        hobbyMovie.isChecked = true
                        hobbyReading.isChecked = true
                        hobbyEating.isChecked = true
                        hobbyWorkOut.isChecked = true
                        hobbyCamping.isChecked = true
                        hobbyCoding.isChecked = true
                        hobbyCafe.isChecked = true
                        hobbyHiking.isChecked = true
                        hobbyBeer.isChecked = true
                        hobbyTrip.isChecked = true
                        hobbyShopping.isChecked = true
                        hobbyWalking.isChecked = true
                        hobbyTalking.isChecked = true
                        hobbyBaseball.isChecked = true
                        hobbyRunning.isChecked = true
                        hobbyClimbing.isChecked = true
                        hobbyInstrument.isChecked = true
                        hobbyDriving.isChecked = true
                        hobbyInvest.isChecked = true
                        hobbyPhoto.isChecked = true
                        hobbyCook.isChecked = true
                        hobbyGame.isChecked = true
                        hobbyCoinSinging.isChecked = true
                        hobbyRiding.isChecked = true
                    }
                    R.id.hobbyMovie -> RecommendData.hobbyList.add("영화")
                    R.id.hobbyReading -> RecommendData.hobbyList.add("독서")
                    R.id.hobbyEating -> RecommendData.hobbyList.add("맛집 탐방")
                    R.id.hobbyWorkOut -> RecommendData.hobbyList.add("운동")
                    R.id.hobbyCamping -> RecommendData.hobbyList.add("캠핑")
                    R.id.hobbyCoding -> RecommendData.hobbyList.add("코딩")
                    R.id.hobbyCafe -> RecommendData.hobbyList.add("카페")
                    R.id.hobbyHiking -> RecommendData.hobbyList.add("등산")
                    R.id.hobbyBeer -> RecommendData.hobbyList.add("맥주")
                    R.id.hobbyTrip -> RecommendData.hobbyList.add("여행")
                    R.id.hobbyShopping -> RecommendData.hobbyList.add("쇼핑")
                    R.id.hobbyWalking -> RecommendData.hobbyList.add("산책")
                    R.id.hobbyTalking -> RecommendData.hobbyList.add("수다")
                    R.id.hobbyBaseball -> RecommendData.hobbyList.add("야구 보기")
                    R.id.hobbyRunning -> RecommendData.hobbyList.add("러닝")
                    R.id.hobbyClimbing -> RecommendData.hobbyList.add("클라이밍")
                    R.id.hobbyInstrument -> RecommendData.hobbyList.add("악기 연주")
                    R.id.hobbyDriving -> RecommendData.hobbyList.add("드라이브")
                    R.id.hobbyInvest -> RecommendData.hobbyList.add("재테크")
                    R.id.hobbyPhoto -> RecommendData.hobbyList.add("사진 찍기")
                    R.id.hobbyCook -> RecommendData.hobbyList.add("요리")
                    R.id.hobbyGame -> RecommendData.hobbyList.add("게임")
                    R.id.hobbyCoinSinging -> RecommendData.hobbyList.add("코인노래방")
                    R.id.hobbyRiding -> RecommendData.hobbyList.add("라이딩")
                }
            }
            else {
                when(buttonView.id) {
                    R.id.checkAllHobby -> {
                        checkAllHobby.text = "전체 선택"
                        hobbyMovie.isChecked = false
                        hobbyReading.isChecked = false
                        hobbyEating.isChecked = false
                        hobbyWorkOut.isChecked = false
                        hobbyCamping.isChecked = false
                        hobbyCoding.isChecked = false
                        hobbyCafe.isChecked = false
                        hobbyHiking.isChecked = false
                        hobbyBeer.isChecked = false
                        hobbyTrip.isChecked = false
                        hobbyShopping.isChecked = false
                        hobbyWalking.isChecked = false
                        hobbyTalking.isChecked = false
                        hobbyBaseball.isChecked = false
                        hobbyRunning.isChecked = false
                        hobbyClimbing.isChecked = false
                        hobbyInstrument.isChecked = false
                        hobbyDriving.isChecked = false
                        hobbyInvest.isChecked = false
                        hobbyPhoto.isChecked = false
                        hobbyCook.isChecked = false
                        hobbyGame.isChecked = false
                        hobbyCoinSinging.isChecked = false
                        hobbyRiding.isChecked = false
                    }
                    R.id.hobbyMovie -> RecommendData.hobbyList.remove("영화")
                    R.id.hobbyReading -> RecommendData.hobbyList.remove("독서")
                    R.id.hobbyEating -> RecommendData.hobbyList.remove("맛집 탐방")
                    R.id.hobbyWorkOut -> RecommendData.hobbyList.remove("운동")
                    R.id.hobbyCamping -> RecommendData.hobbyList.remove("캠핑")
                    R.id.hobbyCoding -> RecommendData.hobbyList.remove("코딩")
                    R.id.hobbyCafe -> RecommendData.hobbyList.remove("카페")
                    R.id.hobbyHiking -> RecommendData.hobbyList.remove("등산")
                    R.id.hobbyBeer -> RecommendData.hobbyList.remove("맥주")
                    R.id.hobbyTrip -> RecommendData.hobbyList.remove("여행")
                    R.id.hobbyShopping -> RecommendData.hobbyList.remove("쇼핑")
                    R.id.hobbyWalking -> RecommendData.hobbyList.remove("산책")
                    R.id.hobbyTalking -> RecommendData.hobbyList.remove("수다")
                    R.id.hobbyBaseball -> RecommendData.hobbyList.remove("야구 보기")
                    R.id.hobbyRunning -> RecommendData.hobbyList.remove("러닝")
                    R.id.hobbyClimbing -> RecommendData.hobbyList.remove("클라이밍")
                    R.id.hobbyInstrument -> RecommendData.hobbyList.remove("악기 연주")
                    R.id.hobbyDriving -> RecommendData.hobbyList.remove("드라이브")
                    R.id.hobbyInvest -> RecommendData.hobbyList.remove("재테크")
                    R.id.hobbyPhoto -> RecommendData.hobbyList.remove("사진 찍기")
                    R.id.hobbyCook -> RecommendData.hobbyList.remove("요리")
                    R.id.hobbyGame -> RecommendData.hobbyList.remove("게임")
                    R.id.hobbyCoinSinging -> RecommendData.hobbyList.remove("코인노래방")
                    R.id.hobbyRiding -> RecommendData.hobbyList.remove("라이딩")
                }
            }
        }
    }
}