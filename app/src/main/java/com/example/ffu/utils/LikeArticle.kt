package com.example.ffu.utils

data class LikeArticle (
    val Id:String,
    val nickName: String,
    val ageJob: String,
    val introMe: String,
    val imageUrl: String
) {
    constructor(): this("","","","","")
}