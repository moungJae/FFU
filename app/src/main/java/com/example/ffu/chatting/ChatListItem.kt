package com.example.ffu.chatting

data class ChatListItem(
    val senderId: String,
    val receiverId :String,
    val key: Long
) {

    constructor(senderId: String) : this("", "", 0)
}