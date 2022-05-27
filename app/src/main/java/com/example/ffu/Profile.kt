package com.example.ffu

data class Profile(

    val age: String?,
    val birth: String?,
    val drinking : String?,
    val gender: String?,
    val hobby: String?,
    val introMe: String?,
    val job: String?,
    val mbti: String?,
    val nickname: String?,
    val personality: String?,
    val religion: String?,
    val smoke: String?,
    val tel: String?,
    val join: Boolean

    ) {
    constructor(): this("","","","",
        "","","","","",
        "","","","",false)
}