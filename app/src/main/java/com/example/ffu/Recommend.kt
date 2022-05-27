package com.example.ffu

data class Recommend(

    val latitude: Double,
    val longitude: Double

    ) {
    constructor(): this(0.0, 0.0)
}