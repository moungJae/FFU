package com.example.ffu.matching

data class LikeArticleModel(
    val Id:String,
    val nickName: String,
    val age: String,
    val mbti: String,
    val imageUrl: String
) {
    constructor(): this("","","","","")
}