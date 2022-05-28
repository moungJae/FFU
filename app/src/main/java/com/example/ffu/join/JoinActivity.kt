package com.example.ffu.join

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ffu.R
import com.example.ffu.profile.ProfileSettingActivity
import com.metagalactic.dotprogressbar.DotProgressBar

class JoinActivity : AppCompatActivity() {

    private lateinit var birthText : TextView
    private lateinit var progressBar : DotProgressBar

    private var birth : String = ""
    private var gender : String = ""
    private var myYear = 1970
    private var myMonth = 1
    private var myDay = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join)

        initialSetting()
        checkBirth()
        checkData()
    }

    private fun initialSetting() {
        progressBar = findViewById<DotProgressBar>(R.id.join_progressbar)
        birthText = findViewById<TextView>(R.id.join_text_birth)
    }

    private fun checkBirth() {
        val birthButton = findViewById<Button>(R.id.join_yearMonth_button)

        birthButton.setOnClickListener {
            val dialog = AlertDialog.Builder(this).create()
            val edialog : LayoutInflater = LayoutInflater.from(this)
            val mView : View = edialog.inflate(R.layout.dialog_datepicker,null)
            val year : NumberPicker = mView.findViewById(R.id.dialog_datePicker_year)
            val month : NumberPicker = mView.findViewById(R.id.dialog_datePicker_month)
            val day : NumberPicker = mView.findViewById(R.id.dialog_datePicker_day)
            val cancel : Button = mView.findViewById(R.id.dialog_datePicker_cancel)
            val save : Button = mView.findViewById(R.id.dialog_datePicker_save)

            //  순환 안되게 막기
            year.wrapSelectorWheel = false
            month.wrapSelectorWheel = false
            day.wrapSelectorWheel = false

            //  editText 설정 해제
            year.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            month.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            day.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

            //  최소값 설정
            year.minValue = 1970
            month.minValue = 1
            day.minValue = 1

            //  최대값 설정
            year.maxValue = 2021
            month.maxValue = 12
            day.maxValue = 31

            // 현재 지정값 설정
            year.value = myYear
            month.value = myMonth
            day.value = myDay

            //  취소 버튼 클릭 시
            cancel.setOnClickListener {
                dialog.dismiss()
                dialog.cancel()
            }
            //  완료 버튼 클릭 시
            save.setOnClickListener {
                birth = (year.value).toString() + "/" + (month.value).toString() + "/" + (day.value).toString()
                birthText.setText(birth)
                myYear = year.value
                myMonth = month.value
                myDay = day.value
                dialog.dismiss()
                dialog.cancel()
            }
            dialog.setView(mView)
            dialog.create()
            dialog.show()
        }
    }

    private fun checkGender() {
        val genderRadio = findViewById<RadioGroup>(R.id.join_genderRadio)

        when (genderRadio.checkedRadioButtonId) {
            R.id.join_female -> gender = "여자"
            R.id.join_male -> gender = "남자"
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
            checkGender()
            if (birth.isEmpty() || gender.isEmpty()) {
                Toast.makeText(this, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else {
                progressBar.visibility = View.VISIBLE
                Thread(Runnable {
                    Thread.sleep(1500)
                    Handler(Looper.getMainLooper()).post {
                        moveNext()
                    }
                }).start()
            }
        }
    }
}
