package com.example.ffu.recommend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.ffu.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetSmoking: BottomSheetDialogFragment() {

    private lateinit var yesRadioButton: RadioButton
    private lateinit var noRadioButton: RadioButton
    private lateinit var smokingRadioGroup: RadioGroup
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.control_smoking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        smokingRadioGroup = view.findViewById(R.id.smokingGroup)
        yesRadioButton = view.findViewById(R.id.smokingYes)
        noRadioButton = view.findViewById(R.id.smokingNO)

        noRadioButton.isChecked = true
        smokingRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when(i) {
                R.id.smokingYes -> RecommendData.smokingCheck = true
                R.id.smokingNO -> RecommendData.smokingCheck = false
            }
        }
    }
}