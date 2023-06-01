package com.example.orhan_ucar_odev7.models

import java.io.Serializable

data class Note(
    val id: Int,
    var title: String,
    var detail: String,
    var date: String
) : Serializable

