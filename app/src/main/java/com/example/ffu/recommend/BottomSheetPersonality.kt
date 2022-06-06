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
        checkAllPersonality.isChecked = true
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
                    R.id.personalityActive -> RecommendData.personalitySet.add("적극적인")
                    R.id.personalityQuiet -> RecommendData.personalitySet.add("조용한")
                    R.id.personalityZany -> RecommendData.personalitySet.add("엉뚱한")
                    R.id.personalitySerious -> RecommendData.personalitySet.add("진지한")
                    R.id.personalityFreely -> RecommendData.personalitySet.add("자유로운")
                    R.id.personalityImprovised -> RecommendData.personalitySet.add("즉흥적인")
                    R.id.personalityDetailed -> RecommendData.personalitySet.add("꼼꼼한")
                    R.id.personalitySensitive -> RecommendData.personalitySet.add("감성적인")
                    R.id.personalityDiligent -> RecommendData.personalitySet.add("성실한")
                    R.id.personalityLogical -> RecommendData.personalitySet.add("논리적인")
                    R.id.personalityCalm -> RecommendData.personalitySet.add("침착한")
                    R.id.personalityConfident -> RecommendData.personalitySet.add("자신감있는")
                    R.id.personalityCharming -> RecommendData.personalitySet.add("애교있는")
                    R.id.personalityMature -> RecommendData.personalitySet.add("어른스러운")
                    R.id.personalityPolite -> RecommendData.personalitySet.add("예의 바른")
                    R.id.personalityHumorous -> RecommendData.personalitySet.add("유머러스한")
                    R.id.personalityHumble -> RecommendData.personalitySet.add("허세 없는")
                    R.id.personalityIntel -> RecommendData.personalitySet.add("지적인")
                    R.id.personalityTimid -> RecommendData.personalitySet.add("소심한")
                    R.id.personalityCool -> RecommendData.personalitySet.add("쿨한")
                    R.id.personalityDdorai -> RecommendData.personalitySet.add("또라이같은")
                    R.id.personalityKind -> RecommendData.personalitySet.add("친절한")
                    R.id.personalityPlanned -> RecommendData.personalitySet.add("계획적인")
                    R.id.personalitySelfConfident -> RecommendData.personalitySet.add("당당한")
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
                    R.id.personalityActive -> RecommendData.personalitySet.remove("적극적인")
                    R.id.personalityQuiet -> RecommendData.personalitySet.remove("조용한")
                    R.id.personalityZany -> RecommendData.personalitySet.remove("엉뚱한")
                    R.id.personalitySerious -> RecommendData.personalitySet.remove("진지한")
                    R.id.personalityFreely -> RecommendData.personalitySet.remove("자유로운")
                    R.id.personalityImprovised -> RecommendData.personalitySet.remove("즉흥적인")
                    R.id.personalityDetailed -> RecommendData.personalitySet.remove("꼼꼼한")
                    R.id.personalitySensitive -> RecommendData.personalitySet.remove("감성적인")
                    R.id.personalityDiligent -> RecommendData.personalitySet.remove("성실한")
                    R.id.personalityLogical -> RecommendData.personalitySet.remove("논리적인")
                    R.id.personalityCalm -> RecommendData.personalitySet.remove("침착한")
                    R.id.personalityConfident -> RecommendData.personalitySet.remove("자신감있는")
                    R.id.personalityCharming -> RecommendData.personalitySet.remove("애교있는")
                    R.id.personalityMature -> RecommendData.personalitySet.remove("어른스러운")
                    R.id.personalityPolite -> RecommendData.personalitySet.remove("예의 바른")
                    R.id.personalityHumorous -> RecommendData.personalitySet.remove("유머러스한")
                    R.id.personalityHumble -> RecommendData.personalitySet.remove("허세 없는")
                    R.id.personalityIntel -> RecommendData.personalitySet.remove("지적인")
                    R.id.personalityTimid -> RecommendData.personalitySet.remove("소심한")
                    R.id.personalityCool -> RecommendData.personalitySet.remove("쿨한")
                    R.id.personalityDdorai -> RecommendData.personalitySet.remove("또라이같은")
                    R.id.personalityKind -> RecommendData.personalitySet.remove("친절한")
                    R.id.personalityPlanned -> RecommendData.personalitySet.remove("계획적인")
                    R.id.personalitySelfConfident -> RecommendData.personalitySet.remove("당당한")
                }
            }
        }
    }
}