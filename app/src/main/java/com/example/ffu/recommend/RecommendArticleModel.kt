package com.example.ffu.recommend

data class RecommendArticleModel(
    val nickName: String,
    val age: String,
    val mbti: String,
    val imageUrl: String
) {
    constructor(): this("","","","")
}