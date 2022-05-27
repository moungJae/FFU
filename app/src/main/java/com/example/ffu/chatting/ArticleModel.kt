package com.example.ffu.chatting

import android.net.Uri
import com.google.firebase.storage.StorageReference

data class ArticleModel (
    val Id: String?,
    val Name: String?,
    val Gender: String?,
    val Birth: String?,
    val imageUri: String?
    )