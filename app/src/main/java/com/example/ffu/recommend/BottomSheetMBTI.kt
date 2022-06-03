package com.example.ffu.recommend

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import com.example.ffu.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetMBTI : BottomSheetDialogFragment() {

    private lateinit var listener : CompoundButton.OnCheckedChangeListener
    private lateinit var checkAllMBTI : CompoundButton
    private lateinit var checkESTJ : CompoundButton
    private lateinit var checkESFJ : CompoundButton
    private lateinit var checkENFJ : CompoundButton
    private lateinit var checkENTJ : CompoundButton
    private lateinit var checkENTP : CompoundButton
    private lateinit var checkENFP : CompoundButton
    private lateinit var checkESFP : CompoundButton
    private lateinit var checkESTP : CompoundButton
    private lateinit var checkINTP : CompoundButton
    private lateinit var checkINFP : CompoundButton
    private lateinit var checkISFP : CompoundButton
    private lateinit var checkISTP : CompoundButton
    private lateinit var checkISTJ : CompoundButton
    private lateinit var checkISFJ : CompoundButton
    private lateinit var checkINFJ : CompoundButton
    private lateinit var checkINTJ : CompoundButton
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.control_mbti, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        makeListner()
        checkBoxInit(view)
    }
    private fun checkBoxInit(view : View) {
        checkAllMBTI = view.findViewById<CompoundButton>(R.id.checkAllMBTI)
        checkESTJ = view.findViewById<CompoundButton>(R.id.checkESTJ)
        checkESFJ = view.findViewById<CompoundButton>(R.id.checkESFJ)
        checkENFJ = view.findViewById<CompoundButton>(R.id.checkENFJ)
        checkENTJ = view.findViewById<CompoundButton>(R.id.checkENTJ)
        checkENTP = view.findViewById<CompoundButton>(R.id.checkENTP)
        checkENFP = view.findViewById<CompoundButton>(R.id.checkENFP)
        checkESFP = view.findViewById<CompoundButton>(R.id.checkESFP)
        checkESTP = view.findViewById<CompoundButton>(R.id.checkESTP)
        checkINTP = view.findViewById<CompoundButton>(R.id.checkINTP)
        checkINFP = view.findViewById<CompoundButton>(R.id.checkINFP)
        checkISFP = view.findViewById<CompoundButton>(R.id.checkISFP)
        checkISTP = view.findViewById<CompoundButton>(R.id.checkISTP)
        checkISTJ = view.findViewById<CompoundButton>(R.id.checkISTJ)
        checkISFJ = view.findViewById<CompoundButton>(R.id.checkISFJ)
        checkINFJ = view.findViewById<CompoundButton>(R.id.checkINFJ)
        checkINTJ = view.findViewById<CompoundButton>(R.id.checkINTJ)

        checkAllMBTI.setOnCheckedChangeListener(listener)
        checkESTJ.setOnCheckedChangeListener(listener)
        checkESFJ.setOnCheckedChangeListener(listener)
        checkENFJ.setOnCheckedChangeListener(listener)
        checkENTJ.setOnCheckedChangeListener(listener)
        checkENTP.setOnCheckedChangeListener(listener)
        checkENFP.setOnCheckedChangeListener(listener)
        checkESFP.setOnCheckedChangeListener(listener)
        checkESTP.setOnCheckedChangeListener(listener)
        checkINTP.setOnCheckedChangeListener(listener)
        checkINFP.setOnCheckedChangeListener(listener)
        checkISFP.setOnCheckedChangeListener(listener)
        checkISTP.setOnCheckedChangeListener(listener)
        checkISTJ.setOnCheckedChangeListener(listener)
        checkISFJ.setOnCheckedChangeListener(listener)
        checkINFJ.setOnCheckedChangeListener(listener)
        checkINTJ.setOnCheckedChangeListener(listener)
    }
    private fun makeListner() {
        listener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                when (buttonView.id) {
                    R.id.checkAllMBTI -> {
                        checkAllMBTI.text = "전체 선택"
                        checkESTJ.isChecked = true
                        checkESFJ.isChecked = true
                        checkENFJ.isChecked = true
                        checkENTJ.isChecked = true
                        checkENTP.isChecked = true
                        checkENFP.isChecked = true
                        checkESFP.isChecked = true
                        checkESTP.isChecked = true
                        checkINTP.isChecked = true
                        checkINFP.isChecked = true
                        checkISFP.isChecked = true
                        checkISTP.isChecked = true
                        checkISTJ.isChecked = true
                        checkISFJ.isChecked = true
                        checkINFJ.isChecked = true
                        checkINTJ.isChecked = true
                    }
                    R.id.checkESTJ -> RecommendData.MBTIList.add("ESTJ")
                    R.id.checkESFJ -> RecommendData.MBTIList.add("ESFJ")
                    R.id.checkENFJ -> RecommendData.MBTIList.add("ENFJ")
                    R.id.checkENTJ -> RecommendData.MBTIList.add("ENTJ")
                    R.id.checkENTP -> RecommendData.MBTIList.add("ENTP")
                    R.id.checkENFP -> RecommendData.MBTIList.add("ENFP")
                    R.id.checkESFP -> RecommendData.MBTIList.add("ESFP")
                    R.id.checkESTP -> RecommendData.MBTIList.add("ESTP")

                    R.id.checkINTP -> RecommendData.MBTIList.add("INTP")
                    R.id.checkINFP -> RecommendData.MBTIList.add("INFP")
                    R.id.checkISFP -> RecommendData.MBTIList.add("ISFP")
                    R.id.checkISTP -> RecommendData.MBTIList.add("ISTP")
                    R.id.checkISTJ -> RecommendData.MBTIList.add("ISTJ")
                    R.id.checkISFJ -> RecommendData.MBTIList.add("ISFJ")
                    R.id.checkINFJ -> RecommendData.MBTIList.add("INFJ")
                    R.id.checkINTJ -> RecommendData.MBTIList.add("INTJ")
                }
            }
            else {
                when(buttonView.id) {
                    R.id.checkAllMBTI -> {
                        checkAllMBTI.text = "전체 해제"
                        checkESTJ.isChecked = false
                        checkESFJ.isChecked = false
                        checkENFJ.isChecked = false
                        checkENTJ.isChecked = false
                        checkENTP.isChecked = false
                        checkENFP.isChecked = false
                        checkESFP.isChecked = false
                        checkESTP.isChecked = false
                        checkINTP.isChecked = false
                        checkINFP.isChecked = false
                        checkISFP.isChecked = false
                        checkISTP.isChecked = false
                        checkISTJ.isChecked = false
                        checkISFJ.isChecked = false
                        checkINFJ.isChecked = false
                        checkINTJ.isChecked = false
                    }
                    R.id.checkESTJ -> RecommendData.MBTIList.remove("ESTJ")
                    R.id.checkESFJ -> RecommendData.MBTIList.remove("ESFJ")
                    R.id.checkENFJ -> RecommendData.MBTIList.remove("ENFJ")
                    R.id.checkENTJ -> RecommendData.MBTIList.remove("ENTJ")
                    R.id.checkENTP -> RecommendData.MBTIList.remove("ENTP")
                    R.id.checkENFP -> RecommendData.MBTIList.remove("ENFP")
                    R.id.checkESFP -> RecommendData.MBTIList.remove("ESFP")
                    R.id.checkESTP -> RecommendData.MBTIList.remove("ESTP")

                    R.id.checkINTP -> RecommendData.MBTIList.remove("INTP")
                    R.id.checkINFP -> RecommendData.MBTIList.remove("INFP")
                    R.id.checkISFP -> RecommendData.MBTIList.remove("ISFP")
                    R.id.checkISTP -> RecommendData.MBTIList.remove("ISTP")
                    R.id.checkISTJ -> RecommendData.MBTIList.remove("ISTJ")
                    R.id.checkISFJ -> RecommendData.MBTIList.remove("ISFJ")
                    R.id.checkINFJ -> RecommendData.MBTIList.remove("INFJ")
                    R.id.checkINTJ -> RecommendData.MBTIList.remove("INTJ")
                }
            }
        }
    }
}