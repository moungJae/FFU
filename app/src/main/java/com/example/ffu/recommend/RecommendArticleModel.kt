package com.example.ffu.recommend

data class RecommendArticleModel(
    val id: String,
    val nickname: String,
    val age: String,
    val mbti: String,
    val imageUrl: String
) {
    constructor(): this("","","","","")
}