package com.example.ffu.recommend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.example.ffu.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetPersonality : BottomSheetDialogFragment() {

    private lateinit var listener : CompoundButton.OnCheckedChangeListener
    private lateinit var checkAllPersonality : CompoundButton
    private lateinit var personalityActive : CompoundButton
    private lateinit var personalityQuiet : CompoundButton
    private lateinit var personalityZany : CompoundButton
    private lateinit var personalitySerious : CompoundButton
    private lateinit var personalityFreely : CompoundButton
    private lateinit var personalityImprovised : CompoundButton
    private lateinit var personalityDetailed : CompoundButton
    private lateinit var personalitySensitive : CompoundButton
    private lateinit var personalityDiligent : CompoundButton
    private lateinit var personalityLogical : CompoundButton
    private lateinit var personalityCalm : CompoundButton
    private lateinit var personalityConfident : CompoundButton
    private lateinit var personalityCharming : CompoundButton
    private lateinit var personalityMature : CompoundButton
    private lateinit var personalityPolite : CompoundButton
    private lateinit var personalityHumorous : CompoundButton
    private lateinit var personalityHumble : CompoundButton
    private lateinit var personalityIntel : CompoundButton
    private lateinit var personalityTimid : CompoundButton
    private lateinit var personalityCool : CompoundButton
    private lateinit var personalityDdorai : CompoundButton
    private lateinit var personalityKind : CompoundButton
    private lateinit var personalityPlanned : CompoundButton
    private lateinit var personalitySelfConfident : CompoundButton
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.control_personality, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeListner()

        checkBoxInit(view)
    }
    private fun checkBoxInit(view : View) {
        checkAllPersonality = view.findViewById<CompoundButton>(R.id.checkAllPersonality)
        personalityActive = view.findViewById<CompoundButton>(R.id.personalityActive)
        personalityQuiet = view.findViewById<CompoundButton>(R.id.personalityQuiet)
        personalityZany = view.findViewById<CompoundButton>(R.id.personalityZany)
        personalitySerious = view.findViewById<CompoundButton>(R.id.personalitySerious)
        personalityFreely = view.findViewById<CompoundButton>(R.id.personalityFreely)
        personalityImprovised = view.findViewById<CompoundButton>(R.id.personalityImprovised)
        personalityDetailed = view.findViewById<CompoundButton>(R.id.personalityDetailed)
        personalitySensitive = view.findViewById<CompoundButton>(R.id.personalitySensitive)
        personalityDiligent = view.findViewById<CompoundButton>(R.id.personalityDiligent)
        personalityLogical = view.findViewById<CompoundButton>(R.id.personalityLogical)
        personalityCalm = view.findViewById<CompoundButton>(R.id.personalityCalm)
        personalityConfident = view.findViewById<CompoundButton>(R.id.personalityConfident)
        personalityCharming = view.findViewById<CompoundButton>(R.id.personalityCharming)
        personalityMature = view.findViewById<CompoundButton>(R.id.personalityMature)
        personalityPolite = view.findViewById<CompoundButton>(R.id.personalityPolite)
        personalityHumorous = view.findViewById<CompoundButton>(R.id.personalityHumorous)
        personalityHumble = view.findViewById<CompoundButton>(R.id.personalityHumble)
        personalityIntel = view.findViewById<CompoundButton>(R.id.personalityIntel)
        personalityTimid = view.findViewById<CompoundButton>(R.id.personalityTimid)
        personalityCool = view.findViewById<CompoundButton>(R.id.personalityCool)
        personalityDdorai = view.findViewById<CompoundButton>(R.id.personalityDdorai)
        personalityKind = view.findViewById<CompoundButton>(R.id.personalityKind)
        personalityPlanned = view.findViewById<CompoundButton>(R.id.personalityPlanned)
        personalitySelfConfident = view.findViewById<CompoundButton>(R.id.personalitySelfConfident)

        checkAllPersonality.setOnCheckedChangeListener(listener)
        personalityActive.setOnCheckedChangeListener(listener)
        personalityQuiet.setOnCheckedChangeListener(listener)
        personalityZany.setOnCheckedChangeListener(listener)
        personalitySerious.setOnCheckedChangeListener(listener)
        personalityFreely.setOnCheckedChangeListener(listener)
        personalityImprovised.setOnCheckedChangeListener(listener)
        personalityDetailed.setOnCheckedChangeListener(listener)
        personalitySensitive.setOnCheckedChangeListener(listener)

        personalityDiligent.setOnCheckedChangeListener(listener)
        personalityLogical.setOnCheckedChangeListener(listener)
        personalityCalm.setOnCheckedChangeListener(listener)
        personalityConfident.setOnCheckedChangeListener(listener)
        personalityCharming.setOnCheckedChangeListener(listener)
        personalityMature.setOnCheckedChangeListener(listener)
        personalityPolite.setOnCheckedChangeListener(listener)
        personalityHumorous.setOnCheckedChangeListener(listener)

        personalityHumble.setOnCheckedChangeListener(listener)
        personalityIntel.setOnCheckedChangeListener(listener)
        personalityTimid.setOnCheckedChangeListener(listener)
        personalityCool.setOnCheckedChangeListener(listener)
        personalityDdorai.setOnCheckedChangeListener(listener)
        personalityKind.setOnCheckedChangeListener(listener)
        personalityPlanned.setOnCheckedChangeListener(listener)
        personalitySelfConfident.setOnCheckedChangeListener(listener)
    }
    private fun makeListner() {
        listener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                when (buttonView.id) {
                    R.id.checkAllPersonality -> {
                        checkAllPersonality.text = "전체 해제"
                        personalityActive.isChecked = true
                        personalityQuiet.isChecked = true
                        personalityZany.isChecked = true
                        personalitySerious.isChecked = true
                        personalityFreely.isChecked = true
                        personalityImprovised.isChecked = true
                        personalityDetailed.isChecked = true
                        personalitySensitive.isChecked = true
                        personalityDiligent.isChecked = true
                        personalityLogical.isChecked = true
                        personalityCalm.isChecked = true
                        personalityConfident.isChecked = true
                        personalityCharming.isChecked = true
                        personalityMature.isChecked = true
                        personalityPolite.isChecked = true
                        personalityHumorous.isChecked = true
                        personalityHumble.isChecked = true
                        personalityIntel.isChecked = true
                        personalityTimid.isChecked = true
                        personalityCool.isChecked = true
                        personalityDdorai.isChecked = true
                        personalityKind.isChecked = true
                        personalityPlanned.isChecked = true
                        personalitySelfConfident.isChecked = true
                    }
                    R.id.personalityActive -> RecommendData.personalityList.add("적극적인")
                    R.id.personalityQuiet -> RecommendData.personalityList.add("조용한")
                    R.id.personalityZany -> RecommendData.personalityList.add("엉뚱한")
                    R.id.personalitySerious -> RecommendData.personalityList.add("진지한")
                    R.id.personalityFreely -> RecommendData.personalityList.add("자유로운")
                    R.id.personalityImprovised -> RecommendData.personalityList.add("즉흥적인")
                    R.id.personalityDetailed -> RecommendData.personalityList.add("꼼꼼한")
                    R.id.personalitySensitive -> RecommendData.personalityList.add("감성적인")
                    R.id.personalityDiligent -> RecommendData.personalityList.add("성실한")
                    R.id.personalityLogical -> RecommendData.personalityList.add("논리적인")
                    R.id.personalityCalm -> RecommendData.personalityList.add("침착한")
                    R.id.personalityConfident -> RecommendData.personalityList.add("자신감있는")
                    R.id.personalityCharming -> RecommendData.personalityList.add("애교있는")
                    R.id.personalityMature -> RecommendData.personalityList.add("어른스러운")
                    R.id.personalityPolite -> RecommendData.personalityList.add("예의 바른")
                    R.id.personalityHumorous -> RecommendData.personalityList.add("유머러스한")
                    R.id.personalityHumble -> RecommendData.personalityList.add("허세 없는")
                    R.id.personalityIntel -> RecommendData.personalityList.add("지적인")
                    R.id.personalityTimid -> RecommendData.personalityList.add("소심한")
                    R.id.personalityCool -> RecommendData.personalityList.add("쿨한")
                    R.id.personalityDdorai -> RecommendData.personalityList.add("또라이같은")
                    R.id.personalityKind -> RecommendData.personalityList.add("친절한")
                    R.id.personalityPlanned -> RecommendData.personalityList.add("계획적인")
                    R.id.personalitySelfConfident -> RecommendData.personalityList.add("당당한")
                }
            }
            else {
                when(buttonView.id) {
                    R.id.checkAllPersonality -> {
                        checkAllPersonality.text = "전체 선택"
                        personalityActive.isChecked = false
                        personalityQuiet.isChecked = false
                        personalityZany.isChecked = false
                        personalitySerious.isChecked = false
                        personalityFreely.isChecked = false
                        personalityImprovised.isChecked = false
                        personalityDetailed.isChecked = false
                        personalitySensitive.isChecked = false
                        personalityDiligent.isChecked = false
                        personalityLogical.isChecked = false
                        personalityCalm.isChecked = false
                        personalityConfident.isChecked = false
                        personalityCharming.isChecked = false
                        personalityMature.isChecked = false
                        personalityPolite.isChecked = false
                        personalityHumorous.isChecked = false
                        personalityHumble.isChecked = false
                        personalityIntel.isChecked = false
                        personalityTimid.isChecked = false
                        personalityCool.isChecked = false
                        personalityDdorai.isChecked = false
                        personalityKind.isChecked = false
                        personalityPlanned.isChecked = false
                        personalitySelfConfident.isChecked = false
                    }
                    R.id.personalityActive -> RecommendData.personalityList.remove("적극적인")
                    R.id.personalityQuiet -> RecommendData.personalityList.remove("조용한")
                    R.id.personalityZany -> RecommendData.personalityList.remove("엉뚱한")
                    R.id.personalitySerious -> RecommendData.personalityList.remove("진지한")
                    R.id.personalityFreely -> RecommendData.personalityList.remove("자유로운")
                    R.id.personalityImprovised -> RecommendData.personalityList.remove("즉흥적인")
                    R.id.personalityDetailed -> RecommendData.personalityList.remove("꼼꼼한")
                    R.id.personalitySensitive -> RecommendData.personalityList.remove("감성적인")
                    R.id.personalityDiligent -> RecommendData.personalityList.remove("성실한")
                    R.id.personalityLogical -> RecommendData.personalityList.remove("논리적인")
                    R.id.personalityCalm -> RecommendData.personalityList.remove("침착한")
                    R.id.personalityConfident -> RecommendData.personalityList.remove("자신감있는")
                    R.id.personalityCharming -> RecommendData.personalityList.remove("애교있는")
                    R.id.personalityMature -> RecommendData.personalityList.remove("어른스러운")
                    R.id.personalityPolite -> RecommendData.personalityList.remove("예의 바른")
                    R.id.personalityHumorous -> RecommendData.personalityList.remove("유머러스한")
                    R.id.personalityHumble -> RecommendData.personalityList.remove("허세 없는")
                    R.id.personalityIntel -> RecommendData.personalityList.remove("지적인")
                    R.id.personalityTimid -> RecommendData.personalityList.remove("소심한")
                    R.id.personalityCool -> RecommendData.personalityList.remove("쿨한")
                    R.id.personalityDdorai -> RecommendData.personalityList.remove("또라이같은")
                    R.id.personalityKind -> RecommendData.personalityList.remove("친절한")
                    R.id.personalityPlanned -> RecommendData.personalityList.remove("계획적인")
                    R.id.personalitySelfConfident -> RecommendData.personalityList.remove("당당한")
                }
            }
        }
    }
}