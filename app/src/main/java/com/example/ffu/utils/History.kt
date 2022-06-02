package com.example.ffu.utils

data class History(
    val name : String,
    val time : String,
    val type : Int
){ constructor(): this("","",-1)
    companion object {
        const val SEND_TYPE = 0
        const val RECEIVE_TYPE = 1
        const val MATCH_TYPE = 2
    }
}