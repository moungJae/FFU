package com.example.ffu.utils

data class ChatItem(

    val senderId: String?,
    val senderName: String?,
    val sendDate : String?,
    val message: String?,
    val type: Int
) {
    companion object {
        const val LEFT_TYPE = 0
        const val RIGHT_TYPE = 1
    }
    constructor(): this("", "","","",-1)
}