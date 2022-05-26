package com.example.ffu

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ListenerInfo(var userId : String, var reference : DatabaseReference, var listener : ValueEventListener)