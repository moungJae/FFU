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
                        checkAllPersonality.text = "?????? ??????"
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
                    R.id.personalityActive -> RecommendData.personalitySet.add("????????????")
                    R.id.personalityQuiet -> RecommendData.personalitySet.add("?????????")
                    R.id.personalityZany -> RecommendData.personalitySet.add("?????????")
                    R.id.personalitySerious -> RecommendData.personalitySet.add("?????????")
                    R.id.personalityFreely -> RecommendData.personalitySet.add("????????????")
                    R.id.personalityImprovised -> RecommendData.personalitySet.add("????????????")
                    R.id.personalityDetailed -> RecommendData.personalitySet.add("?????????")
                    R.id.personalitySensitive -> RecommendData.personalitySet.add("????????????")
                    R.id.personalityDiligent -> RecommendData.personalitySet.add("?????????")
                    R.id.personalityLogical -> RecommendData.personalitySet.add("????????????")
                    R.id.personalityCalm -> RecommendData.personalitySet.add("?????????")
                    R.id.personalityConfident -> RecommendData.personalitySet.add("???????????????")
                    R.id.personalityCharming -> RecommendData.personalitySet.add("????????????")
                    R.id.personalityMature -> RecommendData.personalitySet.add("???????????????")
                    R.id.personalityPolite -> RecommendData.personalitySet.add("?????? ??????")
                    R.id.personalityHumorous -> RecommendData.personalitySet.add("???????????????")
                    R.id.personalityHumble -> RecommendData.personalitySet.add("?????? ??????")
                    R.id.personalityIntel -> RecommendData.personalitySet.add("?????????")
                    R.id.personalityTimid -> RecommendData.personalitySet.add("?????????")
                    R.id.personalityCool -> RecommendData.personalitySet.add("??????")
                    R.id.personalityDdorai -> RecommendData.personalitySet.add("???????????????")
                    R.id.personalityKind -> RecommendData.personalitySet.add("?????????")
                    R.id.personalityPlanned -> RecommendData.personalitySet.add("????????????")
                    R.id.personalitySelfConfident -> RecommendData.personalitySet.add("?????????")
                }
            }
            else {
                when(buttonView.id) {
                    R.id.checkAllPersonality -> {
                        checkAllPersonality.text = "?????? ??????"
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
                    R.id.personalityActive -> RecommendData.personalitySet.remove("????????????")
                    R.id.personalityQuiet -> RecommendData.personalitySet.remove("?????????")
                    R.id.personalityZany -> RecommendData.personalitySet.remove("?????????")
                    R.id.personalitySerious -> RecommendData.personalitySet.remove("?????????")
                    R.id.personalityFreely -> RecommendData.personalitySet.remove("????????????")
                    R.id.personalityImprovised -> RecommendData.personalitySet.remove("????????????")
                    R.id.personalityDetailed -> RecommendData.personalitySet.remove("?????????")
                    R.id.personalitySensitive -> RecommendData.personalitySet.remove("????????????")
                    R.id.personalityDiligent -> RecommendData.personalitySet.remove("?????????")
                    R.id.personalityLogical -> RecommendData.personalitySet.remove("????????????")
                    R.id.personalityCalm -> RecommendData.personalitySet.remove("?????????")
                    R.id.personalityConfident -> RecommendData.personalitySet.remove("???????????????")
                    R.id.personalityCharming -> RecommendData.personalitySet.remove("????????????")
                    R.id.personalityMature -> RecommendData.personalitySet.remove("???????????????")
                    R.id.personalityPolite -> RecommendData.personalitySet.remove("?????? ??????")
                    R.id.personalityHumorous -> RecommendData.personalitySet.remove("???????????????")
                    R.id.personalityHumble -> RecommendData.personalitySet.remove("?????? ??????")
                    R.id.personalityIntel -> RecommendData.personalitySet.remove("?????????")
                    R.id.personalityTimid -> RecommendData.personalitySet.remove("?????????")
                    R.id.personalityCool -> RecommendData.personalitySet.remove("??????")
                    R.id.personalityDdorai -> RecommendData.personalitySet.remove("???????????????")
                    R.id.personalityKind -> RecommendData.personalitySet.remove("?????????")
                    R.id.personalityPlanned -> RecommendData.personalitySet.remove("????????????")
                    R.id.personalitySelfConfident -> RecommendData.personalitySet.remove("?????????")
                }
            }
        }
    }
}