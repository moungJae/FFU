package com.example.ffu.utils

data class LikeArticle (
    val Id:String,
    val nickName: String,
    val age: String,
    val mbti: String,
    val imageUrl: String
) {
    constructor(): this("","","","","")
}