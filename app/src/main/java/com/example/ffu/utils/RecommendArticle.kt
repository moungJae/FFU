package com.example.ffu.utils

data class RecommendArticle(
    val Id:String,
    val nickName: String,
    val age: String,
    val mbti: String,
    val imageUrl: String
) {
    constructor(): this("","","","","")
}