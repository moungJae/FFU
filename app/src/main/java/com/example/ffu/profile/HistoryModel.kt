package com.example.ffu.profile

data class HistoryModel(
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
