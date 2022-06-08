package com.example.ffu.join

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.ffu.R
import com.example.ffu.profile.ProfileSettingActivity
import com.metagalactic.dotprogressbar.DotProgressBar

class JoinActivity : AppCompatActivity() {

    private lateinit var birthText: TextView
    private lateinit var progressBar: DotProgressBar

    private var birth: String = ""
    private var gender: String = ""
    private var myYear = 2000
    private var myMonth = 1
    private var myDay = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join)

        initialSetting()
        checkGender()
        checkBirth()
        checkData()
    }

    private fun initialSetting() {
        progressBar = findViewById<DotProgressBar>(R.id.join_progressbar)
    }

    private fun checkBirth() {
        val year = findViewById<NumberPicker>(R.id.dialog_datePicker_year)
        val month = findViewById<NumberPicker>(R.id.dialog_datePicker_month)
        val day = findViewById<NumberPicker>(R.id.dialog_datePicker_day)

        year.wrapSelectorWheel = false
        month.wrapSelectorWheel = false
        day.wrapSelectorWheel = false
        year.minValue = 1970
        month.minValue = 1
        day.minValue = 1

        //  최대값 설정, 월마다 최대 일을 지정해야하는디..윤년
        year.maxValue = 2015
        month.maxValue = 12
        day.maxValue = 31
        // 현재 지정값 설정
        year.value = myYear
        month.value = myMonth
        day.value = myDay

        year.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        month.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        day.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        val listener = NumberPicker.OnValueChangeListener { numberPicker, old, new ->
            when (numberPicker) {
                year -> myYear = new
                month -> myMonth = new
                day -> myDay = new
                else -> Log.d("birth", "$year $month $day")
            }
        }
        year.setOnValueChangedListener(listener)
        month.setOnValueChangedListener(listener)
        day.setOnValueChangedListener(listener)
    }

    private fun checkGender() {
        val checkGender = findViewById<RadioGroup>(R.id.checkGender)

        checkGender.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.checkFemale -> gender = "여자"
                R.id.checkMale -> gender = "남자"
            }
        }
    }

    private fun moveNext() {
        val intent = Intent(this, ProfileSettingActivity::class.java)

        intent.putExtra("birth", birth)
        intent.putExtra("gender", gender)
        startActivity(intent)
        finish()
    }

    private fun checkData() {
        val nextButton = findViewById<Button>(R.id.join_joinNext)

        nextButton.setOnClickListener {
            birth = (myYear).toString() + "/" + (myMonth).toString() + "/" + (myDay).toString()
            if (birth.isEmpty()) {
                Toast.makeText(this, "생일을 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else if (gender.isEmpty()) {
                Toast.makeText(this, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                progressBar.visibility = View.VISIBLE
                Thread(Runnable {
                    Thread.sleep(500)
                    Handler(Looper.getMainLooper()).post {
                        moveNext()
                    }
                }).start()
            }
        }
    }
}
