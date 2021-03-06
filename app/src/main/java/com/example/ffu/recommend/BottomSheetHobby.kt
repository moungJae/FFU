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
                        checkAllHobby.text = "?????? ??????"
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
                    R.id.hobbyMovie -> RecommendData.hobbySet.add("??????")
                    R.id.hobbyReading -> RecommendData.hobbySet.add("??????")
                    R.id.hobbyEating -> RecommendData.hobbySet.add("?????? ??????")
                    R.id.hobbyWorkOut -> RecommendData.hobbySet.add("??????")
                    R.id.hobbyCamping -> RecommendData.hobbySet.add("??????")
                    R.id.hobbyCoding -> RecommendData.hobbySet.add("??????")
                    R.id.hobbyCafe -> RecommendData.hobbySet.add("??????")
                    R.id.hobbyHiking -> RecommendData.hobbySet.add("??????")
                    R.id.hobbyBeer -> RecommendData.hobbySet.add("??????")
                    R.id.hobbyTrip -> RecommendData.hobbySet.add("??????")
                    R.id.hobbyShopping -> RecommendData.hobbySet.add("??????")
                    R.id.hobbyWalking -> RecommendData.hobbySet.add("??????")
                    R.id.hobbyTalking -> RecommendData.hobbySet.add("??????")
                    R.id.hobbyBaseball -> RecommendData.hobbySet.add("?????? ??????")
                    R.id.hobbyRunning -> RecommendData.hobbySet.add("??????")
                    R.id.hobbyClimbing -> RecommendData.hobbySet.add("????????????")
                    R.id.hobbyInstrument -> RecommendData.hobbySet.add("?????? ??????")
                    R.id.hobbyDriving -> RecommendData.hobbySet.add("????????????")
                    R.id.hobbyInvest -> RecommendData.hobbySet.add("?????????")
                    R.id.hobbyPhoto -> RecommendData.hobbySet.add("?????? ??????")
                    R.id.hobbyCook -> RecommendData.hobbySet.add("??????")
                    R.id.hobbyGame -> RecommendData.hobbySet.add("??????")
                    R.id.hobbyCoinSinging -> RecommendData.hobbySet.add("???????????????")
                    R.id.hobbyRiding -> RecommendData.hobbySet.add("?????????")
                }
            }
            else {
                when(buttonView.id) {
                    R.id.checkAllHobby -> {
                        checkAllHobby.text = "?????? ??????"
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
                    R.id.hobbyMovie -> RecommendData.hobbySet.remove("??????")
                    R.id.hobbyReading -> RecommendData.hobbySet.remove("??????")
                    R.id.hobbyEating -> RecommendData.hobbySet.remove("?????? ??????")
                    R.id.hobbyWorkOut -> RecommendData.hobbySet.remove("??????")
                    R.id.hobbyCamping -> RecommendData.hobbySet.remove("??????")
                    R.id.hobbyCoding -> RecommendData.hobbySet.remove("??????")
                    R.id.hobbyCafe -> RecommendData.hobbySet.remove("??????")
                    R.id.hobbyHiking -> RecommendData.hobbySet.remove("??????")
                    R.id.hobbyBeer -> RecommendData.hobbySet.remove("??????")
                    R.id.hobbyTrip -> RecommendData.hobbySet.remove("??????")
                    R.id.hobbyShopping -> RecommendData.hobbySet.remove("??????")
                    R.id.hobbyWalking -> RecommendData.hobbySet.remove("??????")
                    R.id.hobbyTalking -> RecommendData.hobbySet.remove("??????")
                    R.id.hobbyBaseball -> RecommendData.hobbySet.remove("?????? ??????")
                    R.id.hobbyRunning -> RecommendData.hobbySet.remove("??????")
                    R.id.hobbyClimbing -> RecommendData.hobbySet.remove("????????????")
                    R.id.hobbyInstrument -> RecommendData.hobbySet.remove("?????? ??????")
                    R.id.hobbyDriving -> RecommendData.hobbySet.remove("????????????")
                    R.id.hobbyInvest -> RecommendData.hobbySet.remove("?????????")
                    R.id.hobbyPhoto -> RecommendData.hobbySet.remove("?????? ??????")
                    R.id.hobbyCook -> RecommendData.hobbySet.remove("??????")
                    R.id.hobbyGame -> RecommendData.hobbySet.remove("??????")
                    R.id.hobbyCoinSinging -> RecommendData.hobbySet.remove("???????????????")
                    R.id.hobbyRiding -> RecommendData.hobbySet.remove("?????????")
                }
            }
        }
    }
}