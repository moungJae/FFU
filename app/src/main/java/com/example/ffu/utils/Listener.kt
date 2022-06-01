package com.example.ffu.utils

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference

data class Listener(
    var reference : DatabaseReference,
    var listener : ChildEventListener
)