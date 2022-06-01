package com.example.ffu.utils

data class Animation(

    val permission: Boolean,
    val person: Boolean,
    val request: Boolean

    ) {
    constructor(): this(false, false,false)
}
