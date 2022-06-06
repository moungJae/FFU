package com.example.ffu.utils

data class RecommendArticle(
    val Id:String,
    val nickName: String,
    val ageJob: String,
    val introMe: String,
    val imageUrl: String
) {
    constructor(): this("","","","","")
}